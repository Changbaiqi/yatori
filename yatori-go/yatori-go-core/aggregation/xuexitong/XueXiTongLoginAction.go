package xuexitong

import (
	"yatori-go-core/api/xuexitong"
	log2 "yatori-go-core/utils/log"
)

func XueXiTLoginAction(cache *xuexitong.XueXiTUserCache) error {
	_, err := cache.LoginApi()
	if err == nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 登录成功")
	}
	return nil
}

func XueXiTPullCourseAction(cache *xuexitong.XueXiTUserCache) error {
	courses, err := cache.PullCourses()
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取失败")
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+courses)
	return nil
}
