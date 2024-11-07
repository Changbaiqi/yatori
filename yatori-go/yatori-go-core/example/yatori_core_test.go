package example

import (
	"strconv"
	"testing"
	yinghua "yatori-go-core/aggregation/yinghua"
	yinghuaApi "yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
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
		utils.LogPrintln(utils.INFO, "课程：", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))

	}
}

// 测试拉取对应课程的视屏列表
func TestPullCourseVideoList(t *testing.T) {
	utils.NOWLOGLEVEL = utils.INFO //设置日志登记为DEBUG
	//测试账号
	cache := yinghuaApi.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(cache)
	for _, courseItem := range list {
		utils.LogPrintln(utils.INFO, " ", courseItem.Id, " ", courseItem.Name, " ", strconv.FormatFloat(courseItem.Progress, 'b', 5, 32), " ", courseItem.StartDate.String(), " ", strconv.Itoa(courseItem.VideoCount), " ", strconv.Itoa(courseItem.VideoLearned))
		videoList, _ := yinghua.VideosListAction(cache, courseItem) //拉取视屏列表动作
		for _, videoItem := range videoList {
			utils.LogPrintln(utils.INFO, " ", "视屏：", videoItem.CourseId, " ", videoItem.Id, " ", videoItem.Name, " ", strconv.Itoa(int(videoItem.VideoDuration)))
		}
	}

}
