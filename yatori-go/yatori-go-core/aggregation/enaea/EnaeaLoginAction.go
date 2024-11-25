package enaea

import (
	"errors"
	"strings"
	"yatori-go-core/api/enaea"
	"yatori-go-core/utils/log"
)

// EnaeaLoginAction 学习公社登录
func EnaeaLoginAction(cache *enaea.EnaeaUserCache) (string, error) {
	api, err := enaea.LoginApi(cache)
	if err != nil {
		return "", err
	}
	if strings.Contains(api, "用户名或密码错误") {
		return "", errors.New("用户名或密码错误")
	}
	//fmt.Println(api)

	return api, nil
}

// LoginTimeoutAfreshAction 超时重登
func LoginTimeoutAfreshAction(cache *enaea.EnaeaUserCache, err error) {
	if err == nil {
		return
	}
	log.Print(log.INFO, "["+cache.Account+"] ", log.BoldRed, "检测到登录失效，正在进行重新登录逻辑...")
	_, err1 := EnaeaLoginAction(cache)
	if err1 != nil {
		log.Print(log.INFO, "["+cache.Account+"] ", log.BoldRed, "失效重登失败")
	}
	log.Print(log.INFO, "["+cache.Account+"] ", log.BoldGreen, "失效重登成功")
}
