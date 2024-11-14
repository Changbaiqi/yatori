package xuexitong

import (
	"encoding/json"
	"strconv"
	"yatori-go-core/api/entity"
	"yatori-go-core/api/xuexitong"
	log2 "yatori-go-core/utils/log"
)

func XueXiTPullCourseAction(cache *xuexitong.XueXiTUserCache) error {
	courses, err := cache.CourseListApi()
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取失败")
	}
	var xueXiTCourse entity.XueXiTCourse
	err = json.Unmarshal([]byte(courses), &xueXiTCourse)
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 解析失败")
		panic(err)
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+" 课程数量："+strconv.Itoa(len(xueXiTCourse.ChannelList)))
	log2.Print(log2.INFO, "["+cache.Name+"] "+courses)
	return nil
}

func XueXiTCourseDetailAction(cache *xuexitong.XueXiTUserCache, courseId string) error {
	course, err := cache.CourseDetailApi(courseId)
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取失败")
		panic(err)
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+course)
	return nil
}
