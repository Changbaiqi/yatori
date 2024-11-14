package xuexitong

import (
	"encoding/json"
	"io/ioutil"
	"net/http"
	"yatori-go-core/api/entity"
	log2 "yatori-go-core/utils/log"
)

// CourseListApi 拉取对应账号的课程数据
func (cache *XueXiTUserCache) CourseListApi() (string, error) {

	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, API_PULL_COURSES, nil)

	if err != nil {
		return "", err
	}
	req.Header.Add("Cookie", cache.cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		return "", err
	}
	return string(body), nil
}

// CourseDetailApi 拉取对应课程的详细信息
// 此处courseId 为 对应channelList中的 **chatid**
// TODO 目前是通过获得全部账号课程查找 暂不知是否有单独获得接口
func (cache *XueXiTUserCache) CourseDetailApi(courseId string) (string, error) {
	courses, err := cache.CourseListApi()
	if err != nil {
		return "", err
	}
	var xueXiTCourse entity.XueXiTCourse
	err = json.Unmarshal([]byte(courses), &xueXiTCourse)
	for channel := range xueXiTCourse.ChannelList {
		if xueXiTCourse.ChannelList[channel].Content.Chatid == courseId {
			marshal, _ := json.Marshal(xueXiTCourse.ChannelList[channel])
			return string(marshal), nil
		}
	}
	log2.Print(log2.INFO, "["+cache.Name+"] "+" 课程不存在")
	return "", nil
}
