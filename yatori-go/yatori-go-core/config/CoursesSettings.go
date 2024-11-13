package config

type CoursesSettings struct {
	Name         string   `json:"name"`
	IncludeExams []string `json:"includeExams"`
	ExcludeExams []string `json:"excludeExams"`
}
