package example

import (
	"fmt"
	"github.com/thedevsaddam/gojsonq"
	"log"
	"strconv"
	"sync"
	"testing"
	time2 "time"
	yinghua "yatori-go-core/aggregation/yinghua"
	yinghuaApi "yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
	log2 "yatori-go-core/utils/log"
)

// 账号登录测试
func TestLogin(t *testing.T) {
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
}

// 测试获取课程列表
func TestPullCourseList(t *testing.T) {
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(cache)
	for _, item := range list {
		log2.Print(log2.INFO, "课程：", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))

	}
}

// 测试拉取对应课程的视屏列表
func TestPullCourseVideoList(t *testing.T) {
	log2.NOWLOGLEVEL = log2.INFO //设置日志登记为DEBUG
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(cache)
	for _, courseItem := range list {
		log2.Print(log2.INFO, " ", courseItem.Id, " ", courseItem.Name, " ", strconv.FormatFloat(courseItem.Progress, 'b', 5, 32), " ", courseItem.StartDate.String(), " ", strconv.Itoa(courseItem.VideoCount), " ", strconv.Itoa(courseItem.VideoLearned))
		videoList, _ := yinghua.VideosListAction(cache, courseItem) //拉取视屏列表动作
		for _, videoItem := range videoList {
			log2.Print(log2.INFO, " ", "视屏：", videoItem.CourseId, " ", videoItem.Id, " ", videoItem.Name, " ", strconv.Itoa(int(videoItem.VideoDuration)))
		}
	}

}

// 用于登录保活
func keepAliveLogin(userCache yinghuaApi.UserCache) {
	for {
		api := yinghuaApi.KeepAliveApi(userCache)
		log2.Print(log2.INFO, " ", "登录保活状态：", api)
		time2.Sleep(time2.Second * 60)
	}
}

var wg sync.WaitGroup

// 刷视频的抽离函数
func videoListStudy(userCache yinghuaApi.UserCache, course yinghua.YingHuaCourse) {
	videoList, _ := yinghua.VideosListAction(userCache, course) //拉取对应课程的视屏列表

	// 提交学时
	for _, video := range videoList {
		log2.Print(log2.INFO, " ", video.Name)
		time := video.ViewedDuration //设置当前观看时间为最后看视屏的时间
		studyId := "0"
		for {
			if video.Progress == 100 {
				break //如果看完了，也就是进度为100那么直接跳过
			}
			sub := yinghuaApi.SubmitStudyTimeApi(userCache, video.Id, studyId, time) //提交学时
			if gojsonq.New().JSONString(sub).Find("msg") != "提交学时成功!" {
				time2.Sleep(5 * time2.Second)
				continue
			}

			studyId = strconv.Itoa(int(gojsonq.New().JSONString(sub).Find("result.data.studyId").(float64)))
			log2.Print(log2.INFO, " ", video.Name, " ", "提交状态：", gojsonq.New().JSONString(sub).Find("msg").(string), " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			time += 5
			time2.Sleep(5 * time2.Second)
			if time > video.VideoDuration {
				break //如果看完该视屏则直接下一个
			}
		}
	}
	wg.Done()
}

// 测试获取指定视屏并且刷课
func TestBrushOneLesson(t *testing.T) {
	utils.YatoriCoreInit()
	log2.NOWLOGLEVEL = log2.INFO //设置日志登记为DEBUG
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	go keepAliveLogin(cache)                   //携程保活
	list, _ := yinghua.CourseListAction(cache) //拉取课程列表
	for _, item := range list {
		wg.Add(1)
		log2.Print(log2.INFO, " ", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))
		go videoListStudy(cache, item) //多携程刷课
	}
	wg.Wait()
}

func TestCourseDetail(t *testing.T) {
	utils.YatoriCoreInit()
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}

	error := yinghua.LoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	fmt.Println(cache.GetToken())
	action, _ := yinghua.CourseDetailAction(cache, "1012027")
	fmt.Println(action)
	if error != nil {
		log.Fatal(error)
	}

}
