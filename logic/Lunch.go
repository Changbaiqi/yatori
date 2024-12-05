package logic

import (
	"math/rand"
	"os"
	"strings"
	"sync"
	"time"
	"yatori-go-console/config"
	"yatori-go-console/logic/cqie"
	"yatori-go-console/logic/enaea"
	"yatori-go-console/logic/xuexitong"
	"yatori-go-console/logic/yinghua"
	"yatori-go-console/utils"

	lg "github.com/yatori-dev/yatori-go-core/utils/log"
)

func Lunch() {
	//读取配置文件
	configJson := config.ReadConfig("./config.yaml")
	//初始化日志配置
	lg.LogInit(lg.StringToLOGLEVEL(configJson.Setting.BasicSetting.LogLevel), configJson.Setting.BasicSetting.LogOutFileSw == 1, configJson.Setting.BasicSetting.ColorLog, "./assets/log")
	//配置文件检查模块
	configJsonCheck(&configJson)
	//是否开启IP代理池
	isIpProxy(&configJson)

	brushBlock(&configJson)
	lg.Print(lg.INFO, lg.Red, "Yatori --- ", "所有任务执行完毕")
}

var platformLock sync.WaitGroup //平台锁
// brushBlock 刷课执行块
func brushBlock(configData *config.JSONDataForConfig) {
	//统一登录模块------------------------------------------------------------------
	yingHuaAccount := yinghua.FilterAccount(configData)
	yingHuaOperation := yinghua.UserLoginOperation(yingHuaAccount)
	enaeaAccount := enaea.FilterAccount(configData)
	enaeaOperation := enaea.UserLoginOperation(enaeaAccount)
	cqieAccount := cqie.FilterAccount(configData)
	cqieOpertation := cqie.UserLoginOperation(cqieAccount)
	xueXiTongAccount := xuexitong.FilterAccount(configData)
	xueXiTongOperation := xuexitong.UserLoginOperation(xueXiTongAccount)

	//统一刷课---------------------------------------------------------------------
	//英华
	platformLock.Add(1)
	go func() {
		yinghua.RunBrushOperation(configData.Setting, yingHuaAccount, yingHuaOperation) //英华统一刷课模块
		platformLock.Done()
	}()
	//学习公社
	platformLock.Add(1)
	go func() {
		enaea.RunBrushOperation(configData.Setting, enaeaAccount, enaeaOperation) //学习公社统一刷课模块
		platformLock.Done()
	}()
	platformLock.Add(1)
	go func() {
		cqie.RunBrushOperation(configData.Setting, cqieAccount, cqieOpertation) //重庆工程学院CQIE刷课模块
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

// configJsonCheck 配置文件检测检验
func configJsonCheck(configData *config.JSONDataForConfig) {
	if len(configData.Users) == 0 {
		lg.Print(lg.INFO, lg.BoldRed, "请先在config文件中配置好相应账号")
		os.Exit(0)
	}

	//防止用户填完整url
	for i, v := range configData.Users {
		if v.AccountType == "ENAEA" || v.AccountType == "CQIE" || v.AccountType == "XUEXITONG" { //跳过学习公社和CQIE
			continue
		} else if v.URL == "url" {
			lg.Print(lg.INFO, lg.BoldRed, "请先在config文件中配置好相应账号")
			os.Exit(0)
		}
		split := strings.Split(v.URL, "/")
		(*configData).Users[i].URL = (split[0] + "/" + split[1] + "/" + split[2])
	}
}

// isIpProxy 是否开启IP池代理
func isIpProxy(configData *config.JSONDataForConfig) {
	if configData.Setting.BasicSetting.IpProxySw == 0 {
		return
	}
	lg.Print(lg.INFO, lg.Yellow, "正在开启IP池代理...")
	lg.Print(lg.INFO, lg.Yellow, "正在检查IP池IP可用性...")
	reader, err := utils.IpFilesReader("./ip.txt")
	if err != nil {
		lg.Print(lg.INFO, lg.BoldRed, "IP代理池文件ip.txt读取失败，请确认文件格式或者内容是否正确")
		os.Exit(0)
	}

	for _, v := range reader {
		_, state, err := utils.CheckProxyIp(v)
		if err != nil {
			lg.Print(lg.INFO, " ["+v+"] ", lg.BoldRed, "该IP代理不可用，错误信息：", err.Error())
			continue
		}
		lg.Print(lg.INFO, " ["+v+"] ", lg.Green, "检测通过，状态：", state)
		utils.IPProxyPool = append(utils.IPProxyPool, v) //添加到IP代理池里面
	}
	lg.Print(lg.INFO, lg.BoldGreen, "IP检查完毕")
	//若无可用IP代理则直接退出
	if len(utils.IPProxyPool) == 0 {
		lg.Print(lg.INFO, lg.BoldRed, "无可用IP代理池，若要继续使用请先检查IP代理池文件内的IP可用性，或者在配置文件关闭IP代理功能")
		os.Exit(0)
	}
	//每隔一定时间切换IP
	go func() {
		for {
			time.Sleep(10 * time.Second)
			proxyIp := utils.IPProxyPool[rand.Intn(len(utils.IPProxyPool))]
			os.Setenv("HTTP_PROXY", "http://"+proxyIp)
			os.Setenv("HTTPS_PROXY", "https://"+proxyIp)
		}
	}()
}
