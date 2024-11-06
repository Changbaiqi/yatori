package yinghua

import (
	"errors"
	"fmt"
	"github.com/thedevsaddam/gojsonq"
	"strconv"
	"time"
	"yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
)

// 课程必要数据得截取
type YingHuaCourse struct {
	Id           string    //课程ID
	Name         string    //课程名称
	Mode         int       //课程模式
	StartDate    time.Time //开始时间
	EndDate      time.Time //结束时间
	Progress     float64   //学习进度
	VideoCount   int       //视屏总数
	VideoLearned int       //已学习视屏数量
}

// 课程列表
func CourseListAction(cache yinghua.UserCache) ([]YingHuaCourse, error) {
	var courseList []YingHuaCourse
	listJson := yinghua.CourseListApi(cache)
	utils.LogPrintln(utils.DEBUG, `[ `, cache.Account, ` ]`, `CourseListAction---`, listJson)

	//如果获取失败
	if gojsonq.New().JSONString(listJson).Find("msg") != "获取数据成功" {
		return []YingHuaCourse{}, errors.New("获取数据失败")
	}
	jsonList := gojsonq.New().JSONString(listJson).Find("result.list")
	// 断言为切片并遍历
	if items, ok := jsonList.([]interface{}); ok {
		for _, item := range items {
			// 每个 item 是 map[string]interface{} 类型
			if obj, ok := item.(map[string]interface{}); ok {
				startTime, _ := time.Parse("2006-01-02", obj["startDate"].(string)) //时间转换
				endTime, _ := time.Parse("2006-01-02", obj["endDate"].(string))     //时间转换
				courseList = append(courseList, YingHuaCourse{Id: strconv.Itoa(int(obj["id"].(float64))), Name: obj["name"].(string), Mode: int(obj["mode"].(float64)), Progress: obj["progress"].(float64), StartDate: startTime, EndDate: endTime, VideoCount: int(obj["videoCount"].(float64)), VideoLearned: int(obj["videoLearned"].(float64))})
			}
		}
	} else {
		fmt.Println("无法将数据转换为预期的类型")
	}
	return courseList, nil
}
