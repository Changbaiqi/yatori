package xuexitong

import (
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"strconv"
)

// PullChapter 需要通过获取课程后的cpi 和key 参数 作为参数
// cpi 和 key 类型 在获取的json原始数据中为 int
// TODO暂时使用 int 类型作为测试类型 待Course结构体实体后传值 或 在action中使用一次XueXiTCourseDetailForCourseIdAction函数获取 待商定
func (cache *XueXiTUserCache) PullChapter(cpi int, key int) (string, error) {
	method := "GET"

	params := url.Values{}
	params.Add("id", strconv.Itoa(key))
	params.Add("personid", strconv.Itoa(cpi))
	params.Add("fields", "id,bbsid,classscore,isstart,allowdownload,chatid,name,state,isfiled,visiblescore,hideclazz,begindate,forbidintoclazz,coursesetting.fields(id,courseid,hiddencoursecover,coursefacecheck),course.fields(id,belongschoolid,name,infocontent,objectid,app,bulletformat,mappingcourseid,imageurl,teacherfactor,jobcount,knowledge.fields(id,name,indexOrder,parentnodeid,status,isReview,layer,label,jobcount,begintime,endtime,attachment.fields(id,type,objectid,extension).type(video)))")
	params.Add("view", "json")

	client := &http.Client{}
	req, err := http.NewRequest(method, ApiPullChapter+"?"+params.Encode(), nil)

	if err != nil {
		fmt.Println(err)
		return "", err
	}
	req.Header.Add("User-Agent", " Dalvik/2.1.0 (Linux; U; Android 12; SM-N9006 Build/70e2a6b.1) (schild:e9b05c3f9fb49fef2f516e86ac3c4ff1) (device:SM-N9006) Language/zh_CN com.chaoxing.mobile/ChaoXingStudy_3_6.3.7_android_phone_10822_249 (@Kalimdor)_4627cad9c4b6415cba5dc6cac39e6c96")
	req.Header.Add("Accept-Language", " zh_CN")
	req.Header.Add("Host", " mooc1-api.chaoxing.com")
	req.Header.Add("Connection", " Keep-Alive")
	req.Header.Add("Cookie", cache.cookie)

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

	fmt.Println(string(body))
	return string(body), nil
}
