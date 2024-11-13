package yinghua

import (
	"errors"
	"github.com/thedevsaddam/gojsonq"
	"os"
	"strings"
	"yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
	"yatori-go-core/utils/log"
)

// 登录API聚合整理
// {"refresh_code":1,"status":false,"msg":"账号密码不正确"}
// {"_code": 1, "status": false,"msg": "账号登录超时，请重新登录", "result": {}}
func LoginAction(cache *yinghua.UserCache) error {
	for {

		path, cookie := yinghua.YingHuaLogin.VerificationCodeApi(cache) //获取验证码
		cache.SetCookie(cookie)
		img, _ := utils.ReadImg(path)                   //读取验证码图片
		codeResult := utils.AutoVerification(img)       //自动识别
		utils.DeleteFile(path)                          //删除验证码文件
		cache.SetVerCode(codeResult)                    //填写验证码
		jsonStr := yinghua.YingHuaLogin.LoginApi(cache) //执行登录
		log.Print(log.DEBUG, "["+cache.Account+"] "+"LoginAction---"+jsonStr)
		if gojsonq.New().JSONString(jsonStr).Find("msg") == "验证码有误！" {
			continue
		} else if gojsonq.New().JSONString(jsonStr).Find("redirect") == nil {
			return errors.New(gojsonq.New().JSONString(jsonStr).Find("msg").(string))
		}
		cache.SetToken(strings.Split(strings.Split(gojsonq.New().JSONString(jsonStr).Find("redirect").(string), "token=")[1], "&")[0]) //设置Token
		cache.SetSign(strings.Split(gojsonq.New().JSONString(jsonStr).Find("redirect").(string), "&sign=")[1])                         //设置签名
		log.Print(log.INFO, "["+cache.Account+"] "+" 登录成功")
		break
	}
	return nil
}

// 超时重登逻辑
func LoginTimeoutAfreshAction(cache *yinghua.UserCache, backJson string) {
	//未超时则直接return
	if !strings.Contains(backJson, "账号登录超时，请重新登录") {
		return
	}
	log.Print(log.INFO, "["+cache.Account+"] ", log.BoldRed, "检测到登录超时，进行重新登录逻辑...")
	err := LoginAction(cache)
	if err != nil {
		log.Print(log.INFO, "["+cache.Account+"] ", log.BoldRed, "超时重登失败")
		os.Exit(0)
	}
	log.Print(log.INFO, "["+cache.Account+"] ", log.BoldGreen, "超时重登成功")
}
