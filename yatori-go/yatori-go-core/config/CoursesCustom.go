package config

type CoursesCustom struct {
	VideoModel      int               `json:"videoModel"`
	AutoExam        int               `json:"autoExam"`
	ExcludeCourses  []string          `json:"excludeCourses"`
	IncludeCourses  []string          `json:"includeCourses"`
	CoursesSettings []CoursesSettings `json:"coursesSettings"`
}
