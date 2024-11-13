package config

type BasicSetting struct {
	CompletionTone int    `default:"1" json:"completionTone,omitempty"` //是否开启刷完提示音，0为关闭，1为开启，默认为1
	ColorLog       int    `json:"colorLog,omitempty"`                   //是否为彩色日志，0为关闭彩色日志，1为开启，默认为1
	LogOutFileSw   int    `json:"logOutFileSw,omitempty"`               //是否输出日志文件0代表不输出，1代表输出，默认为1
	LogLevel       string `json:"logLevel,omitempty"`                   //日志等级，默认INFO，DEBUG为找BUG调式用的，日志内容较详细，默认为INFO
	LogModel       int    `json:"logModel"`                             //日志模式，0代表以视屏提交学时基准打印日志，1代表以一个课程为基准打印信息，默认为0
}
