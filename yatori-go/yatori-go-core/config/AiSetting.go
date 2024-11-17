package config

type AiSetting struct {
	AiType string `json:"aiType"`
	Model  string `json:"model"`
	APIKEY string `json:"API_KEY" yaml:"API_KEY" mapstructure:"API_KEY"`
}
