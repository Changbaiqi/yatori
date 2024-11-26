package examples

import (
	"fmt"
	"net/http"
	"net/url"
	"testing"
	time2 "time"
	"yatori-go-core/common"
	"yatori-go-core/global"
	"yatori-go-core/utils"
)

func setup() {
	common.InitConfig("./")
}


// 账号登录测试
func TestLogin(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}
	error := yinghua.YingHuaLoginAction(&cache)
	if error != nil {

	}
}

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
	fmt.Println(chapter)
}

// 用于测试Config遵旨的初始化
func TestInitConfig(T *testing.T) {
	setup()
	users := global.Config.Users
	println(users)
}


// 测试获取课程列表
func TestPullCourseList(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}

	error := yinghua.YingHuaLoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(&cache)
	for _, item := range list {
		log2.Print(log2.INFO, "课程：", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))

	}
}

// 测试拉取对应课程的视屏列表
func TestPullCourseVideoList(t *testing.T) {
	log2.NOWLOGLEVEL = log2.INFO //设置日志登记为DEBUG
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}

	error := yinghua.YingHuaLoginAction(&cache)
	if error != nil {

	}
	list, _ := yinghua.CourseListAction(&cache)
	for _, courseItem := range list {
		log2.Print(log2.INFO, " ", courseItem.Id, " ", courseItem.Name, " ", strconv.FormatFloat(courseItem.Progress, 'b', 5, 32), " ", courseItem.StartDate.String(), " ", strconv.Itoa(courseItem.VideoCount), " ", strconv.Itoa(courseItem.VideoLearned))
		videoList, _ := yinghua.VideosListAction(&cache, courseItem) //拉取视屏列表动作
		for _, videoItem := range videoList {
			log2.Print(log2.INFO, " ", "视屏：", videoItem.CourseId, " ", videoItem.Id, " ", videoItem.Name, " ", strconv.Itoa(int(videoItem.VideoDuration)))
		}
	}

}

// 用于登录保活
func keepAliveLogin(UserCache yinghuaApi.YingHuaUserCache) {
	for {
		api := yinghuaApi.KeepAliveApi(UserCache)
		log2.Print(log2.INFO, " ", "登录保活状态：", api)
		time2.Sleep(time2.Second * 60)
	}
}

var wg sync.WaitGroup

// 刷视频的抽离函数
func videoListStudy(UserCache yinghuaApi.YingHuaUserCache, course yinghua.YingHuaCourse) {
	videoList, _ := yinghua.VideosListAction(&UserCache, course) //拉取对应课程的视屏列表

	// 提交学时
	for _, video := range videoList {
		log2.Print(log2.INFO, " ", video.Name)
		time := video.ViewedDuration //设置当前观看时间为最后看视屏的时间
		studyId := "0"
		for {
			if video.Progress == 100 {
				break //如果看完了，也就是进度为100那么直接跳过
			}
			sub, _ := yinghuaApi.SubmitStudyTimeApi(UserCache, video.Id, studyId, time) //提交学时
			if gojsonq.New().JSONString(sub).Find("msg") != "提交学时成功!" {
				time2.Sleep(5 * time2.Second)
				continue
			}

			studyId = strconv.Itoa(int(gojsonq.New().JSONString(sub).Find("result.data.studyId").(float64)))
			log2.Print(log2.INFO, " ", video.Name, " ", "提交状态：", gojsonq.New().JSONString(sub).Find("msg").(string), " ", "观看时间：", strconv.Itoa(time)+"/"+strconv.Itoa(video.VideoDuration), " ", "观看进度：", fmt.Sprintf("%.2f", float32(time)/float32(video.VideoDuration)*100), "%")
			time += 5
			time2.Sleep(5 * time2.Second)
			if time > video.VideoDuration {
				break //如果看完该视屏则直接下一个
			}
		}
	}
	wg.Done()
}

// 测试获取指定视屏并且刷课
func TestBrushOneLesson(t *testing.T) {
	utils.YatoriCoreInit()
	log2.NOWLOGLEVEL = log2.INFO //设置日志登记为DEBUG
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}

	error := yinghua.YingHuaLoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	go keepAliveLogin(cache)                    //携程保活
	list, _ := yinghua.CourseListAction(&cache) //拉取课程列表
	for _, item := range list {
		wg.Add(1)
		log2.Print(log2.INFO, " ", item.Id, " ", item.Name, " ", strconv.FormatFloat(item.Progress, 'b', 5, 32), " ", item.StartDate.String(), " ", strconv.Itoa(item.VideoCount), " ", strconv.Itoa(item.VideoLearned))
		go videoListStudy(cache, item) //多携程刷课
	}
	wg.Wait()
}

// 测试获取单个课程的详细信息
func TestCourseDetail(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}

	error := yinghua.YingHuaLoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	fmt.Println(cache.GetToken())
	action, _ := yinghua.CourseDetailAction(&cache, "1012027")
	fmt.Println(action)
	if error != nil {
		log.Fatal(error)
	}

}

// 测试获取考试的信息
func TestExamDetail(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[0]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}
	fmt.Println(cache)

	error := yinghua.YingHuaLoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	list, _ := yinghua.CourseListAction(&cache) //拉取课程列表
	//list[0]
	action, error := yinghua.VideosListAction(&cache, list[2])
	if error != nil {
		log.Fatal(error)
	}
	for _, node := range action {
		if node.Name != "第一单元 章节测试" {
			continue
		}
		fmt.Println(node)
		//api := yinghuaApi.ExamDetailApi(cache, node.Id)
		detailAction, _ := yinghua.ExamDetailAction(&cache, node.Id)
		//{"_code":9,"status":false,"msg":"考试测试时间还未开始","result":{}}
		exam, _ := yinghuaApi.StartExam(cache, node.CourseId, node.Id, detailAction[0].ExamId, 5, nil)
		fmt.Println(detailAction)
		fmt.Println(exam)
	}
}

// 测试获取作业的信息
func TestWorkDetail(t *testing.T) {
	utils.YatoriCoreInit()
	//测试账号
	setup()
	user := global.Config.Users[2]
	cache := yinghuaApi.YingHuaUserCache{
		PreUrl:   user.URL,
		Account:  user.Account,
		Password: user.Password,
	}

	error := yinghua.YingHuaLoginAction(&cache) // 登录
	if error != nil {
		log.Fatal(error) //登录失败则直接退出
	}
	list, _ := yinghua.CourseListAction(&cache) //拉取课程列表
	//list[0]
	action, error := yinghua.VideosListAction(&cache, list[0])
	if error != nil {
		log.Fatal(error)
	}
	for _, node := range action {
		if node.Name != "作业" {
			continue
		}
		fmt.Println(node)
		//获取作业详细信息
		detailAction, _ := yinghua.WorkDetailAction(&cache, node.Id)
		////{"_code":9,"status":false,"msg":"考试测试时间还未开始","result":{}}
		//开始写作业
		//yinghua.StartWorkAction(&cache, detailAction[0], global.Config.Setting.AiSetting.AiType, global.Config.Setting.AiSetting.APIKEY)
		fmt.Println(detailAction)
		//打印最终分数
		s, error := yinghua.WorkedFinallyScoreAction(&cache, detailAction[0])
		if error != nil {
			log.Fatal(error)
		}
		fmt.Println("最高分：", s)
	}
}

// ai自动回复测试，使用时请先自己配置好对应TestData里面的API_KEY
func TestAiAnswer(t *testing.T) {
	//测试账号
	setup()
	setting := global.Config.Setting
	messages := utils.AIChatMessages{Messages: []utils.Message{
		{
			Role:    "user",
			Content: "你好,你叫什么名字",
		},
	}}
	//api, _ := utils.TongYiChatReplyApi(setting.AiSetting.APIKEY, messages)
	api, _ := utils.AggregationAIApi("", setting.AiSetting.Model, setting.AiSetting.AiType, messages, setting.AiSetting.APIKEY)

	fmt.Println(api)
}

// 用于测试代理IP访问
func TestProxyIp(t *testing.T) {
	// 代理服务器地址
	proxyStr := "http://115.236.144.234:3128"
	proxyURL, err := url.Parse(proxyStr)
	if err != nil {
		fmt.Println("解析代理地址失败:", err)
		return
	}

	// 创建带有代理设置的 HTTP 客户端
	client := &http.Client{
		Transport: &http.Transport{
			Proxy: http.ProxyURL(proxyURL),
		},
		Timeout: 10 * time2.Second, // 设置超时时间
	}

	// 发起 HTTP 请求
	reqURL := "https://www.baidu.com" // 测试访问，返回请求者 IP
	req, err := http.NewRequest("GET", reqURL, nil)
	if err != nil {
		fmt.Println("创建请求失败:", err)
		return
	}

	// 设置 User-Agent 等头信息（根据需求）
	req.Header.Set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")

	// 执行请求
	resp, err := client.Do(req)
	if err != nil {
		fmt.Println("请求失败:", err)
		return
	}
	defer resp.Body.Close()

	// 打印响应状态和内容
	fmt.Println("响应状态:", resp.Status)
}
