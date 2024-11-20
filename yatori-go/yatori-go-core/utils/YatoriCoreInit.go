package utils

import (
	"embed"
	_ "embed"
	ort "github.com/yalue/onnxruntime_go"
	"log"
	"os"
	"runtime"
)

//go:embed third_party/onnxruntime.dll
//go:embed third_party/onnxruntime_arm64.so
//go:embed third_party/onnxruntime_arm64.dylib
//go:embed third_party/onnxruntime.so
//go:embed third_party/common_old1.onnx
var onnxruntime embed.FS

//go:embed third_party/common_old1.onnx
var common_old1 []byte

func YatoriCoreInit() {
	//检查AI模型文件是否已经复制到本地
	f1, _ := PathExists(getSharedLibPath())          //检测运行库
	f2, _ := PathExists("./assets/common_old1.onnx") //检测AI模型
	if !f1 && !f2 {                                  //只要有一个文件没有那么就重新加载
		writeDLLToDisk() // 确保文件都加载了
	}
	loadAiEnvironment() //加载AI环境
}

// 将必要文件复制到当前目录下
func writeDLLToDisk() {
	PathExistForCreate("./assets")
	libPath := "./assets/" + getSharedLibPath()
	onnx := "./assets/common_old1.onnx"
	data, err := onnxruntime.ReadFile("third_party/" + getSharedLibPath())
	if err != nil {
		log.Println(err)
	}
	f1 := os.WriteFile(libPath, data, 0644)
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
	ort.SetSharedLibraryPath("./assets/" + getSharedLibPath())
	err := ort.InitializeEnvironment()
	if err != nil {
		panic(err)
	}
	//defer ort.DestroyEnvironment()

}

// 根据不同系统加载不同的运行库
func getSharedLibPath() string {
	if runtime.GOOS == "windows" {
		if runtime.GOARCH == "amd64" {
			return "onnxruntime.dll"
		}
	}
	if runtime.GOOS == "darwin" {
		if runtime.GOARCH == "arm64" {
			return "onnxruntime_arm64.dylib"
		}
	}
	if runtime.GOOS == "linux" {
		if runtime.GOARCH == "arm64" {
			return "onnxruntime_arm64.so"
		}
		return "onnxruntime.so"
	}
	panic("Unable to find a version of the onnxruntime library supporting this system.")
}
