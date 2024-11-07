package config

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
type AiSetting struct {
	AiType string `json:"aiType"`
	APIKEY string `json:"API_KEY"`
}
type Setting struct {
	EmailInform EmailInform `json:"emailInform"`
	AiSetting   AiSetting   `json:"aiSetting"`
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
