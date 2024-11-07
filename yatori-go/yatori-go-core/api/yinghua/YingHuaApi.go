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

type YingHuaLogin interface {
	LoginApi() string
	VerificationCodeApi() (string, string)
}

type UserCache struct {
	PreUrl   string //前置url
	Account  string //账号
	Password string //用户密码
	verCode  string //验证码
	cookie   string //验证码用的session
	token    string //保持会话的Token
	sign     string //签名
}

func (cache UserCache) GetVerCode() string {
	return cache.verCode
}
func (cache *UserCache) SetVerCode(verCode string) {
	cache.verCode = verCode
}

func (cache UserCache) GetCookie() string {
	return cache.cookie
}
func (cache *UserCache) SetCookie(cookie string) {
	cache.cookie = cookie
}

func (cache UserCache) GetToken() string {
	return cache.token
}
func (cache *UserCache) SetToken(token string) {
	cache.token = token
}

func (cache UserCache) GetSign() string {
	return cache.token
}
func (cache *UserCache) SetSign(sign string) {
	cache.sign = sign
}

// 登录接口
func (cache UserCache) LoginApi() string {

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
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)

	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("Cookie", cache.cookie)
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
	//fmt.Println(string(body))
	return string(body)
}

// 获取验证码和SESSION验证码,并返回文件路径和SESSION字符串
func (cache UserCache) VerificationCodeApi() (string, string) {

	url := cache.PreUrl + "/service/code?r=%7Btime()%7D"
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

// 登录心跳保活
func KeepAliveApi(userCache UserCache) string {

	url := userCache.PreUrl + "/api/online.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", userCache.token)
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

// 拉取课程列表API
func CourseListApi(cache UserCache) string {

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
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", cache.cookie)
	if err != nil {
		fmt.Println(err)
		return ""
	}
	req.Header.Add("Cookie", "tgw_I7_route=3d5c4e13e7d88bb6849295ab943042a2")
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

// 获取课程详细信息API
func CourseDetailApi(userEntity entity.UserEntity, courseId string) string {

	url := userEntity.PreUrl + "/api/course/detail.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("courseId", courseId)
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
	req.Header.Add("Cookie", "tgw_I7_route=3d5c4e13e7d88bb6849295ab943042a2")
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

// 对应课程的视屏列表
func CourseVideListApi(userCache UserCache, courseId string /*课程ID*/) string {

	url := userCache.PreUrl + "/api/course/chapter.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", userCache.token)
	_ = writer.WriteField("courseId", courseId)
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", userCache.cookie)
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

// 提交学时
func SubmitStudyTimeApi(userCache UserCache, nodeId string /*对应视屏节点ID*/, studyId string /*学习分配ID*/, studyTime int /*提交的学时*/) string {

	url := userCache.PreUrl + "/api/node/study.json"
	method := "POST"
	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("nodeId", nodeId)
	_ = writer.WriteField("token", userCache.token)
	_ = writer.WriteField("terminal", "Android")
	_ = writer.WriteField("studyTime", strconv.Itoa(studyTime))
	_ = writer.WriteField("studyId", studyId)
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

// 获取单个视屏的学习进度
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

// 获取指定课程视屏观看记录
func VideWatchRecodeApi(userCache UserCache, courseId string, page int) string {

	url := userCache.PreUrl + "/api/record/video.json"
	method := "POST"

	payload := &bytes.Buffer{}
	writer := multipart.NewWriter(payload)
	_ = writer.WriteField("platform", "Android")
	_ = writer.WriteField("version", "1.4.8")
	_ = writer.WriteField("token", userCache.token)
	_ = writer.WriteField("courseId", courseId)
	_ = writer.WriteField("page", strconv.Itoa(page))
	err := writer.Close()
	if err != nil {
		fmt.Println(err)
		return ""
	}

	client := &http.Client{}
	req, err := http.NewRequest(method, url, payload)
	req.Header.Set("Cookie", userCache.cookie)
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

// 开始考试接口
func StartExam(userCache UserCache, courseId, nodeId, examId string) (string, error) {

	// Creating a custom HTTP client with timeout and SSL context
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	// Set up the multipart form data
	body := &bytes.Buffer{}
	writer := multipart.NewWriter(body)

	// Add form data to the multipart request
	writer.WriteField("platform", "Android")
	writer.WriteField("version", "1.4.8")
	writer.WriteField("nodeId", nodeId)
	writer.WriteField("token", userCache.token)
	writer.WriteField("terminal", "Android")
	writer.WriteField("examId", examId)
	writer.WriteField("courseId", courseId)

	// Close the writer to finalize the multipart data
	writer.Close()

	// Create the request
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/exam/start.json", userCache.PreUrl), body)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Cookie", userCache.cookie)
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", userCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())

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
	fmt.Println(string(bodyBytes))
	return string(bodyBytes), nil
}

// 获取所有考试题目，但是HTML，建议配合TurnExamTopic函数使用将题目html转成结构体
func GetExamTopicApi(userCache UserCache, nodeId, examId string) (string, error) {

	// Creating a custom HTTP client with timeout and SSL context (skip SSL setup for simplicity)
	client := &http.Client{
		Timeout: 30 * time.Second,
	}

	// Creating the request body (empty JSON object)
	body := []byte("{}")

	// Create the request
	url := fmt.Sprintf("%s/api/exam.json?nodeId=%s&examId=%s&token=%s", userCache.PreUrl, nodeId, examId, userCache.token)
	req, err := http.NewRequest("POST", url, bytes.NewReader(body))
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", userCache.PreUrl)
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

// 提交考试答案接口
func SubmitExamApi(userCache UserCache, examId, answerId, answer, finish string) error {

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
	writer.WriteField("token", userCache.token)

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
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/exam/submit.json", userCache.PreUrl), body)
	if err != nil {
		return err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", userCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())

	// Perform the request
	resp, err := client.Do(req)
	if err != nil {
		return err
	}
	defer resp.Body.Close()

	// Read the response body (we're not using the body here, just ensuring the request goes through)
	_, err = ioutil.ReadAll(resp.Body)
	if err != nil {
		return err
	}

	// You can handle the response here if necessary

	return nil
}

// 获取所有作业题目
func GetWorkApi(userCache UserCache, nodeId string) (string, error) {

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
	writer.WriteField("nodeId", nodeId)
	writer.WriteField("token", userCache.token)
	writer.WriteField("terminal", "Android")

	// Close the writer to finalize the multipart form data
	writer.Close()

	// Create the request with the necessary headers
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/node/work.json", userCache.PreUrl), body)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", userCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())

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

// 提交作业答案接口
func SubmitWorkApi(userCache UserCache, workId, answerId, answer, finish string) (string, error) {

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
	writer.WriteField("token", userCache.token)

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
	req, err := http.NewRequest("POST", fmt.Sprintf("%s/api/work/submit.json", userCache.PreUrl), body)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", userCache.PreUrl)
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", writer.FormDataContentType())

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
