package xuexitong

import (
	"encoding/json"
	"strconv"
	"strings"
	"yatori-go-core/api/entity"
	"yatori-go-core/api/xuexitong"
	log2 "yatori-go-core/utils/log"
)

func XueXiTPullCourseAction(cache *xuexitong.XueXiTUserCache) error {
	courses, err := cache.CourseListApi()
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取失败")
	}
	var xueXiTCourse entity.XueXiTCourseJson
	err = json.Unmarshal([]byte(courses), &xueXiTCourse)
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 解析失败")
		panic(err)
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+" 课程数量："+strconv.Itoa(len(xueXiTCourse.ChannelList)))
	log2.Print(log2.INFO, "["+cache.Name+"] "+courses)
	return nil
}

// XueXiTCourseDetailForCourseIdAction 根据课程ID拉取学习课程详细信息
func XueXiTCourseDetailForCourseIdAction(cache *xuexitong.XueXiTUserCache, courseId string) (entity.XueXiTCourse, error) {
	courses, err := cache.CourseListApi()
	if err != nil {
		return entity.XueXiTCourse{}, err
	}
	var xueXiTCourse entity.XueXiTCourseJson
	err = json.Unmarshal([]byte(courses), &xueXiTCourse)
	for _, channel := range xueXiTCourse.ChannelList {
		if channel.Content.Chatid != courseId {
			continue
		}
		//marshal, _ := json.Marshal()
		sqUrl := channel.Content.Course.Data[0].CourseSquareUrl
		courseId := strings.Split(strings.Split(sqUrl, "courseId=")[1], "&personId")[0]
		personId := strings.Split(strings.Split(sqUrl, "personId=")[1], "&classId")[0]
		classId := strings.Split(strings.Split(sqUrl, "classId=")[1], "&userId")[0]
		userId := strings.Split(sqUrl, "userId=")[1]
		course := entity.XueXiTCourse{
			CourseName: channel.Content.Name,
			ClassId:    classId,
			CourseId:   courseId,
			Cpi:        strconv.FormatInt(channel.Cpi, 32),
			PersonId:   personId,
			UserId:     userId}
		return course, nil
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+" 课程不存在")
	return entity.XueXiTCourse{}, nil
}

// PullCourseChapterAction 获取对应课程的章节信息包括节点信息
func PullCourseChapterAction(cache *xuexitong.XueXiTUserCache, cpi, key int) (string, error) {
	//拉取对应课程的章节信息
	chapter, err := cache.PullChapter(cpi, key)
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取章节失败")
		return "", err
	}
	return chapter, nil
}
