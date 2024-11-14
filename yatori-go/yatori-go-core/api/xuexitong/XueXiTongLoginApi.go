package xuexitong

import (
	"bytes"
	"crypto/aes"
	"crypto/cipher"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
	"strings"
)

const (
	API_LOGIN_WEB    = "https://passport2.chaoxing.com/fanyalogin"
	API_PULL_COURSES = "https://mooc1-api.chaoxing.com/mycourse/backclazzdata"
	KEY              = "u2oh6Vu^HWe4_AES" // 注意 Go 语言中字符串默认就是 UTF-8 编码
)

type XueXiTUserCache struct {
	Name        string //用户使用Phone
	Password    string //用户密码
	JsonContent map[string]interface{}
	cookie      string //验证码用的session
}

// pad 确保数据长度是块大小的整数倍，以便符合块加密算法的要求
func pad(src []byte, blockSize int) []byte {
	padding := blockSize - len(src)%blockSize
	padText := bytes.Repeat([]byte{byte(padding)}, padding)
	return append(src, padText...)
}

func (cache *XueXiTUserCache) LoginApi() (string, error) {
	key := []byte(KEY)
	block, err := aes.NewCipher(key)
	if err != nil {
		fmt.Println("Error creating cipher:", err)
		return "", err
	}

	// 加密电话号码
	phonePadded := pad([]byte(cache.Name), block.BlockSize())
	phoneCipherText := make([]byte, len(phonePadded))
	mode := cipher.NewCBCEncrypter(block, key)
	mode.CryptBlocks(phoneCipherText, phonePadded)
	phoneEncrypted := base64.StdEncoding.EncodeToString(phoneCipherText)

	// 加密密码
	passwdPadded := pad([]byte(cache.Password), block.BlockSize())
	passwdCipherText := make([]byte, len(passwdPadded))
	mode = cipher.NewCBCEncrypter(block, key)
	mode.CryptBlocks(passwdCipherText, passwdPadded)
	passwdEncrypted := base64.StdEncoding.EncodeToString(passwdCipherText)

	// 发送请求
	resp, err := http.PostForm(API_LOGIN_WEB, url.Values{
		"fid":               {"-1"},
		"uname":             {phoneEncrypted},
		"password":          {passwdEncrypted},
		"t":                 {"true"},
		"forbidotherlogin":  {"0"},
		"validate":          {""},
		"doubleFactorLogin": {"0"},
		"independentId":     {"0"},
		"independentNameId": {"0"},
	})
	if err != nil {
		fmt.Println("Error sending request:", err)
		return "", err
	}
	defer resp.Body.Close()

	body, _ := ioutil.ReadAll(resp.Body)

	var jsonContent map[string]interface{}
	err = json.Unmarshal(body, &jsonContent)
	if err != nil {
		fmt.Println("Error parsing JSON:", err)
		return "", err
	}

	if status, ok := jsonContent["status"].(bool); !ok || !status {
		return "", err
	}
	values := resp.Header.Values("Set-Cookie")
	for _, v := range values {
		cache.cookie += strings.ReplaceAll(strings.ReplaceAll(v, "HttpOnly", ""), "Path=/", "")
	}

	cache.JsonContent = jsonContent
	return string(body), nil
}

// VerificationCodeApi 获取验证码和SESSION验证码,并返回文件路径和SESSION字符串
func (cache *XueXiTUserCache) VerificationCodeApi() (string, string) {
	//TODO 待完成
	return "", ""
}
