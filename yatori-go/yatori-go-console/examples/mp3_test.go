package examples

import (
	"fmt"
	"github.com/faiface/beep/mp3"
	"github.com/faiface/beep/speaker"
	"github.com/stretchr/testify/assert"
	"log"
	"os"
	"testing"
	"time"
)

func TestMp3(t *testing.T) {
	f, err := os.Open(".assets/sound/finishNotice.mp3")
	assert.NoError(t, err)
	defer f.Close()
	streamer, format, err := mp3.Decode(f)
	if err != nil {
		log.Fatal(err)
	}
	defer streamer.Close()
	speaker.Init(format.SampleRate, format.SampleRate.N(time.Second/10))
	fmt.Println("music length :", streamer.Len())
	speaker.Play(streamer)
	select {}
	//
	//s, _, err := mp3.Decode(f)
	//assert.NoError(t, err)
	//s.Len()
	//speaker.Init(format, format.SampleRate.N(time.Second/30))
	//// The length of the streamer isn't tested because mp3 files have
	//// a different padding depending on the decoder used.
	//// https://superuser.com/a/1393775

}
