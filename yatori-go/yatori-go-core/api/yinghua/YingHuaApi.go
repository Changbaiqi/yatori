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
func VideWatchRecode(userCache UserCache, courseId string, page int) string {

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
