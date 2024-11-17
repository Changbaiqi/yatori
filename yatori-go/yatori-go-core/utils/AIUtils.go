package utils

import (
	"bytes"
	"encoding/json"
	"errors"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
	"sync"
	"time"
)

// ChatGLMChat struct that holds the chat messages.
type AIChatMessages struct {
	Messages []Message `json:"messages"`
}

// Message struct represents individual messages.
type Message struct {
	Role    string `json:"role"`
	Content string `json:"content"`
}

// AI锁，防止同时过多调用
var mut sync.Mutex

// AggregationAIApi 聚合所有AI接口，直接通过aiType判断然后返回内容
func AggregationAIApi(model, aiType string, aiChatMessages AIChatMessages, apiKey string) (string, error) {
	mut.Lock()
	defer mut.Unlock()
	if aiType == "CHATGLM" {
		return ChatGLMChatReplyApi(model, apiKey, aiChatMessages)
	} else if aiType == "XINGHUO" {
		return XingHuoChatReplyApi(model, apiKey, aiChatMessages)
	} else if aiType == "TONGYI" {
		return TongYiChatReplyApi(model, apiKey, aiChatMessages)
	} else if aiType == "DOUBAO" {
		return DouBaoChatReplyApi(model, apiKey, aiChatMessages)
	}
	return "", errors.New("AI Type: " + aiType)
}

// AICheck AI可用性检测
func AICheck(model, aiType string, apiKey string) error {
	aiChatMessages := AIChatMessages{
		Messages: []Message{
			{
				Role:    "user",
				Content: "你好",
			},
		},
	}

	if aiType == "" {
		return errors.New("AI Type: " + "请先填写AIType参数")
	}
	if apiKey == "" {
		return errors.New("无效apiKey，请检查apiKey是否正确填写")
	}

	if aiType == "CHATGLM" {
		_, err := ChatGLMChatReplyApi(model, apiKey, aiChatMessages)
		return err
	} else if aiType == "XINGHUO" {
		_, err := XingHuoChatReplyApi(model, apiKey, aiChatMessages)
		return err
	} else if aiType == "TONGYI" {
		_, err := TongYiChatReplyApi(model, apiKey, aiChatMessages)
		return err
	} else if aiType == "DOUBAO" {
		_, err := DouBaoChatReplyApi(model, apiKey, aiChatMessages)
		return err
	}
	return errors.New("AI Type: " + aiType)
}

// 通义千问API
func TongYiChatReplyApi(model, apiKey string, aiChatMessages AIChatMessages) (string, error) {
	if model == "" {
		model = "qwen-plus"
	}
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"
	requestBody := map[string]interface{}{
		"model":    model,
		"messages": aiChatMessages.Messages,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		return "", fmt.Errorf("failed to marshal JSON data: %v", err)
	}

	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("failed to create HTTP request: %v", err)
	}

	req.Header.Set("Authorization", "Bearer "+apiKey)
	req.Header.Set("Content-Type", "application/json")

	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("failed to execute HTTP request: %v", err)
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("failed to read response body: %v", err)
	}

	var responseMap map[string]interface{}
	if err := json.Unmarshal(body, &responseMap); err != nil {
		log.Printf("response body: %s", body)
		return "", fmt.Errorf("failed to parse JSON response: %v", err)
	}

	choices, ok := responseMap["choices"].([]interface{})
	if !ok || len(choices) == 0 {
		log.Printf("unexpected response structure: %v", responseMap)
		return "", fmt.Errorf("no choices in response")
	}

	message, ok := choices[0].(map[string]interface{})["message"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("failed to parse message from response")
	}

	content, ok := message["content"].(string)
	if !ok {
		return "", fmt.Errorf("content field missing or not a string in response")
	}

	return content, nil
}

// ChatGLM API
func ChatGLMChatReplyApi(model, apiKey string, aiChatMessages AIChatMessages) (string, error) {
	if model == "" {
		model = "glm-4"
	}
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://open.bigmodel.cn/api/paas/v4/chat/completions"
	requestBody := map[string]interface{}{
		"model":    model,
		"messages": aiChatMessages.Messages,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		return "", fmt.Errorf("failed to marshal JSON data: %v", err)
	}

	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("failed to create HTTP request: %v", err)
	}

	req.Header.Set("Authorization", "Bearer "+apiKey)
	req.Header.Set("Content-Type", "application/json")

	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("failed to execute HTTP request: %v", err)
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("failed to read response body: %v", err)
	}

	var responseMap map[string]interface{}
	if err := json.Unmarshal(body, &responseMap); err != nil {
		log.Printf("response body: %s", body)
		return "", fmt.Errorf("failed to parse JSON response: %v", err)
	}

	choices, ok := responseMap["choices"].([]interface{})
	if !ok || len(choices) == 0 {
		log.Printf("unexpected response structure: %v", responseMap)
		return "", fmt.Errorf("no choices in response")
	}

	message, ok := choices[0].(map[string]interface{})["message"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("failed to parse message from response")
	}

	content, ok := message["content"].(string)
	if !ok {
		return "", fmt.Errorf("content field missing or not a string in response")
	}

	return content, nil
}

// 星火API
func XingHuoChatReplyApi(model, apiKey string, aiChatMessages AIChatMessages) (string, error) {
	if model == "" {
		model = "generalv3.5" //默认模型
	}
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://spark-api-open.xf-yun.com/v1/chat/completions"
	requestBody := map[string]interface{}{
		"model":    model,
		"messages": aiChatMessages.Messages,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		return "", fmt.Errorf("failed to marshal JSON data: %v", err)
	}

	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("failed to create HTTP request: %v", err)
	}

	req.Header.Set("Authorization", "Bearer "+apiKey)
	req.Header.Set("Content-Type", "application/json")

	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("failed to execute HTTP request: %v", err)
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("failed to read response body: %v", err)
	}

	var responseMap map[string]interface{}
	if err := json.Unmarshal(body, &responseMap); err != nil {
		log.Printf("response body: %s", body)
		return "", fmt.Errorf("failed to parse JSON response: %v", err)
	}

	choices, ok := responseMap["choices"].([]interface{})
	if !ok || len(choices) == 0 {
		log.Printf("unexpected response structure: %v", responseMap)
		return "", fmt.Errorf("no choices in response")
	}

	message, ok := choices[0].(map[string]interface{})["message"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("failed to parse message from response")
	}

	content, ok := message["content"].(string)
	if !ok {
		return "", fmt.Errorf("content field missing or not a string in response")
	}

	return content, nil
}

// DouBaoChatReplyApi 豆包API
func DouBaoChatReplyApi(model, apiKey string, aiChatMessages AIChatMessages) (string, error) {
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://ark.cn-beijing.volces.com/api/v3/chat/completions"
	requestBody := map[string]interface{}{
		"model":    model,
		"messages": aiChatMessages.Messages,
	}

	jsonData, err := json.Marshal(requestBody)
	if err != nil {
		return "", fmt.Errorf("failed to marshal JSON data: %v", err)
	}

	req, err := http.NewRequest("POST", url, bytes.NewBuffer(jsonData))
	if err != nil {
		return "", fmt.Errorf("failed to create HTTP request: %v", err)
	}

	req.Header.Set("Authorization", "Bearer "+apiKey)
	req.Header.Set("Content-Type", "application/json")

	resp, err := client.Do(req)
	if err != nil {
		return "", fmt.Errorf("failed to execute HTTP request: %v", err)
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return "", fmt.Errorf("failed to read response body: %v", err)
	}

	var responseMap map[string]interface{}
	if err := json.Unmarshal(body, &responseMap); err != nil {
		log.Printf("response body: %s", body)
		return "", fmt.Errorf("failed to parse JSON response: %v", err)
	}

	choices, ok := responseMap["choices"].([]interface{})
	if !ok || len(choices) == 0 {
		log.Printf("unexpected response structure: %v", responseMap)
		return "", fmt.Errorf("no choices in response")
	}

	message, ok := choices[0].(map[string]interface{})["message"].(map[string]interface{})
	if !ok {
		return "", fmt.Errorf("failed to parse message from response")
	}

	content, ok := message["content"].(string)
	if !ok {
		return "", fmt.Errorf("content field missing or not a string in response")
	}

	return content, nil
}
