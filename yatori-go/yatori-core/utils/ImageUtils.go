package utils

import (
	"fmt"
	"github.com/nfnt/resize"
	"image"
	"image/color"
	"image/draw"
	"image/gif"
	"image/jpeg"
	"image/png"
	"os"
	"strings"
)

// LoadImage 读取指定路径的图片并返回 image.Image 对象。
// 支持 JPEG 和 PNG 格式的图片。
func LoadImage(filePath string) (image.Image, error) {
	// 打开图片文件
	file, err := os.Open(filePath)
	if err != nil {
		return nil, fmt.Errorf("无法打开图片文件: %v", err)
	}
	defer file.Close()

	// 根据文件扩展名选择合适的解码器
	var img image.Image
	switch strings.ToLower(filePath[len(filePath)-4:]) {
	case ".jpg", "jpeg":
		img, err = jpeg.Decode(file)
	case ".png":
		img, err = png.Decode(file)
	default:
		return nil, fmt.Errorf("不支持的图片格式")
	}

	if err != nil {
		return nil, fmt.Errorf("图片解码失败: %v", err)
	}

	return img, nil
}

// 图片转一维数组数据
// ImageToGrayFloatArray 将灰度图像转换为一维浮点数组，范围为 [0.0, 1.0]
func ImageToGrayFloatArray(img image.Image) []float32 {
	bounds := img.Bounds()
	width, height := bounds.Max.X, bounds.Max.Y
	floatArray := make([]float32, width*height) // 只需单通道

	index := 0
	for y := 0; y < height; y++ {
		for x := 0; x < width; x++ {
			// 获取灰度值
			grayColor := color.GrayModel.Convert(img.At(x, y)).(color.Gray)
			grayValue := grayColor.Y

			// 将灰度值归一化为 [0.0, 1.0]
			floatArray[index] = float32(grayValue) / 255.0
			index++
		}
	}

	return floatArray
}

// ConvertToGray 将彩色图像转换为灰度图
func ConvertToGray(img image.Image) *image.Gray {
	bounds := img.Bounds()
	grayImg := image.NewGray(bounds)

	// 手动将 RGB 转换为灰度值，使用标准加权公式
	for y := bounds.Min.Y; y < bounds.Max.Y; y++ {
		for x := bounds.Min.X; x < bounds.Max.X; x++ {
			originalColor := img.At(x, y)
			r, g, b, _ := originalColor.RGBA()

			// RGB 转灰度，标准加权公式
			// Java的加权公式： 0.299 * R + 0.587 * G + 0.114 * B
			// Go 中 RGBA 的值范围是 [0, 65535]，需要缩放到 [0, 255]
			grayValue := uint8(0.299*float64(r)/257 + 0.587*float64(g)/257 + 0.114*float64(b)/257)
			grayImg.Set(x, y, color.Gray{Y: grayValue})
		}
	}

	return grayImg
}

func imageEncode(fileName string, file *os.File, rgba *image.RGBA) error {
	// 将图片和扩展名分离
	stringSlice := strings.Split(fileName, ".")
	// 根据图片的扩展名来运用不同的处理
	switch stringSlice[len(stringSlice)-1] {
	case "jpg":
		return jpeg.Encode(file, rgba, nil)
	case "jpeg":
		return jpeg.Encode(file, rgba, nil)
	case "gif":
		return gif.Encode(file, rgba, nil)
	case "png":
		return png.Encode(file, rgba)
	default:
		panic("不支持的图片类型")
	}
}

// ResizeImage 将图片缩放到指定宽度和高度并返回 RGB 图像
func ResizeImage(img image.Image, width, height uint) *image.RGBA {
	// 使用 resize 库进行平滑缩放
	resized := resize.Resize(width, height, img, resize.Lanczos3)

	// 创建目标 RGB 图像
	rgbImg := image.NewRGBA(image.Rect(0, 0, int(width), int(height)))

	// 将缩放后的图像绘制到 RGB 图像上
	draw.Draw(rgbImg, rgbImg.Bounds(), resized, image.Point{}, draw.Src)

	return rgbImg
}
