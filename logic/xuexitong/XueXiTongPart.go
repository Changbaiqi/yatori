package xuexitong

import (
	"fmt"
	"log"
	"strconv"
	"sync"
	"yatori-go-console/config"
	utils2 "yatori-go-console/utils"

	xuexitong "github.com/yatori-dev/yatori-go-core/aggregation/xuexitong"
	"github.com/yatori-dev/yatori-go-core/aggregation/xuexitong/point"
	"github.com/yatori-dev/yatori-go-core/api/entity"
	xuexitongApi "github.com/yatori-dev/yatori-go-core/api/xuexitong"
	lg "github.com/yatori-dev/yatori-go-core/utils/log"
)

var videosLock sync.WaitGroup //视屏锁
var usersLock sync.WaitGroup  //用户锁

// 用于过滤学习通账号
func FilterAccount(configData *config.JSONDataForConfig) []config.Users {
	var users []config.Users //用于收集英华账号
	for _, user := range configData.Users {
		if user.AccountType == "XUEXITONG" {
			users = append(users, user)
		}
	}
	return users
}

// 用户登录模块
func UserLoginOperation(users []config.Users) []*xuexitongApi.XueXiTUserCache {
	var UserCaches []*xuexitongApi.XueXiTUserCache
	for _, user := range users {
		if user.AccountType == "XUEXITONG" {
			cache := &xuexitongApi.XueXiTUserCache{Name: user.Account, Password: user.Password}
			error := xuexitong.XueXiTLoginAction(cache) // 登录
			if error != nil {
				lg.Print(lg.INFO, "[", lg.Green, cache.Name, lg.White, "] ", lg.Red, error.Error())
				log.Fatal(error) //登录失败则直接退出
			}
			// go keepAliveLogin(cache) //携程保活
			UserCaches = append(UserCaches, cache)
		}
	}
	return UserCaches
}

// 开始刷课模块
func RunBrushOperation(setting config.Setting, users []config.Users, userCaches []*xuexitongApi.XueXiTUserCache) {
	for i, user := range userCaches {
		usersLock.Add(1)
		go userBlock(setting, &users[i], user)
	}
	usersLock.Wait()
}

// 加锁，防止同时过多调用音频通知导致BUG,speak自带的没用，所以别改
// 以用户作为刷课单位的基本块
var soundMut sync.Mutex

func userBlock(setting config.Setting, user *config.Users, cache *xuexitongApi.XueXiTUserCache) {
	// list, err := xuexitong.XueXiTCourseDetailForCourseIdAction(cache, "261619055656961")
	courseList, err := xuexitong.XueXiTPullCourseAction(cache)
	if err != nil {
		log.Fatal(err)
	}
	for _, course := range courseList.ChannelList {
		videosLock.Add(1)
		// fmt.Println(course)
		go nodeListStudy(setting, user, cache, &course)
	}
	videosLock.Wait()
	lg.Print(lg.INFO, "[", lg.Green, cache.Name, lg.Default, "] ", lg.Purple, "所有待学习课程学习完毕")
	if setting.BasicSetting.CompletionTone == 1 { //如果声音提示开启，那么播放
		soundMut.Lock()
		utils2.PlayNoticeSound() //播放提示音
		soundMut.Unlock()
	}
	usersLock.Done()
}

// 课程节点执行
func nodeListStudy(setting config.Setting, user *config.Users, userCache *xuexitongApi.XueXiTUserCache, courseItem *entity.ChannelItem) {
	//过滤课程---------------------------------
	//排除指定课程
	if len(user.CoursesCustom.ExcludeCourses) != 0 && config.CmpCourse(courseItem.Content.Course.Data[0].Name, user.CoursesCustom.ExcludeCourses) {
		videosLock.Done()
		return
	}
	//包含指定课程
	if len(user.CoursesCustom.IncludeCourses) != 0 && !config.CmpCourse(courseItem.Content.Course.Data[0].Name, user.CoursesCustom.IncludeCourses) {
		videosLock.Done()
		return
	}
	course, err := xuexitong.XueXiTCourseDetailForCourseIdAction(userCache, courseItem.Content.Chatid)
	if err != nil {
		log.Fatal(err)
	}
	cpi, _ := strconv.Atoi(course.Cpi)
	key, _ := strconv.Atoi(course.ClassId)
	action, _ := xuexitong.PullCourseChapterAction(userCache, cpi, key)
	var nodes []int
	for _, item := range action.Knowledge {
		nodes = append(nodes, item.ID)
	}
	courseId, _ := strconv.Atoi(course.CourseId)
	_, fetchCards, err := xuexitong.ChapterFetchCardsAction(userCache, &action, nodes, 27, courseId, key, cpi)
	if err != nil {
		log.Fatal(err)
	}
	var (
		videoDTO entity.PointVideoDto
	)
	// 处理返回的任务点对象
	videoDTO = fetchCards[0].PointVideoDto
	videoCourseId, _ := strconv.Atoi(videoDTO.CourseID)
	videoClassId, _ := strconv.Atoi(videoDTO.ClassID)
	if courseId == videoCourseId && key == videoClassId {
		// 测试只对单独一个卡片测试
		card, err := xuexitong.PageMobileChapterCardAction(userCache, key, courseId, videoDTO.KnowledgeID, videoDTO.CardIndex, cpi)
		if err != nil {
			log.Fatal(err)
		}
		videoDTO.AttachmentsDetection(card)
		fmt.Println(videoDTO)
		point.ExecuteVideo(userCache, &videoDTO)
	} else {
		log.Fatal("任务点对象错误")
	}

}
