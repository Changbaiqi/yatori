<div align="center"><h1>Yatori课程助手</h1></div>

---

<div align="center"><img width="200" src="https://q1.qlogo.cn/g?b=qq&nk=2084069833&s=640"></img></div>

<div align="center"><h2>Yatori</h2></div>

## 问题咨询

> QQ交流群：932447008

## 当前支持平台：

> - [x] 英华学堂
> - [ ] 仓辉实训（即将上线）

## 食用方式：

### 代码食用：

> 施工中...

### 直接食用:

> 下载releases然后解压修改config配置文件之后点击start.bat启动即可。
>
> 注意：填url的时候是填写学校英华的根链接，不能带uri，
>
> 比如[https://mooc.xxx.edu.com/](https://mooc.xxx.edu.com/)，而不是[https://mooc.xxx.edu.cn/xx/xx](https://mooc.xxx.edu.cn/xx/xx)
>
> 以及不能用[https://mooc.yinghuaonline.com/](https://mooc.yinghuaonline.com/)，要用自己学校的链接，比如[https://mooc.xxx.cn/](https://mooc.xxx.cn/)，每个学校的链接都不同，这个可以自己去找去问。
>
> 配置文件说明：
>
> ```json
> {
> "users": [
>  {
>    "accountType": "YINGHUA",//指定平台，"YINGHUA"代表英华学堂，CANGHUI代表仓辉平台
>    "url": "url",//平台主页的根url，不同学校url不同，比如https://mooc.xxx.cn/，注意千万别带uri指别写成https://mooc.xxx.cn/xxx/xxx这样。
>    "account": "账号",//账号
>    "password": "",//密码
>    "excludeCourses": ["课程1", "课程2"],//这个参数代表需要排除不刷的课程，复制课程的名称填入即可（一字不差）
>    "includeCourses": ["课程3", "课程4"]//这个指的是需要刷的课程，如果不填默认刷全部课程除非设置了排除课程
>  }
> ]
> }
> ```
>
> 刷课支持多账号，根据需求自行进行改动。
>
> 示例1：
>
> ```json
> {
> "users": [
> 	{
> 		"accountType": "YINGHUA",
> 		"url": "张三的url",
> 		"account": "张三的账号",
> 		"password": "张三的密码"
> 	},
> 	{
> 		"accountType": "YINGHUA",
> 		"url": "李四的url",
> 		"account": "李四的账号",
> 		"password": "李四的密码"
> 	}
> ]
> }
> ```
>

## 免责声明：

> 代码已开源，程序只供学习使用，严禁贩卖，若对贵公司造成损失立马删库（保命(doge)）。

