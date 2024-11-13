package common

import (
	"github.com/spf13/viper"
	"log"
	"yatori-go-core/config"
	"yatori-go-core/global"
	log2 "yatori-go-core/utils/log"
)

// InitConfig 初始化配置文件
func InitConfig(filePath string) {
	c := &config.Config{}
	viper.SetConfigName("TestData")
	viper.SetConfigType("yaml")
	viper.AddConfigPath(filePath)
	err := viper.ReadInConfig()
	if err != nil {
		log2.Print(log2.INFO, log2.BoldRed, "找不到配置文件")
	}
	err = viper.Unmarshal(c)
	viper.SetDefault("setting.basicSetting.logModel", 5)

	if err != nil {
		log2.Print(log2.INFO, log2.BoldRed, "配置文件读取失败，请检查配置文件填写是否正确")
		log.Fatal(err)
	}
	global.Config = c
}
