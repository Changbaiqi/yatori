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
	logic.Lunch()                    //启动yatori-console
}
