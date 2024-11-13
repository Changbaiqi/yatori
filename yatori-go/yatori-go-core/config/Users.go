package config

type Users struct {
	AccountType   string        `json:"accountType"`
	URL           string        `json:"url"`
	Account       string        `json:"account"`
	Password      string        `json:"password"`
	CoursesCustom CoursesCustom `json:"coursesCustom"`
}
