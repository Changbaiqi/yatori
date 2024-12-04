package xuexitong

import (
	"log"
	"yatori-go-console/config"

	xuexitong "github.com/yatori-dev/yatori-go-core/aggregation/xuexitong"
	xuexitongApi "github.com/yatori-dev/yatori-go-core/api/xuexitong"
	lg "github.com/yatori-dev/yatori-go-core/utils/log"
)

// 用于过滤学习通账号
func FilterAccount(configData *config.JSONDataForConfig) []config.Users {
	var users []config.Users //用于收集英华账号
	for _, user := range configData.Users {
		if user.AccountType == "XUEXITONG" {
			users = append(users, user)
		}
	}
	return users
}

// 开始刷课模块
func RunBrushOperation(setting config.Setting, users []config.Users, userCaches []*xuexitongApi.XueXiTUserCache) {

}

// 用户登录模块
func UserLoginOperation(users []config.Users) []*xuexitongApi.XueXiTUserCache {
	var UserCaches []*xuexitongApi.XueXiTUserCache
	for _, user := range users {
		if user.AccountType == "XUEXITONG" {
			cache := &xuexitongApi.XueXiTUserCache{Name: user.Account, Password: user.Password}
			error := xuexitong.XueXiTLoginAction(cache) // 登录
			if error != nil {
				lg.Print(lg.INFO, "[", lg.Green, cache.Name, lg.White, "] ", lg.Red, error.Error())
				log.Fatal(error) //登录失败则直接退出
			}
			// go keepAliveLogin(cache) //携程保活
			UserCaches = append(UserCaches, cache)
		}
	}
	return UserCaches
}
