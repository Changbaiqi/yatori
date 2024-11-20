package yinghua

import (
	"bytes"
	"fmt"
	"io"
	"io/ioutil"
	"log"
	"math/rand"
	"mime/multipart"
	"net/http"
	"os"
	"strconv"
	"time"
	"yatori-go-core/api/entity"
	"yatori-go-core/utils"
)

type YingHuaUserCache struct {
	PreUrl   string //前置url
	Account  string //账号
	Password string //用户密码
	verCode  string //验证码
	cookie   string //验证码用的session
	token    string //保持会话的Token
	sign     string //签名
}

func (cache *YingHuaUserCache) GetVerCode() string {
	return cache.verCode
}
func (cache *YingHuaUserCache) SetVerCode(verCode string) {
	cache.verCode = verCode
}

func (cache *YingHuaUserCache) GetCookie() string {
	return cache.cookie
}
func (cache *YingHuaUserCache) SetCookie(cookie string) {
	cache.cookie = cookie
}

func (cache *YingHuaUserCache) GetToken() string {
	return cache.token
}
func (cache *YingHuaUserCache) SetToken(token string) {
	cache.token = token
}

func (cache *YingHuaUserCache) GetSign() string {
	return cache.token
}
func (cache *YingHuaUserCache) SetSign(sign string) {
	cache.sign = sign
}

//func (cache YingHuaUserCache) String() string {
//
//	return ""
//}

// LoginApi 登录接口
func (cache *YingHuaUserCache) LoginApi() (string, error) {

	url := cache.PreUrl + "/user/login.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("username", cache.Account)
	_ = writer.WriteField("password", cache.Password)
	_ = writer.WriteField("code", cache.verCode)
	_ = writer.WriteField("redirect", cache.PreUrl)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return "", err
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return "", err
	}
	req.Header.Add("Cookie", cache.cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	//fmt.Println(string(body))
	return string(body), nil
}

// VerificationCodeApi 获取验证码和SESSION验证码,并返回文件路径和SESSION字符串
func (cache *YingHuaUserCache) VerificationCodeApi() (string, string) {

	url := cache.PreUrl + fmt.Sprintf("/service/code?r=%d", time.Now().Unix())
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url, nil)
	req.Header.Add("Cookie", cache.cookie)

	if err != nil {
		fmt.Println(err)
		return "", ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", ""
	}
	defer res.Body.Close()

	codeFileName := "code" + strconv.Itoa(rand.Intn(99999)) + ".png" //生成验证码文件名称
	utils.PathExistForCreate("./assets/code/")                       //检测是否存在路径，如果不存在则创建
	filepath := fmt.Sprintf("./assets/code/%s", codeFileName)
	file, err := os.Create(filepath)
	if err != nil {
		log.Println(err)
		return "", ""
	}
	defer file.Close()

	_, err = io.Copy(file, res.Body)
	if err != nil {
		log.Println(err)
		return "", ""
	}
	return filepath, res.Header.Get("Set-Cookie")
}

// KeepAliveApi 登录心跳保活
func KeepAliveApi(UserCache YingHuaUserCache) string {

	url := UserCache.PreUrl + "/api/online.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", UserCache.token)
	//_ = writer.WriteField("schoolId", "7")
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// CourseListApi 拉取课程列表API
func (cache *YingHuaUserCache) CourseListApi() (string, error) {

	url := cache.PreUrl + "/api/course/list.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("type", "0")
	_ = writer.WriteField("token", cache.token)
	err := writer.Close()
	if err != nil {
		return "", err
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", cache.cookie)
	if err != nil {
		return "", err
	}
	req.Header.Add("Cookie", "tgw_I7_route=3d5c4e13e7d88bb6849295ab943042a2")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
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

// CourseDetailApi 获取课程详细信息API
func (cache *YingHuaUserCache) CourseDetailApi(courseId string) (string, error) {

	url := cache.PreUrl + "/api/course/detail.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("courseId", courseId)
	_ = writer.WriteField("token", cache.token)
	err := writer.Close()
	if err != nil {
		return "", err
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Add("Cookie", cache.cookie)

	if err != nil {
		return "", err
	}
	req.Header.Add("Cookie", "tgw_I7_route=3d5c4e13e7d88bb6849295ab943042a2")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		return "", err
	}
	return string(body), err
}

// CourseVideListApi 对应课程的视屏列表
func CourseVideListApi(UserCache YingHuaUserCache, courseId string /*课程ID*/) string {

	url := UserCache.PreUrl + "/api/course/chapter.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", UserCache.token)
	_ = writer.WriteField("courseId", courseId)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", UserCache.cookie)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// SubmitStudyTimeApi 提交学时
func SubmitStudyTimeApi(UserCache YingHuaUserCache, nodeId string /*对应视屏节点ID*/, studyId string /*学习分配ID*/, studyTime int /*提交的学时*/) (string, error) {

	url := UserCache.PreUrl + "/api/node/study.json"
	method := "POST"
	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("nodeId", nodeId)
	_ = writer.WriteField("token", UserCache.token)
	_ = writer.WriteField("terminal", "Android")
	_ = writer.WriteField("studyTime", strconv.Itoa(studyTime))
	_ = writer.WriteField("studyId", studyId)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return "", err
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return "", err
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	return string(body), nil
}

// VideStudyTimeApi 获取单个视屏的学习进度
func VideStudyTimeApi(userEntity entity.UserEntity, nodeId string) string {

	url := userEntity.PreUrl + "/api/node/video.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("nodeId", nodeId)
	_ = writer.WriteField("token", userEntity.Token)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// VideWatchRecodeApi 获取指定课程视屏观看记录
func VideWatchRecodeApi(UserCache YingHuaUserCache, courseId string, page int) string {

	url := UserCache.PreUrl + "/api/record/video.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", UserCache.token)
	_ = writer.WriteField("courseId", courseId)
	_ = writer.WriteField("page", strconv.Itoa(page))
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", UserCache.cookie)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// ExamDetailApi 获取考试信息
func ExamDetailApi(UserCache YingHuaUserCache, nodeId string) string {

	url := UserCache.PreUrl + "/api/node/exam.json?nodeId=" + nodeId
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("nodeId", nodeId)
	_ = writer.WriteField("token", UserCache.token)
	_ = writer.WriteField("terminal", "Android")
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Add("Cookie", UserCache.cookie)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// StartExam 开始考试接口
// {"_code":9,"status":false,"msg":"考试测试时间还未开始","result":{}}
func StartExam(userCache YingHuaUserCache, courseId, nodeId, examId string) (string, error) {

	url := userCache.PreUrl + "/api/exam/start.json?nodeId=" + nodeId + "&courseId=" + courseId + "&token=" + userCache.token + "&examId=" + examId
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url, nil)

	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	return string(body), nil
}

// GetExamTopicApi 获取所有考试题目，但是HTML，建议配合TurnExamTopic函数使用将题目html转成结构体
func GetExamTopicApi(UserCache YingHuaUserCache, nodeId, examId string) (string, error) {

	// Creating a custom HTTP client with timeout and SSL context (skip SSL setup for simplicity)
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	// Creating the request body (empty JSON object)
	body := []byte("{}")

	// Create the request
	url := fmt.Sprintf("%s/api/exam.json?nodeId=%s&examId=%s&token=%s", UserCache.PreUrl, nodeId, examId, UserCache.token)
	req, err := http.NewRequest("POST", url, bytes.NewReader(body))
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", UserCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", "application/json; charset=utf-8")

	// Perform the request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body
	bodyBytes, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}
	return string(bodyBytes), nil
}

// SubmitExamApi 提交考试答案接口
func SubmitExamApi(UserCache YingHuaUserCache, examId, answerId, answer, finish string) (string, error) {
	// Creating the HTTP client with a timeout (30 seconds)
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	// Create a buffer to hold the multipart form data
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)

	// Add form fields to the multipart data
	writer.WriteField("platform", "Android")
	writer.WriteField("version", "1.4.8")
	writer.WriteField("examId", examId)
	writer.WriteField("terminal", "Android")
	writer.WriteField("answerId", answerId)
	writer.WriteField("finish", finish)
	writer.WriteField("token", UserCache.token)

	// Add the answer fields
	if len(answer) == 1 {
		writer.WriteField("answer", string(answer[0]))
	} else {
		for i := 0; i < len(answer); i++ {
			writer.WriteField("answer[]", string(answer[i]))
		}
	}

	// Close the writer to finalize the multipart form data
	writer.Close()

	// Create the request with the necessary headers
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/exam/submit.json", UserCache.PreUrl), body)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", UserCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())

	// Perform the request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body (we're not using the body here, just ensuring the request goes through)
	bodyStr, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	// You can handle the response here if necessary

	return string(bodyStr), nil
}

// WorkDetailApi 获取作业信息
func WorkDetailApi(userCache YingHuaUserCache, nodeId string) string {

	url := userCache.PreUrl + "/api/node/work.json?nodeId=" + nodeId
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("nodeId", nodeId)
	_ = writer.WriteField("token", userCache.token)
	_ = writer.WriteField("terminal", "Android")
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Add("Cookie", userCache.cookie)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	return string(body)
}

// StartWork 开始做作业接口
// {"_code":9,"status":false,"msg":"您已完成作业，该作业仅可答题1次","result":{}}
func StartWork(userCache YingHuaUserCache, courseId, nodeId, workId string) (string, error) {
	url := userCache.PreUrl + "/api/work/start.json?nodeId=" + nodeId + "&courseId=" + courseId + "&token=" + userCache.token + "&workId=" + workId
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url, nil)

	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	return string(body), nil
}

// GetWorkApi 获取所有作业题目
func GetWorkApi(UserCache YingHuaUserCache, nodeId, workId string) (string, error) {

	url := UserCache.PreUrl + "/api/work.json?nodeId=" + nodeId + "&workId=" + workId + "&token=" + UserCache.token
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return "", nil
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	req.Header.Set("Content-Type", writer.FormDataContentType())
	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", nil
	}

	return string(body), nil
}

// SubmitWorkApi 提交作业答案接口
func SubmitWorkApi(UserCache YingHuaUserCache, workId, answerId, answer, finish string /*finish代表是否是最后提交并且结束考试，0代表不是，1代表是*/) (string, error) {

	// Creating the HTTP client with a timeout (30 seconds)
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	// Create a buffer to hold the multipart form data
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)

	// Add form fields to the multipart data
	writer.WriteField("platform", "Android")
	writer.WriteField("version", "1.4.8")
	writer.WriteField("workId", workId)
	writer.WriteField("terminal", "Android")
	writer.WriteField("answerId", answerId)
	writer.WriteField("finish", finish)
	writer.WriteField("token", UserCache.token)

	// Add the answer(s)
	if len(answer) == 1 {
		writer.WriteField("answer", string(answer[0]))
	} else {
		for i := 0; i < len(answer); i++ {
			writer.WriteField("answer[]", string(answer[i]))
		}
	}

	// Close the writer to finalize the multipart form data
	writer.Close()

	// Create the request with the necessary headers
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/work/submit.json", UserCache.PreUrl), body)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", UserCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())
	req.Header.Add("Cookie", UserCache.cookie)

	// Perform the request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Optionally, read the response body
	bodyBytes, _ := ioutil.ReadAll(resp.Body)
	return string(bodyBytes), nil
}

// WorkedDetail 获取最后作业得分接口
// {"_code":9,"status":false,"msg":"您已完成作业，该作业仅可答题1次","result":{}}
func WorkedFinallyDetailApi(userCache YingHuaUserCache, courseId, nodeId, workId string) (string, error) {
	url := userCache.PreUrl + "/api/work.json?nodeId=" + nodeId + "&workId=" + workId + "&token=" + userCache.token
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url, nil)

	if err != nil {
		fmt.Println(err)
		return "", err
	}
	req.Header.Add("Cookie", userCache.cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	return string(body), nil
}

// WorkedDetail 获取最后作业得分接口
// {"_code":9,"status":false,"msg":"您已完成作业，该作业仅可答题1次","result":{}}
func ExamFinallyDetailApi(userCache YingHuaUserCache, courseId, nodeId, workId string) (string, error) {
	url := userCache.PreUrl + "/api/exam.json?nodeId=" + nodeId + "&examId=" + workId + "&token=" + userCache.token
	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, url, nil)

	if err != nil {
		fmt.Println(err)
		return "", err
	}
	req.Header.Add("Cookie", userCache.cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

	res, err := client.Do(req)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		return "", err
	}
	return string(body), nil
}
