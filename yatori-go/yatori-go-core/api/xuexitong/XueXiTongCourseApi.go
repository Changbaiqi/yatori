package xuexitong

//注意Api类文件主需要写最原始的接口请求和最后的json的string形式返回，不需要用结构体序列化。
//序列化和具体的功能实现请移步到Action代码文件中
import (
	"crypto/md5"
	"encoding/hex"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	"strconv"
	"time"
)

// CourseListApi 拉取对应账号的课程数据
func (cache *XueXiTUserCache) CourseListApi() (string, error) {

	method := "GET"

	client := &http.Client{}
	req, err := http.NewRequest(method, API_PULL_COURSES, nil)

	if err != nil {
		return "", err
	}
	req.Header.Add("Cookie", cache.cookie)
	req.Header.Add("User-Agent", "Apifox/1.0.0 (https://apifox.com)")

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

// 视屏点---------------------------------------------------------------
type PointVideoDto struct {
	ObjectID   string
	FID        int
	DToken     string
	Duration   int
	JobID      string
	OtherInfo  string
	Title      string
	RT         float64
	Logger     *log.Logger
	Session    *Session
	Attachment map[string]interface{}
}

type Session struct {
	Client *http.Client
	Acc    *Account
}

type Account struct {
	PUID string
}

type APIError struct {
	Message string
}

func (e *APIError) Error() string {
	return e.Message
}

func NewPointVideoDto(objectID string, logger *log.Logger, session *Session, attachment map[string]interface{}) *PointVideoDto {
	return &PointVideoDto{
		ObjectID:   objectID,
		Logger:     logger,
		Session:    session,
		Attachment: attachment,
	}
}

func (p *PointVideoDto) ParseAttachment() (bool, error) {
	attachments, ok := p.Attachment["attachments"].([]interface{})
	if !ok {
		p.Logger.Println("Failed to parse attachments")
		return false, errors.New("invalid attachment structure")
	}

	var point map[string]interface{}
	for _, a := range attachments {
		attachment, _ := a.(map[string]interface{})
		property, _ := attachment["property"].(map[string]interface{})
		if property["objectid"] == p.ObjectID {
			point = attachment
			break
		}
	}

	if point == nil {
		p.Logger.Println("Failed to locate resource")
		return false, nil
	}

	defaults, ok := p.Attachment["defaults"].(map[string]interface{})
	if !ok {
		return false, errors.New("invalid defaults structure")
	}
	p.FID = int(defaults["fid"].(float64))

	if jobID, exists := point["jobid"].(string); exists {
		p.JobID = jobID
		p.OtherInfo = point["otherInfo"].(string)
		p.RT = point["property"].(map[string]interface{})["rt"].(float64)
		p.Logger.Println("Parsed attachment successfully")
		isPassed, _ := point["isPassed"].(bool)
		return !isPassed, nil
	}

	p.Logger.Println("Non-task video ignored")
	return false, nil
}

func (p *PointVideoDto) Fetch() (bool, error) {
	params := url.Values{}
	params.Set("k", strconv.Itoa(p.FID))
	params.Set("flag", "normal")
	params.Set("_dc", strconv.FormatInt(time.Now().UnixNano()/1e6, 10))

	resp, err := p.Session.Client.Get(fmt.Sprintf("%s/%s?%s", APIChapterCardResource, p.ObjectID, params.Encode()))
	if err != nil {
		return false, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return false, fmt.Errorf("failed to fetch video, status code: %d", resp.StatusCode)
	}

	var jsonResponse map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&jsonResponse); err != nil {
		return false, err
	}

	p.DToken = jsonResponse["dtoken"].(string)
	p.Duration = int(jsonResponse["duration"].(float64))
	p.Title = jsonResponse["filename"].(string)

	if jsonResponse["status"].(string) == "success" {
		p.Logger.Printf("Fetch successful: %s", p)
		return true, nil
	}

	p.Logger.Println("Fetch failed")
	return false, nil
}

func (p *PointVideoDto) PlayReport(playingTime int) (map[string]interface{}, error) {
	clipTime := fmt.Sprintf("0_%d", p.Duration)
	hash := md5.Sum([]byte(fmt.Sprintf("[%s][%s][%s][%d][%s][%d][%s]",
		p.Session.Acc.PUID, p.JobID, p.ObjectID, playingTime*1000, "d_yHJ!$pdA~5", p.Duration*1000, clipTime)))
	enc := hex.EncodeToString(hash[:])

	params := url.Values{}
	params.Set("otherInfo", p.OtherInfo)
	params.Set("playingTime", strconv.Itoa(playingTime))
	params.Set("duration", strconv.Itoa(p.Duration))
	params.Set("jobid", p.JobID)
	params.Set("clipTime", clipTime)
	params.Set("clazzId", strconv.Itoa(p.FID))
	params.Set("objectId", p.ObjectID)
	params.Set("userid", p.Session.Acc.PUID)
	params.Set("isdrag", "0")
	params.Set("enc", enc)
	params.Set("rt", fmt.Sprintf("%f", p.RT))
	params.Set("dtype", "Video")
	params.Set("view", "pc")
	params.Set("_t", strconv.FormatInt(time.Now().UnixNano()/1e6, 10))

	reqURL := fmt.Sprintf("%s/%d/%s?%s", APIVideoPlayReport, p.FID, p.DToken, params.Encode())
	resp, err := p.Session.Client.Get(reqURL)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("failed to report play, status code: %d", resp.StatusCode)
	}

	var jsonResponse map[string]interface{}
	if err := json.NewDecoder(resp.Body).Decode(&jsonResponse); err != nil {
		return nil, err
	}

	if errorMsg, exists := jsonResponse["error"].(string); exists {
		return nil, &APIError{Message: errorMsg}
	}

	p.Logger.Printf("Play report successful: %d/%d", playingTime, p.Duration)
	return jsonResponse, nil
}
