package examples

import (
	"crypto/tls"
	"fmt"
	"io/ioutil"
	"net/http"
	"testing"
	"time"
)

// 测试拉取console公告
func TestNoticePull(t *testing.T) {
	url := "https://yatori-dev.github.io/yatori-docs/notice/yatori-go-console-inform.txt"
	method := "GET"

	tr := &http.Transport{
		TLSClientConfig: &tls.Config{
			InsecureSkipVerify: true, // 跳过证书验证，仅用于开发环境
		},
	}
	client := &http.Client{
		Timeout:   10 * time.Second,
		Transport: tr,
	}
	req, err := http.NewRequest(method, url, nil)

	if err != nil {
		fmt.Println(err)
		// return "", nil
	}
	req.Header.Add("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.13; rv:88.0) Gecko/20100101 Firefox/88.0")

	res, err := client.Do(req)
	if err != nil {
		time.Sleep(100 * time.Millisecond)
		// return StartWork(userCache, courseId, nodeId, workId, retryNum-1, err)
	}
	defer res.Body.Close()

	body, err := ioutil.ReadAll(res.Body)
	if err != nil {
		fmt.Println(err)
		// return "", nil
	}
	fmt.Println(string(body))
}
