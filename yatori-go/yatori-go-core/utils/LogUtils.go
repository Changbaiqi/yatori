package utils

import "log"

type LOGLEVEL int

const (
	INFO LOGLEVEL = iota
	DEBUG
)

var LOGLEVELSTR = []string{"INFO", "DEBUG"}

var NOWLOGLEVEL = INFO

// 返回枚举项的索引值
func (level LOGLEVEL) String() string {
	return LOGLEVELSTR[level]
}

// 返回枚举项的字符值
func (level LOGLEVEL) Index() int {
	return int(level)
}

// 日志初始化
func LogInit() {

}

// 打印日志
func LogPrintln(LOGLEVEL LOGLEVEL, logText ...string) {
	if LOGLEVEL == NOWLOGLEVEL {
		str := ""
		for _, v := range logText {
			str += v
		}
		log.Println(str)
	}
}
