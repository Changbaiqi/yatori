package xuexitong

import (
	"encoding/json"
	"fmt"
	"log"
	"sort"
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

type ChaptersList struct {
	ChatID    string          `json:"chatid"`
	Knowledge []KnowledgeItem `json:"knowledge"`
}

// KnowledgeItem 结构体用于存储 knowledge 中的每个项目
type KnowledgeItem struct {
	JobCount     int           `json:"jobcount"` // 作业数量
	IsReview     int           `json:"isreview"` // 是否为复习
	Attachment   []interface{} `json:"attachment"`
	IndexOrder   int           `json:"indexorder"` // 节点顺序
	Name         string        `json:"name"`       // 章节名称
	ID           int           `json:"id"`
	Label        string        `json:"label"`        // 节点标签
	Layer        int           `json:"layer"`        // 节点层级
	ParentNodeID int           `json:"parentnodeid"` // 父节点 ID
	Status       string        `json:"status"`       // 节点状态
}

// PullCourseChapterAction 获取对应课程的章节信息包括节点信息
func PullCourseChapterAction(cache *xuexitong.XueXiTUserCache, cpi, key int) (ChaptersList, error) {
	//拉取对应课程的章节信息
	chapter, err := cache.PullChapter(cpi, key)
	if err != nil {
		log2.Print(log2.INFO, "["+cache.Name+"] "+" 拉取章节失败")
		return ChaptersList{}, err
	}
	var chaptersList ChaptersList
	var chapterMap map[string]interface{}
	err = json.Unmarshal([]byte(chapter), &chapterMap)
	if err != nil {
		fmt.Println("Error parsing JSON: ", err)
		return ChaptersList{}, err
	}
	chapterMapJson, err := json.Marshal(chapterMap["data"])
	// 解析 JSON 数据为 map 切片
	var chapterData []map[string]interface{}
	if err := json.Unmarshal(chapterMapJson, &chapterData); err != nil {
		log.Fatalf("Error parsing JSON: %v", err)
	}
	chatid := chapterData[0]["chatid"].(string)

	// 提取 knowledge
	var knowledgeData []map[string]interface{}
	course, ok := chapterData[0]["course"].(map[string]interface{})
	if !ok {
		fmt.Println("无法提取 course")
		return ChaptersList{}, err
	}
	data, ok := course["data"].([]interface{})
	if !ok {
		fmt.Println("无法提取 course data")
		return ChaptersList{}, err
	}
	if len(data) > 0 {
		knowledge, ok := data[0].(map[string]interface{})["knowledge"].(map[string]interface{})["data"].([]interface{})
		if !ok {
			fmt.Println("无法提取 knowledge")
			return ChaptersList{}, err
		}
		for _, item := range knowledge {
			knowledgeMap := item.(map[string]interface{})
			knowledgeData = append(knowledgeData, knowledgeMap)
		}
	} else {
		fmt.Println("course data 为空")
		return ChaptersList{}, err
	}

	// 将提取的数据封装到 CourseInfo 结构体中
	var knowledgeItems []KnowledgeItem
	for _, item := range knowledgeData {
		knowledgeItem := KnowledgeItem{
			JobCount:     int(item["jobcount"].(float64)),
			IsReview:     int(item["isreview"].(float64)),
			Attachment:   item["attachment"].(map[string]interface{})["data"].([]interface{}),
			IndexOrder:   int(item["indexorder"].(float64)),
			Name:         item["name"].(string),
			ID:           int(item["id"].(float64)),
			Label:        item["label"].(string),
			Layer:        int(item["layer"].(float64)),
			ParentNodeID: int(item["parentnodeid"].(float64)),
			Status:       item["status"].(string),
		}
		knowledgeItems = append(knowledgeItems, knowledgeItem)
	}
	chaptersList = ChaptersList{
		ChatID:    chatid,
		Knowledge: knowledgeItems,
	}
	// 按照任务点节点重排顺序
	sort.Slice(chaptersList.Knowledge, func(i, j int) bool {
		iLabelParts := strings.Split(chaptersList.Knowledge[i].Label, ".")
		jLabelParts := strings.Split(chaptersList.Knowledge[j].Label, ".")
		for k := range iLabelParts {
			if k >= len(jLabelParts) {
				return false // i has more parts, so it should come after j
			}
			iv, _ := strconv.Atoi(iLabelParts[k])
			jv, _ := strconv.Atoi(jLabelParts[k])
			if iv != jv {
				return iv < jv
			}
		}
		return len(iLabelParts) < len(jLabelParts)
	})
	fmt.Printf("获取课程章节成功 (共 %d 个)",
		len(chaptersList.Knowledge)) //  [%s(Cou.%s/Cla.%s)]
	return chaptersList, nil
}
