package main

import (
	"fmt"
	"yatori-go-console/config"
	"yatori-go-console/logic"
	utils2 "yatori-go-console/utils"
)

func main() {
	utils2.YatoriConsoleInit()       //初始化yatori-console
	fmt.Println(config.YaotirLogo()) //打印LOGO
	//proxyIp := "115.182.212.177:80"
	//os.Setenv("HTTP_PROXY", "http://"+proxyIp)
	//os.Setenv("HTTPS_PROXY", "http://"+proxyIp)

	logic.Lunch() //启动yatori-console
}
