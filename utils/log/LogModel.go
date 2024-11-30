package log

import lg "github.com/Yatori-Dev/yatori-go-core/utils/log"

func ModelPrint(isSw bool, LOGLEVEL lg.LOGLEVEL, logText ...interface{}) {
	if isSw {
		lg.Print(LOGLEVEL, logText...)
	}
}
