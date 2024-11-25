package enaea

import (
	"errors"
	"fmt"
	"github.com/thedevsaddam/gojsonq"
	"regexp"
	"strconv"
	"strings"
	"time"
	"yatori-go-core/api/enaea"
)

type EnaeaProject struct {
	CircleNameShort  string    //短标题名称
	ClusterId        string    //组ID?反正不知道啥玩意
	ClusterName      string    //期数名称
	PlanState        int       //计划状态
	CircleId         string    //班级ID?反正不知道啥玩意
	CircleName       string    //班级期数名称
	StartTime        time.Time //开始时间
	EndTime          time.Time //结束时间
	CircleCardNumber string    //卡片数字编号
}
type EnaeaCourse struct {
	TitleTag          string  //课程对应侧边栏标签
	CourseTitle       string  //课程名称
	Remark            string  //课程节点名称
	CourseContentType string  //课程内容类型
	StudyProgress     float32 //学习进度
	CourseId          string  //课程ID
	CircleId          string
	SyllabusId        string
}
type EnaeaVideo struct {
	FileName      string  //视频文件名称
	TccId         string  //视频的TccID
	StudyProgress float32 //视频学习进度
	Id            string  //Id
	CourseId      string
	CircleId      string
	SCFUCKPKey    string //key
	SCFUCKPValue  string //value
}

// ProjectListAction 获取所需要学习的工程列表
func ProjectListAction(cache *enaea.EnaeaUserCache) ([]EnaeaProject, error) {
	var projects []EnaeaProject
	api, err := enaea.PullProjectsApi(cache)
	if err != nil {
		return nil, err
	}
	jsonList := gojsonq.New().JSONString(api).Find("result.list")
	// 断言为切片并遍历
	if items, ok := jsonList.([]interface{}); ok {
		for _, item := range items {
			// 每个 item 是 map[string]interface{} 类型
			if obj, ok := item.(map[string]interface{}); ok {
				startTime, _ := time.Parse("2006.01.02", strings.Split(obj["startEndTime"].(string), "-")[0])
				endTime, _ := time.Parse("2006.01.02", strings.Split(obj["startEndTime"].(string), "-")[1])
				projects = append(projects, EnaeaProject{
					CircleNameShort:  obj["circleNameShort"].(string),
					CircleId:         strconv.Itoa(int(obj["circleId"].(float64))),
					ClusterId:        strconv.Itoa(int(obj["clusterId"].(float64))),
					CircleName:       obj["circleName"].(string),
					ClusterName:      obj["clusterName"].(string),
					StartTime:        startTime,
					EndTime:          endTime,
					PlanState:        int(obj["planState"].(float64)),
					CircleCardNumber: obj["circleCardNumber"].(string),
				})
			}
		}
	}
	return projects, nil
}

// 拉取项目对应课程
func CourseListAction(cache *enaea.EnaeaUserCache, circleId string) ([]EnaeaCourse, error) {
	var courses []EnaeaCourse
	courseHTML, err := enaea.PullStudyCourseHTMLApi(cache, circleId)
	if err != nil {
		return nil, err
	}
	// Use regex to find the syllabusId in the response body
	regexPattern := fmt.Sprintf(`<a title="([^"]*)" href="circleIndexRedirect.do\?action=toNewMyClass&type=course&circleId=%s&syllabusId=([^&]*?)&isRequired=[^&]*&studentProgress=([\d]+)+">[^<]*</a>`, circleId)
	re := regexp.MustCompile(regexPattern)
	matches := re.FindAllStringSubmatch(courseHTML, -1)
	for _, v := range matches {
		api, err := enaea.PullStudyCourseListApi(cache, circleId, v[2])
		if err != nil {
			return nil, err
		}
		jsonList := gojsonq.New().JSONString(api).Find("result.list")
		// 断言为切片并遍历
		if items, ok := jsonList.([]interface{}); ok {
			for _, item := range items {
				// 每个 item 是 map[string]interface{} 类型
				if obj, ok := item.(map[string]interface{}); ok {
					remark := obj["remark"].(string)
					centerDTO := obj["studyCenterDTO"].(map[string]interface{})
					studyProgress, _ := strconv.ParseFloat(centerDTO["studyProgress"].(string), 64)
					courses = append(courses, EnaeaCourse{
						TitleTag:          v[1],
						CircleId:          circleId,
						SyllabusId:        v[2],
						Remark:            remark,
						StudyProgress:     float32(studyProgress),
						CourseId:          strconv.Itoa(int(centerDTO["courseId"].(float64))),
						CourseTitle:       centerDTO["courseTitle"].(string),
						CourseContentType: centerDTO["coursecontentType"].(string),
					},
					)
				}
			}
		}

	}
	return courses, nil
}

// 拉取对应课程的视频
func VideoListAction(cache *enaea.EnaeaUserCache, circleId, courseId string) ([]EnaeaVideo, error) {
	var videos []EnaeaVideo
	api, err := enaea.PullCourseVideoListApi(cache, circleId, courseId)
	if err != nil {
		return nil, err
	}
	jsonList := gojsonq.New().JSONString(api).Find("result.list")
	if items, ok := jsonList.([]interface{}); ok {
		for _, item := range items {
			if obj, ok := item.(map[string]interface{}); ok {

				studyProgress, _ := strconv.ParseFloat(obj["studyProgress"].(string), 64)
				if obj["filename"] == nil {
					fmt.Println("空")
				}
				videos = append(videos, EnaeaVideo{
					TccId:         obj["tccId"].(string),
					FileName:      obj["filename"].(string),
					StudyProgress: float32(studyProgress),
					Id:            strconv.Itoa(int(obj["id"].(float64))),
					CourseId:      courseId,
					CircleId:      circleId,
				})
			}
		}
	}
	return videos, nil
}

// 开始学习适配，首次学习视频前必须先调用这个函数接口
func StatisticTicForCCVideAction(cache *enaea.EnaeaUserCache, video *EnaeaVideo) error {
	json, K, V, err := enaea.StatisticTicForCCVideApi(cache, video.CourseId, video.Id, video.CircleId)
	if err != nil {
		return err
	}
	if gojsonq.New().JSONString(json).Find("success") == false {
		return errors.New(gojsonq.New().JSONString(json).Find("message").(string))
	}
	video.SCFUCKPKey = K
	video.SCFUCKPValue = V
	return nil
}

// 提交学时
// {"success": false,"message":"nologin"}
func SubmitStudyTimeAction(cache *enaea.EnaeaUserCache, video *EnaeaVideo, time int64) error {
	api, err := enaea.SubmitStudyTimeApi(cache, video.CircleId, video.SCFUCKPKey, video.SCFUCKPValue, video.Id, time)
	if err != nil {
		return err
	}
	if gojsonq.New().JSONString(api).Find("success") == false {
		return errors.New(gojsonq.New().JSONString(api).Find("message").(string))
	}
	video.StudyProgress = float32(gojsonq.New().JSONString(api).Find("progress").(float64))
	fmt.Println(api)
	return nil
}

// 超时重登
func LoginTimeoutAfreshAction(cache *enaea.EnaeaUserCache, err error) {
	if err == nil {
		return
	}
	EnaeaLoginAction(cache)
}
