package main

import (
	"strconv"
	"yatori-go-core/aggregation/yinghua"
	yinghua2 "yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
)

func main() {

	utils.NOWLOGLEVEL = utils.INFO //设置日志登记为DEBUG
	cache := yinghua2.UserCache{PreUrl: "https://swxymooc.csuft.edu.cn", Account: "2023021990", Password: "a047846"}
	error := yinghua.LoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(cache)
	for _, item := range list {
		utils.LogPrintln(utils.INFO, " ", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))

	}

}
