package utils

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"log"
	"net/http"
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

// 通义千问API
func TongYiChatReplyApi(apiKey string, aiChatMessages AIChatMessages) (string, error) {
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://dashscope.aliyuncs.com/compatible-mode/v1/chat/completions"
	requestBody := map[string]interface{}{
		"model":    "qwen-plus",
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
func ChatGLMChatReplyApi(apiKey string, aiChatMessages AIChatMessages) (string, error) {
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://open.bigmodel.cn/api/paas/v4/chat/completions"
	requestBody := map[string]interface{}{
		"model":    "glm-4",
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
func XingHuoChatReplyApi(apiKey string, aiChatMessages AIChatMessages) (string, error) {
	client := &http.Client{
		Timeout: 30 * time.Second, // Set connection and read timeout
	}

	url := "https://spark-api-open.xf-yun.com/v1/chat/completions"
	requestBody := map[string]interface{}{
		"model":    "generalv3.5",
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
