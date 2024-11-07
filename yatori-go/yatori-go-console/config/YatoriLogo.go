package config

import (
	"fmt"
	"os"
)

// 读取logo
func YaotirLogo() string {
	file, err := os.ReadFile("./logo.txt")
	if err != nil {
		fmt.Println(err)
	}
	return string(file)
}
