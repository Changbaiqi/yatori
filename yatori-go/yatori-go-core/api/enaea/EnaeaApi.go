package enaea

import (
	"crypto/md5"
	"encoding/hex"
	"fmt"
	"io/ioutil"
	"net/http"
	"strings"
	"time"
)

type EnaeaUserCache struct {
	Account  string //账号
	Password string //密码
	cookie   string //cookie
	ASUSS    string //token
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
			cache.ASUSS = cookie.Value
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
	req.Header.Add("Cookie", "ASUSS="+cache.ASUSS+";")
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
	req.Header.Add("Cookie", "ASUSS="+cache.ASUSS+";")
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
