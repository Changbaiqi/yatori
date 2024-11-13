package yinghua

import (
	"fmt"
	"yatori-go-core/utils"
)

// 英华AI回复预设前置话语
func YingHuaAiAnswer(aiType string, API_KEY string, examDetail YingHuaExam, examTopic utils.ExamTopic) {
	problem := `试卷名称：` + examDetail.Title + `
题目类型：` + examTopic.Type + `
题目内容：` + examTopic.Content
	fmt.Printf(problem)
}
