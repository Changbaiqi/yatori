package enaea

import (
	"bytes"
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"strings"
	"time"
)

type EnaeaUserCache struct {
	Account  string //账号
	Password string //密码
	cookie   string //cookie
	asuss    string //token
}

// LoginApi 学习公社登录
func LoginApi(cache *EnaeaUserCache) (string, error) {
	//时间戳
	simTime := fmt.Sprintf("%d", time.Now().UnixMilli())

	client := &http.Client{}
	url := fmt.Sprintf("https://passport.enaea.edu.cn/login.do?ajax=true&jsonp=ablesky_%s&j_username=%s&j_password=%s&_acegi_security_remember_me=false&_=%s",
		simTime,
		cache.Account,
		getMD5Str(cache.Password),
		simTime,
	)

	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", err
	}

	req.Header.Add("Referer", "https://study.enaea.edu.cn/login.do")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "passport.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return "", fmt.Errorf("failed to get a successful response, status code: %d", resp.StatusCode)
	}

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}
	for _, cookie := range resp.Cookies() {
		if cookie.Name == "ASUSS" {
			cache.asuss = cookie.Value
			break
		}
	}
	return string(body), nil
}

// 登录采用的加密算法
func getMD5Str(input string) string {
	hash := md5.Sum([]byte(input))
	return strings.ToUpper(hex.EncodeToString(hash[:]))
}

// ProjectsPull 拉取所有工程
func PullProjectsApi(cache *EnaeaUserCache) (string, error) {
	url := fmt.Sprintf("https://study.enaea.edu.cn/assessment.do?action=getMyCircleCourses&start=0&limit=200&isFinished=false&_=%d", time.Now().UnixMilli())

	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", err
	}

	// Add headers
	req.Header.Add("Cookie", "ASUSS="+cache.asuss+";")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	// Make the request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read and process the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(body), nil
}

// 获取对应项目的页面的HTML，用于截取侧边栏菜单栏
func PullStudyCourseHTMLApi(cache *EnaeaUserCache, circleId string) (string, error) {
	client := &http.Client{}
	url := fmt.Sprintf("https://study.enaea.edu.cn/circleIndexRedirect.do?action=toCircleIndex&circleId=%s&ct=%d", circleId, time.Now().UnixMilli())
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", err
	}

	// Set the headers
	req.Header.Add("Cookie", "ASUSS="+cache.asuss+";")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	// Make the HTTP request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(body), nil
}

// 获取对项目课程列表
func PullStudyCourseListApi(cache *EnaeaUserCache, circleId, syllabusId string) (string, error) {
	client := &http.Client{}
	url := fmt.Sprintf("https://study.enaea.edu.cn/circleIndex.do?action=getMyClass&start=0&limit=200&isCompleted=&circleId=%s&syllabusId=%s&categoryRemark=all&_=%d", circleId, syllabusId, time.Now().UnixMilli())

	// Create the HTTP request
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", err
	}

	// Set headers
	req.Header.Add("Cookie", "ASUSS="+cache.asuss+";")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	// Execute the HTTP request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}
	return string(body), nil
}

// PullCourseVideoListApi 拉取课程对应视屏列表
func PullCourseVideoListApi(cache *EnaeaUserCache, circleId, courseId string) (string, error) {
	// Construct the URL with courseId, circleId, and timestamp
	url := fmt.Sprintf("https://study.enaea.edu.cn/course.do?action=getCourseContentList&courseId=%s&circleId=%s&_=%d",
		courseId, circleId, time.Now().UnixMilli())

	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", err
	}

	// Add headers
	req.Header.Add("Cookie", "ASUSS="+cache.asuss+";")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	// Make the HTTP request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	return string(body), nil
}

// StatisticTicForCCVideApi 在观看视屏前一定要先调用这个函数，相当于告诉后端，我要看这个视屏了，请对这个视屏开始计时
func StatisticTicForCCVideApi(cache *EnaeaUserCache, courseId, courseContentId, circleId string) (string /*json*/, string /*key*/, string /*value*/, error) {
	// Construct the URL
	url := fmt.Sprintf("https://study.enaea.edu.cn/course.do?action=statisticForCCVideo&courseId=%s&coursecontentId=%s&circleId=%s&_=%d",
		courseId, courseContentId, circleId, time.Now().UnixMilli())

	client := &http.Client{}
	req, err := http.NewRequest("GET", url, nil)
	if err != nil {
		return "", "", "", err
	}

	// Add headers
	req.Header.Add("Cookie", "ASUSS="+cache.asuss+";")
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")

	// Execute the HTTP request
	resp, err := client.Do(req)
	if err != nil {
		return "", "", "", err
	}
	defer resp.Body.Close()

	// Read the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", "", "", err
	}

	SCFUCKP_K := ""
	SCFUCKP_V := ""
	for _, cookie := range resp.Cookies() {
		if strings.HasPrefix(cookie.Name, "SCFUCKP") {
			SCFUCKP_K = cookie.Name
			SCFUCKP_V = cookie.Value
			break
		}
	}

	return string(body), SCFUCKP_K, SCFUCKP_V, nil
}

// SubmitStudyTimeApi 提交学时
func SubmitStudyTimeApi(cache *EnaeaUserCache, circleId, SCFUCKPKey, SCFUCKPValue, id string, studyTime int64) (string, error) {
	// Create form data
	data := url.Values{}
	data.Set("id", id)
	data.Set("circleId", circleId)
	data.Set("ct", fmt.Sprintf("%d", studyTime))
	data.Set("finish", "false")

	// Create the HTTP request
	client := &http.Client{}
	req, err := http.NewRequest("POST", "https://study.enaea.edu.cn/studyLog.do", bytes.NewBufferString(data.Encode()))
	//req, err := http.NewRequest("POST", "https://study.enaea.edu.cn/thirdPlatform/record", bytes.NewBufferString(data.Encode()))
	if err != nil {
		return "", err
	}

	// Add headers
	cookie := fmt.Sprintf("ASUSS=%s;%s=%s", cache.asuss, SCFUCKPKey, SCFUCKPValue)
	req.Header.Add("Cookie", cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
	req.Header.Add("Accept", "*/*")
	req.Header.Add("Host", "study.enaea.edu.cn")
	req.Header.Add("Connection", "keep-alive")
	req.Header.Add("Content-Type", "application/x-www-form-urlencoded")

	// Execute the HTTP request
	resp, err := client.Do(req)
	if err != nil {
		return "", err
	}
	defer resp.Body.Close()

	// Read the response body
	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", err
	}

	// Convert JSON string to SubmitLearnTimeRequest struct
	//submitLearnTimeRequest, err := FromJsonString(string(body))
	//if err != nil {
	//	return nil, err
	//}
	return string(body), nil
}
