package config

import (
	"encoding/json"
	"log"
	"os"
)

type JSONDataForConfig struct {
	Setting Setting `json:"setting"`
	Users   []Users `json:"users"`
}
type EmailInform struct {
	Sw       int    `json:"sw"`
	SMTPHost string `json:"smtpHost"`
	SMTPPort string `json:"smtpPort"`
	Email    string `json:"email"`
	Password string `json:"password"`
}
type BasicSetting struct {
	CompletionTone *int `json:"completionTone,omitempty"`
	ColorLog       *int `json:"colorLog,omitempty"`
}
type AiSetting struct {
	AiType string `json:"aiType"`
	APIKEY string `json:"API_KEY"`
}
type Setting struct {
	BasicSetting BasicSetting `json:"basicSetting"`
	EmailInform  EmailInform  `json:"emailInform"`
	AiSetting    AiSetting    `json:"aiSetting"`
}
type CoursesSettings struct {
	Name         string   `json:"name"`
	IncludeExams []string `json:"includeExams"`
	ExcludeExams []string `json:"excludeExams"`
}
type CoursesCustom struct {
	VideoModel      int               `json:"videoModel"`
	AutoExam        int               `json:"autoExam"`
	ExcludeCourses  []string          `json:"excludeCourses"`
	IncludeCourses  []string          `json:"includeCourses"`
	CoursesSettings []CoursesSettings `json:"coursesSettings"`
}
type Users struct {
	AccountType   string        `json:"accountType"`
	URL           string        `json:"url"`
	Account       string        `json:"account"`
	Password      string        `json:"password"`
	CoursesCustom CoursesCustom `json:"coursesCustom"`
}

func (s *JSONDataForConfig) UnmarshalJSON(data []byte) error {
	type Alias JSONDataForConfig
	alias := &struct {
		*Alias
	}{Alias: (*Alias)(s)}

	if err := json.Unmarshal(data, alias); err != nil {
		return err
	}

	// 设置完成通知音默认开启
	if s.Setting.BasicSetting.CompletionTone == nil {
		num := 1
		s.Setting.BasicSetting.CompletionTone = &num
	}

	// 设置默认彩色日志
	if s.Setting.BasicSetting.ColorLog == nil {
		num := 1
		s.Setting.BasicSetting.ColorLog = &num
	}

	return nil
}

// 入读日志
func ReadConfig(filePath string) JSONDataForConfig {
	var configJson JSONDataForConfig
	content, err := os.ReadFile(filePath)
	if err != nil {
		log.Fatal(err)
	}
	err = json.Unmarshal(content, &configJson)
	if err != nil {
		log.Fatal(err)
	}
	return configJson
}
