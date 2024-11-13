package main

import (
	"encoding/json"
	"fyne.io/fyne/v2"
	"fyne.io/fyne/v2/app"
	"fyne.io/fyne/v2/container"
	"fyne.io/fyne/v2/widget"
	"log"
	"os"
	"os/exec"
	"time"
)

//打包go build -ldflags "-s -w -H=windowsgui -extldflags '-static'"

type Users struct {
	Users []Person `json:"users"`
}
type Person struct {
	AccountType string `json:"accountType"`
	URL         string `json:"url"`
	Account     string `json:"account"`
	Password    string `json:"password"`
}

func start(users Users) {
	modifiedJson, err := json.MarshalIndent(users, "", "  ")
	if err != nil {
		log.Fatalf("无法将数据结构转换回 JSON: %v", err)
	}
	err = os.WriteFile("config.json", modifiedJson, 0644)
	if err != nil {
		log.Fatalf("无法写回文件: %v", err)
	}
	start := "start.bat"
	// 创建一个exec.Cmd结构体实例，用于执行外部命令
	// 这里我们使用cmd /c start cmd /k batFilePath的组合命令
	// cmd /c 表示在命令行中执行接下来的命令然后终止
	// start 表示启动一个新的窗口来运行指定的程序或命令
	// cmd /k 表示在新的命令行窗口中执行指定的命令并保持窗口打开
	cmd := exec.Command("cmd", "/c", "start", "cmd", "/k", start)
	err1 := cmd.Run()
	time.Sleep(2 * time.Second)
	if err1 != nil {

	} else {

	}
}
func main() {
	file, err := os.ReadFile("config.json")
	if err != nil {
		panic(err)
	}
	var users Users
	err = json.Unmarshal([]byte(file), &users)
	myApp := app.New()
	myWindow := myApp.NewWindow("ATRI1.1")
	AccountType := widget.NewEntry()
	AccountType.SetPlaceHolder("平台")
	URL := widget.NewEntry()
	URL.SetPlaceHolder("根网页")
	Account := widget.NewEntry()
	Account.SetPlaceHolder("账号")
	Password := widget.NewEntry()
	Password.SetPlaceHolder("密码")
	content := container.NewVBox(AccountType, URL, Account, Password, widget.NewButton("启动！", func() {
		if AccountType.Text != "" {
			users.Users[0].AccountType = AccountType.Text
		}
		if URL.Text != "" {
			users.Users[0].URL = URL.Text
		}
		if Account.Text != "" {
			users.Users[0].Account = Account.Text
		}
		if Password.Text != "" {
			users.Users[0].Password = Password.Text
		}
		start(users)
		myWindow.Close()
	}),
		//widget.NewRichText(&widget.TextSegment{Text: "1.YINGHUA", Style: widget.RichTextStyle{}}),
		widget.NewLabel("1.YINGHUA代表英华学堂,CANGHUI代表仓辉平台,ENAEA代表学习公社(南理仓辉是英华套皮，填英华)\n2.比如https://mooc.xxx.cn/，注意千万别带uri指别写成https://mooc.xxx.cn/xxx/xxx这样"),
	)
	myWindow.SetContent(content)
	myWindow.Resize(fyne.NewSize(400, 400))
	myWindow.ShowAndRun()
}

//widget.NewLabel("1.YINGHUA代表英华学堂,CANGHUI代表仓辉平台,ENAEA代表学习公社(南理仓辉是英华套皮，填英华)\n2.比如https://mooc.xxx.cn/，注意千万别带uri指别写成https://mooc.xxx.cn/xxx/xxx这样")
