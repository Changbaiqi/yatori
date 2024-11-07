package main

import (
	"encoding/json"
	"fmt"
	"github.com/thedevsaddam/gojsonq"
	"log"
	"log/slog"
	"os"
	"strconv"
	"sync"
	time2 "time"
	"yatori-go-console/config"
	utils2 "yatori-go-console/utils"
	"yatori-go-core/aggregation/yinghua"
	yinghuaApi "yatori-go-core/api/yinghua"
	lg "yatori-go-core/utils/log"
)

var videosLock sync.WaitGroup
var usersLock sync.WaitGroup

func main() {
	utils2.YatoriConsoleInit()
	fmt.Println(config.YaotirLogo()) //打印LOGO
	lg.LogInit(true, "./assets/log") //初始化日志配置
	lg.NOWLOGLEVEL = lg.INFO         //设置日志登记为INFO
	content, err := os.ReadFile("./config.json")
	if err != nil {
		log.Fatal(err)
	}

	var configJson config.JSONDataForConfig
	err = json.Unmarshal(content, &configJson)
	if err != nil {
		log.Fatal(err)
	}
	for _, user := range configJson.Users {
		if user.AccountType == "YINGHUA" {
			usersLock.Add(1)
			go userBlock(user)
		}
	}
	usersLock.Wait()
}

func userBlock(user config.Users) {
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: user.URL, Account: user.Account, Password: user.Password}
	error := yinghua.LoginAction(&cache) // 登录
	if error != nil {
		if error.Error() == "学生信息不存在" {
			lg.Print(lg.INFO, "[", lg.Green, cache.Account, lg.White, "] ", lg.Red, "账号信息不存在")
		} else if error.Error() == "账号密码不正确" {
			lg.Print(lg.INFO, "[", lg.Green, cache.Account, lg.White, "] ", lg.Red, "账号密码不正确")
		}
		log.Fatal(error) //登录失败则直接退出
	}
	go keepAliveLogin(cache)                   //携程保活
	list, _ := yinghua.CourseListAction(cache) //拉取课程列表
	for _, item := range list {
		videosLock.Add(1)
		go videoListStudy(cache, item) //多携程刷课
	}
	lg.Print(lg.INFO, "[", lg.Green, cache.Account, lg.White, "] ", lg.Purple, "所有待学习课程学习完毕")
	go func() {
		videosLock.Add(1)
		utils2.PlayNoticeSound()
		videosLock.Done()
	}()
	videosLock.Wait()
}

// 用于登录保活
func keepAliveLogin(userCache yinghuaApi.UserCache) {
	for {
		api := yinghuaApi.KeepAliveApi(userCache)
		lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.White, "] ", lg.DarkGray, "登录心跳保活状态：", api)
		time2.Sleep(time2.Minute * 5) //每隔五分钟一次心跳保活
	}
}

// 刷视频的抽离函数
func videoListStudy(userCache yinghuaApi.UserCache, course yinghua.YingHuaCourse) {
	videoList, _ := yinghua.VideosListAction(userCache, course) //拉取对应课程的视屏列表

	// 提交学时
	for _, video := range videoList {
		lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.White, "] ", lg.Yellow, "正在学习视屏：", lg.White, " 【"+video.Name+"】 ")
		time := video.ViewedDuration //设置当前观看时间为最后看视屏的时间
		studyId := "0"
		for {
			if video.Progress == 100 {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.White, "] ", " 【", video.Name, "】 ", " ", lg.Blue, "学习完毕")
				break //如果看完了，也就是进度为100那么直接跳过
			}
			if video.VideoDuration == 0 {
				break //跳过非视屏的节点
			}
			sub := yinghuaApi.SubmitStudyTimeApi(userCache, video.Id, studyId, time) //提交学时
			slog.Debug("---", video.Id, sub)
			if gojsonq.New().JSONString(sub).Find("msg") != "提交学时成功!" {
				time2.Sleep(5 * time2.Second)
				continue
			}
			//打印日志部分
			studyId = strconv.Itoa(int(gojsonq.New().JSONString(sub).Find("result.data.studyId").(float64)))
			if gojsonq.New().JSONString(sub).Find("msg").(string) == "提交学时成功!" {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.White, "] ", " 【", video.Name, "】 >>> ", "提交状态：", lg.Green, gojsonq.New().JSONString(sub).Find("msg").(string), lg.White, " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			} else {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.White, "] ", " 【", video.Name, "】 >>> ", "提交状态：", lg.Red, gojsonq.New().JSONString(sub).Find("msg").(string), lg.White, " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			}
			time += 5
			time2.Sleep(5 * time2.Second)
			if time > video.VideoDuration {
				break //如果看完该视屏则直接下一个
			}
		}
	}
	videosLock.Done()
}
