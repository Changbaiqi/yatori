package examples

import (
	"fmt"
	"log"
	"strconv"
	"testing"
	"yatori-go-core/aggregation/xuexitong"
	xuexitongApi "yatori-go-core/api/xuexitong"
	"yatori-go-core/global"
	"yatori-go-core/utils"
)

// TestLoginXueXiTo 测试学习通登录以及课程数据拉取
func TestLoginXueXiTo(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[1]
	userCache := xuexitongApi.XueXiTUserCache{
		Name:     user.Account,
		Password: user.Password,
	}
	err := xuexitong.XueXiTLoginAction(&userCache)
	if err != nil {
		log.Fatal(err)
	}
	//拉取课程列表并打印
	xuexitong.XueXiTPullCourseAction(&userCache)
}

// 测试学习通单课程详情
func TestCourseDetailXueXiTo(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[1]
	userCache := xuexitongApi.XueXiTUserCache{
		Name:     user.Account,
		Password: user.Password,
	}
	err := xuexitong.XueXiTLoginAction(&userCache)
	if err != nil {
		log.Fatal(err)
	}
	action, err := xuexitong.XueXiTCourseDetailForCourseIdAction(&userCache, "260159398019074")
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println(action)
}

// TestCourseXueXiToChapter 用于测试学习通对应课程章节信息拉取
func TestCourseXueXiToChapter(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[1]
	userCache := xuexitongApi.XueXiTUserCache{
		Name:     user.Account,
		Password: user.Password,
	}

	err := xuexitong.XueXiTLoginAction(&userCache)
	if err != nil {
		log.Fatal(err)
	}
	//拉取对应课程信息
	_, err = xuexitong.XueXiTCourseDetailForCourseIdAction(&userCache, "260159398019074")
	//拉取对应课程的章节信息
	chapter, err := xuexitong.PullCourseChapterAction(&userCache, 283918535, 107333284)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("chatid:" + chapter.ChatID)
	for _, item := range chapter.Knowledge {
		fmt.Println("章节名称:" + item.Name)
		fmt.Println("标签:" + item.Label)
		fmt.Println("层级" + strconv.Itoa(item.Layer))
	}
}
