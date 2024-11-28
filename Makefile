.PHONY: all build run gotool clean help

BINARY_NAME="app"
RELEASE_DIR="release"


build-all: gotool
	mkdir -p $(RELEASE_DIR)
	make build-linux
	make build-windows

build-linux:
	CGO_ENABLED=0 GOOS=linux GOARCH=amd64 go build -o $(RELEASE_DIR)/$(BINARY_NAME)

build-windows:
	CGO_ENABLED=0 GOOS=windows GOARCH=amd64 go build -o $(RELEASE_DIR)/$(BINARY_NAME).exe

run:
	@go run ./

gotool:
	go fmt ./
	go vet ./

clean:
	@if [ -f $(RELEASE_DIR)/$(BINARY_NAME) ] ; then rm $(RELEASE_DIR)/** ; fi

help:
	@echo "make - 格式化 Go 代码, 并编译生成二进制文件"
	@echo "make build - 编译 Go 代码, 生成二进制文件"
	@echo "make run - 直接运行 Go 代码"
	@echo "make clean - 移除二进制文件和 vim swap files"
	@echo "make gotool - 运行 Go 工具 'fmt' and 'vet'"
