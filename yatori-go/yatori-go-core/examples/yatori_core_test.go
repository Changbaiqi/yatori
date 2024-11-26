package examples

import (
	"fmt"
	"net/http"
	"net/url"
	"testing"
	time2 "time"
	"yatori-go-core/common"
	"yatori-go-core/global"
	"yatori-go-core/utils"
)

func setup() {
	common.InitConfig("./")
}

// 用于测试Config遵旨的初始化
func TestInitConfig(T *testing.T) {
	setup()
	users := global.Config.Users
	println(users)
}

// ai自动回复测试，使用时请先自己配置好对应TestData里面的API_KEY
func TestAiAnswer(t *testing.T) {
	//测试账号
	setup()
	setting := global.Config.Setting
	messages := utils.AIChatMessages{Messages: []utils.Message{
		{
			Role:    "user",
			Content: "你好,你叫什么名字",
		},
	}}
	//api, _ := utils.TongYiChatReplyApi(setting.AiSetting.APIKEY, messages)
	api, _ := utils.AggregationAIApi("", setting.AiSetting.Model, setting.AiSetting.AiType, messages, setting.AiSetting.APIKEY)

	fmt.Println(api)
}

// 用于测试代理IP访问
func TestProxyIp(t *testing.T) {
	// 代理服务器地址
	proxyStr := "http://115.236.144.234:3128"
	proxyURL, err := url.Parse(proxyStr)
	if err != nil {
		fmt.Println("解析代理地址失败:", err)
		return
	}

	// 创建带有代理设置的 HTTP 客户端
	client := &http.Client{
		Transport: &http.Transport{
			Proxy: http.ProxyURL(proxyURL),
		},
		Timeout: 10 * time2.Second, // 设置超时时间
	}

	// 发起 HTTP 请求
	reqURL := "https://www.baidu.com" // 测试访问，返回请求者 IP
	req, err := http.NewRequest("GET", reqURL, nil)
	if err != nil {
		fmt.Println("创建请求失败:", err)
		return
	}

	// 设置 User-Agent 等头信息（根据需求）
	req.Header.Set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")

	// 执行请求
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("请求失败:", err)
		return
	}
	defer resp.Body.Close()

	// 打印响应状态和内容
	fmt.Println("响应状态:", resp.Status)
}
