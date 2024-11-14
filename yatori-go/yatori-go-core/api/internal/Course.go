package internal

type CourseInterface interface {
	CourseListApi() (string, error)
	CourseDetailApi(courseId string) (string, error)
}
