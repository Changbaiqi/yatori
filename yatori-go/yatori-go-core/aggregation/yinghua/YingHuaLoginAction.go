package yinghua

import (
	"errors"
	"github.com/thedevsaddam/gojsonq"
	"strings"
	"yatori-go-core/api/yinghua"
	"yatori-go-core/utils"
)

// 登录API聚合整理
func LoginAction(cache *yinghua.UserCache) error {
	for {

		path, cookie := yinghua.YingHuaLogin.VerificationCodeApi(cache) //获取验证码
		cache.SetCookie(cookie)
		img, _ := utils.ReadImg(path)                                                   //读取验证码图片
		codeResult := utils.AutoVerification(img)                                       //自动识别
		utils.DeleteFile(path)                                                          //删除验证码文件
		cache.SetVerCode(codeResult)                                                    //填写验证码
		jsonStr := yinghua.YingHuaLogin.LoginApi(cache)                                 //执行登录
		utils.LogPrintln(utils.DEBUG, "[ "+cache.Account+" ]"+"LoginAction---"+jsonStr) //debug日志
		if gojsonq.New().JSONString(jsonStr).Find("msg") == "验证码有误！" {
			continue
		} else if gojsonq.New().JSONString(jsonStr).Find("msg") == "学生信息不存在" {
			return errors.New("学生信息不存在")
		} else {
			cache.SetToken(strings.Split(strings.Split(gojsonq.New().JSONString(jsonStr).Find("redirect").(string), "token=")[1], "&")[0]) //设置Token
			cache.SetSign(strings.Split(gojsonq.New().JSONString(jsonStr).Find("redirect").(string), "&sign=")[1])                         //设置签名
			utils.LogPrintln(utils.INFO, "["+cache.Account+"]"+" 登录成功")
			break
		}
	}
	return nil
}
