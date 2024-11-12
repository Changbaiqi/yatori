package examples

import "github.com/spf13/viper"

type TestData struct {
	Users []Account `json:"users"`
}
type Account struct {
	URL      string `json:"url"`
	Account  string `json:"account"`
	Password string `json:"password"`
}

// 读取测试用的账号信息
func readAccount() TestData {
	var testData TestData
	viper.SetConfigName("TestData")
	viper.AddConfigPath("./")
	viper.SetConfigType("yaml")
	err := viper.ReadInConfig()
	if err != nil {
		panic(err)
	}
	err = viper.Unmarshal(&testData)
	if err != nil {
		panic(err)
	}
	return testData
}
