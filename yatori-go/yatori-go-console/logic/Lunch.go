package logic

import (
	"sync"
	"yatori-go-console/config"
	"yatori-go-console/logic/xuexitong"
	"yatori-go-console/logic/yinghua"
	yinghua2 "yatori-go-core/api/yinghua"
	lg "yatori-go-core/utils/log"
)

func Lunch() {
	configJson := config.ReadConfig("./config.yaml")                                                                        //读取配置文件
	lg.LogInit(configJson.Setting.BasicSetting.LogOutFileSw == 1, configJson.Setting.BasicSetting.ColorLog, "./assets/log") //初始化日志配置
	lg.NOWLOGLEVEL = lg.INFO                                                                                                //设置日志登记为INFO
	BrushBlock(configJson)                                                                                                  //开始刷课
	lg.Print(lg.INFO, lg.Red, "Yatori --- ", "所有任务执行完毕")
}

var platformLock sync.WaitGroup //平台锁
// 刷课执行块
func BrushBlock(configData config.JSONDataForConfig) {
	yingHuaBlock, xueXiTongBlock := LoginBlock(configData) //英华统一登录模块
	//英华
	platformLock.Add(1)
	go func() {
		yinghua.RunBrushOperation(configData.Setting, yingHuaBlock) //英华统一刷课模块
		platformLock.Done()
	}()
	//学习通
	platformLock.Add(1)
	go func() {
		xuexitong.RunBrushOperation(configData.Setting, xueXiTongBlock) //英华统一刷课模块
		platformLock.Done()
	}()

	platformLock.Wait()
}

// 登录块
func LoginBlock(configData config.JSONDataForConfig) ([]yinghua2.UserCache, []yinghua2.UserCache) {
	yingHuaAccount := yinghua.FilterAccount(configData)
	yingHuaOperation := yinghua.UserLoginOperation(yingHuaAccount)
	xueXiTongAccount := xuexitong.FilterAccount(configData)
	xueXiTongOperation := xuexitong.UserLoginOperation(xueXiTongAccount)
	return yingHuaOperation, xueXiTongOperation
}
