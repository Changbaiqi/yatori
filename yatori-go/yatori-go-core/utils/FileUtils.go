package utils

import (
	"fmt"
	"image"
	"image/png"
	"os"
)

// 检测文件夹是否存在不存在
func PathExists(path string) (bool, error) {
	_, err := os.Stat(path)
	if err == nil {
		return true, nil
	}
	if os.IsNotExist(err) {
		return false, nil
	}
	return false, err
}

// 检测目录是否存在，不存在就创建
func PathExistForCreate(path string) {
	exists, _ := PathExists(path)
	if !exists {
		os.MkdirAll(path, os.ModePerm)
	}
}

// 从文件读取imgage
func ReadImg(imgFile string) (image.Image, error) {
	f, err := os.Open(imgFile)
	if err != nil {
		return nil, err
	}
	img, err := png.Decode(f)
	if err != nil {
		return nil, err
	}
	return img, nil
}

func DeleteFile(path string) {

	// 删除文件
	err := os.Remove(path)
	if err != nil {
		fmt.Println("Error:", err)
		return
	}
}
