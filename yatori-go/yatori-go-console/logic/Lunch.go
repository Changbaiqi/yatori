package logic

import (
	"os"
	"strings"
	"sync"
	"yatori-go-console/config"
	"yatori-go-console/logic/xuexitong"
	"yatori-go-console/logic/yinghua"
	lg "yatori-go-core/utils/log"
)

func Lunch() {
	configJson := config.ReadConfig("./config.yaml")                                                                                                                                       //读取配置文件
	lg.LogInit(lg.StringToLOGLEVEL(configJson.Setting.BasicSetting.LogLevel), configJson.Setting.BasicSetting.LogOutFileSw == 1, configJson.Setting.BasicSetting.ColorLog, "./assets/log") //初始化日志配置
	configJsonCheck(&configJson)                                                                                                                                                           //配置文件检查模块
	brushBlock(&configJson)
	lg.Print(lg.INFO, lg.Red, "Yatori --- ", "所有任务执行完毕")
}

var platformLock sync.WaitGroup //平台锁
// 刷课执行块
func brushBlock(configData *config.JSONDataForConfig) {
	//统一登录模块------------------------------------------------------------------
	yingHuaAccount := yinghua.FilterAccount(configData)
	yingHuaOperation := yinghua.UserLoginOperation(yingHuaAccount)
	xueXiTongAccount := xuexitong.FilterAccount(configData)
	xueXiTongOperation := xuexitong.UserLoginOperation(xueXiTongAccount)

	//统一刷课---------------------------------------------------------------------
	//英华
	platformLock.Add(1)
	go func() {
		yinghua.RunBrushOperation(configData.Setting, yingHuaAccount, yingHuaOperation) //英华统一刷课模块
		platformLock.Done()
	}()
	//学习通
	platformLock.Add(1)
	go func() {
		xuexitong.RunBrushOperation(configData.Setting, xueXiTongAccount, xueXiTongOperation) //英华统一刷课模块
		platformLock.Done()
	}()
	platformLock.Wait()
}

// 配置文件检测检验
func configJsonCheck(configData *config.JSONDataForConfig) {
	if len(configData.Users) == 0 {
		lg.Print(lg.INFO, lg.BoldRed, "请先在config文件中配置好相应账号")
		os.Exit(0)
	} else if configData.Users[0].URL == "url" {
		lg.Print(lg.INFO, lg.BoldRed, "请先在config文件中配置好相应账号")
		os.Exit(0)
	}
	//防止用户填完整url
	for i, v := range configData.Users {
		split := strings.Split(v.URL, "/")
		(*configData).Users[i].URL = (split[0] + "/" + split[1] + "/" + split[2])
	}
}
