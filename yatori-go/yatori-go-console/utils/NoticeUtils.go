package utils

import (
	"github.com/faiface/beep"
	"github.com/faiface/beep/mp3"
	"github.com/faiface/beep/speaker"
	"log"
	"os"
	"time"
	lg "yatori-go-core/utils/log"
)

// 播放通知音频
func PlayNoticeSound() {
	f, err := os.Open("./assets/sound/finishNotice.mp3")
	defer f.Close()
	streamer, format, err := mp3.Decode(f)
	if err != nil {
		lg.Print(lg.INFO, err)
		log.Fatal(err)
	}
	defer streamer.Close()
	speaker.Init(format.SampleRate, format.SampleRate.N(time.Second/10))
	lg.Print(lg.DEBUG, "music length :", streamer.Len())
	done := make(chan bool)
	speaker.Play(beep.Seq(streamer, beep.Callback(func() {
		done <- true
	})))
	<-done
}
