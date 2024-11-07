package utils

import (
	_ "embed"
	ort "github.com/yalue/onnxruntime_go"
	"log"
	"os"
)

// 初始化
//
//go:embed third_party/onnxruntime.dll
var onnxruntimeDLL []byte

//go:embed third_party/common_old1.onnx
var common_old1 []byte

func YatoriCoreInit() {
	//检查AI模型文件是否已经复制到本地
	f1, _ := PathExists("./assets/onnxruntime.dll")  //检测运行库
	f2, _ := PathExists("./assets/common_old1.onnx") //检测AI模型
	if !f1 && !f2 {                                  //只要有一个文件没有那么就重新加载
		writeDLLToDisk() // 确保文件都加载了
	}
	loadAiEnvironment() //加载AI环境
}

// 将必要文件复制到当前目录下
func writeDLLToDisk() {
	PathExistForCreate("./assets")
	dllPath := "./assets/onnxruntime.dll"
	onnx := "./assets/common_old1.onnx"
	f1 := os.WriteFile(dllPath, onnxruntimeDLL, 0644)
	if f1 != nil {
		log.Fatal(f1)
	}
	f2 := os.WriteFile(onnx, common_old1, 0644)
	if f2 != nil {
		log.Fatal(f2)
	}
}

// 加载AI环境
func loadAiEnvironment() {
	ort.SetSharedLibraryPath("./assets/onnxruntime.dll")

	err := ort.InitializeEnvironment()
	if err != nil {
		panic(err)
	}
	//defer ort.DestroyEnvironment()

}
