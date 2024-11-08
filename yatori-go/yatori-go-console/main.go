package main

import (
	"fmt"
	"github.com/thedevsaddam/gojsonq"
	"log"
	"strconv"
	"sync"
	time2 "time"
	"yatori-go-console/config"
	utils2 "yatori-go-console/utils"
	"yatori-go-core/aggregation/yinghua"
	yinghuaApi "yatori-go-core/api/yinghua"
	lg "yatori-go-core/utils/log"
)

var videosLock sync.WaitGroup //视屏锁
var usersLock sync.WaitGroup  //用户锁

func main() {
	utils2.YatoriConsoleInit()
	fmt.Println(config.YaotirLogo())                                            //打印LOGO
	configJson := config.ReadConfig("./config.json")                            //读取配置文件
	lg.LogInit(true, *configJson.Setting.BasicSetting.ColorLog, "./assets/log") //初始化日志配置
	lg.NOWLOGLEVEL = lg.INFO                                                    //设置日志登记为INFO

	for _, user := range configJson.Users {
		if user.AccountType == "YINGHUA" {
			usersLock.Add(1)
			go userBlock(configJson.Setting, user)
		}
	}
	usersLock.Wait()
	lg.Print(lg.INFO, lg.Red, "Yatori --- ", "所有任务执行完毕")
}

// 以用户作为刷课单位的基本块
func userBlock(setting config.Setting, user config.Users) {
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
	for _, item := range list {                //遍历所有待刷视屏
		videosLock.Add(1)
		go videoListStudy(cache, item) //多携程刷课
	}
	videosLock.Wait()
	lg.Print(lg.INFO, "[", lg.Green, cache.Account, lg.Default, "] ", lg.Purple, "所有待学习课程学习完毕")
	if *setting.BasicSetting.CompletionTone == 1 { //如果声音提示开启，那么播放
		utils2.PlayNoticeSound() //播放提示音
	}
	usersLock.Done()
}

// 用于登录保活
func keepAliveLogin(userCache yinghuaApi.UserCache) {
	for {
		api := yinghuaApi.KeepAliveApi(userCache)
		lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", lg.DarkGray, "登录心跳保活状态：", api)
		time2.Sleep(time2.Minute * 5) //每隔五分钟一次心跳保活
	}
}

// 刷视频的抽离函数
func videoListStudy(userCache yinghuaApi.UserCache, course yinghua.YingHuaCourse) {
	videoList, _ := yinghua.VideosListAction(userCache, course) //拉取对应课程的视屏列表

	// 提交学时
	for _, video := range videoList {
		lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", lg.Yellow, "正在学习视屏：", lg.Default, " 【"+video.Name+"】 ")
		time := video.ViewedDuration //设置当前观看时间为最后看视屏的时间
		studyId := "0"               //服务器端分配的学习ID
		for {
			time += 5
			if video.Progress == 100 {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", " 【", video.Name, "】 ", " ", lg.Blue, "学习完毕")
				break //如果看完了，也就是进度为100那么直接跳过
			}
			//如果没有视屏则跳过
			if !video.TabVideo {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", " 【", video.Name, "】 ", " ", lg.Red, "该节点没有视屏可能是作业或者是考试，Yatori当前版本并不支撑自动考试所以会自动跳过，若需要自动考试可以先用yatori-java版本")
				break
			}
			sub := yinghuaApi.SubmitStudyTimeApi(userCache, video.Id, studyId, time) //提交学时
			lg.Print(lg.DEBUG, "---", video.Id, sub)
			if gojsonq.New().JSONString(sub).Find("msg") != "提交学时成功!" {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", " 【", video.Name, "】 >>> ", "提交状态：", lg.Red, gojsonq.New().JSONString(sub).Find("msg"))
				time2.Sleep(10 * time2.Second)
				continue
			}
			//打印日志部分
			studyId = strconv.Itoa(int(gojsonq.New().JSONString(sub).Find("result.data.studyId").(float64)))
			if gojsonq.New().JSONString(sub).Find("msg").(string) == "提交学时成功!" {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", " 【", video.Name, "】 >>> ", "提交状态：", lg.Green, gojsonq.New().JSONString(sub).Find("msg").(string), lg.Default, " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			} else {
				lg.Print(lg.INFO, "[", lg.Green, userCache.Account, lg.Default, "] ", " 【", video.Name, "】 >>> ", "提交状态：", lg.Red, gojsonq.New().JSONString(sub).Find("msg").(string), lg.Default, " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			}
			time2.Sleep(5 * time2.Second)
			if time >= video.VideoDuration {
				break //如果看完该视屏则直接下一个
			}
		}
	}
	videosLock.Done()
}
