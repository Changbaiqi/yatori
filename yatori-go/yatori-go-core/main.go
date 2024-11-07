package main

import (
	"github.com/thedevsaddam/gojsonq"
	"strconv"
	time2 "time"
	"yatori-go-core/aggregation/yinghua"
	yinghua2 "yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
)

func main() {

	utils.NOWLOGLEVEL = utils.INFO //设置日志登记为DEBUG
	//测试账号
	cache := yinghua2.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(cache)
	for _, item := range list {
		utils.LogPrintln(utils.INFO, " ", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))

	}
	videoList, _ := yinghua.VideosListAction(cache, list[0])

	//提交学时
	time := 0
	studyId := "0"
	for _, item := range videoList {
		recode := yinghua2.VideWatchRecode(cache, list[0].Id, 10)
		utils.LogPrintln(utils.INFO, recode)

		utils.LogPrintln(utils.INFO, " ", item.Name)
		for {
			sub := yinghua2.SubmitStudyTimeApi(cache, "1458874", studyId, time)
			if gojsonq.New().JSONString(sub).Find("msg") != "提交学时成功!" {
				time2.Sleep(5 * time2.Second)
				continue
			}
			studyId = strconv.Itoa(int(gojsonq.New().JSONString(sub).Find("result.data.studyId").(float64)))
			utils.LogPrintln(utils.INFO, " ", sub)
			time += 5
			time2.Sleep(5 * time2.Second)
		}
	}

}
