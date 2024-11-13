package utils

import "gorm.io/gorm"

// 题目结构体
type Problem struct {
	gorm.Model
	Hash    string   //题目信息的Hash
	Type    string   //题目类型，比如单选，多选，简答题等
	Content string   //题目内容
	Answer  []string //答案
	Json    string   //json形式原内容
}
