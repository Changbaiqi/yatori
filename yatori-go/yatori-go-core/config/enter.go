package config

type Config struct {
	Setting Setting `json:"setting"`
	Users   []Users `json:"users"`
}

type Setting struct {
	BasicSetting BasicSetting `json:"basicSetting"`
	EmailInform  EmailInform  `json:"emailInform"`
	AiSetting    AiSetting    `json:"aiSetting"`
}
