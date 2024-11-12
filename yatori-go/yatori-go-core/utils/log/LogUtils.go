package log

import (
	"fmt"
	"os"
	"time"
	"yatori-go-core/utils"
)

type LOGLEVEL int

const (
	INFO LOGLEVEL = iota
	DEBUG
)

var LOGLEVELSTR = []string{"INFO", "DEBUG"}

var NOWLOGLEVEL = INFO

type TxT struct {
	Txt   string //文字
	Color int    //颜色对应枚举
}

// 返回枚举项的索引值
func (level LOGLEVEL) String() string {
	return LOGLEVELSTR[level]
}

// 返回枚举项的字符值
func (level LOGLEVEL) Index() int {
	return int(level)
}

// 通过字符串返回对应枚举值
func StringToLOGLEVEL(str string) LOGLEVEL {
	for i, v := range LOGLEVELSTR {
		if v == str {
			return LOGLEVEL(i)
		}
	}
	return INFO
}

var logFile *os.File         //输出日志路径
var outFlag bool = false     //是否输出日志到本地
var consoleColorFlag int = 1 //彩色控制台日志

// 日志初始化
func LogInit(logLevel LOGLEVEL, isOutFile bool /*是否输出到文件*/, isColorConsole int /*是否彩色控制台颜色0代表默认色，1代表彩色*/, outPath string /*日志输出路径*/) {
	NOWLOGLEVEL = logLevel
	outFlag = isOutFile
	consoleColorFlag = isColorConsole
	if outFlag {
		utils.PathExistForCreate(outPath) //检测是否存在日志文件夹
		timeStr := time.Now().Format("2006-01-02-15-04-05")
		file := outPath + "/log" + timeStr + ".txt"
		//openFile, err := os.OpenFile(file, os.O_RDWR|os.O_CREATE|os.O_APPEND, 0766)
		create, err := os.Create(file)
		if err != nil {
			panic(err)
		}
		logFile = create
	}
}

// 打印日志
func Print(LOGLEVEL LOGLEVEL, logText ...interface{}) {
	if LOGLEVEL == NOWLOGLEVEL {
		timeStr := time.Now().Format("[2006-01-02 15:04:05] ") //获取时间
		consoleStr := timeStr + ""
		fileStr := timeStr + ""
		colorFlag := White
		for _, v := range logText {
			if value, ok := v.(string); ok {
				if consoleColorFlag == 1 {
					consoleStr += ColorTxt(colorFlag, value)
				} else {
					consoleStr += value
				}
				fileStr += value
			} else if value, ok := v.(int); ok {
				colorFlag = value
			}
		}
		fmt.Println(consoleStr) //打印日志到控制台
		if outFlag {
			logFile.WriteString(fileStr + "\n")
		}
	}

}
