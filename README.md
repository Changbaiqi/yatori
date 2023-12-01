<div align="center"><h1>Yatori课程助手</h1></div>

---
<div align="center"><img width="100px" src="https://img.shields.io/badge/Java17-passing-r.svg"></img> <img width="125px" src="https://img.shields.io/badge/Maven3.8.1-building-r.svg"></img></div>

<div align="center"><img width="200" src="https://q1.qlogo.cn/g?b=qq&nk=2084069833&s=640"></img></div>

<div align="center"><h2>Yatori</h2></div>

## 问题咨询

> QQ交流群：932447008
>
> B站：[BiliBili for 长白崎](https://space.bilibili.com/36987520)（不定时更新计算机相关技术教程）
>
> 个人博客：[长白崎的个人博客 (changbaiqi.github.io)](https://changbaiqi.github.io/)
>
> 技术打赏：[技术打赏 | 长白崎的个人博客 (changbaiqi.github.io)](https://changbaiqi.github.io/post/ji-zhu-da-shang/)

## 功能支持及特性：

> - [x] 独立程序，不依赖浏览器
> - [x] AI自动识别跳过验证码
> - [x] 多账号同刷
> - [x] 支持自动考试（目前只支持仓辉。别问，问就是只有人提供了仓辉的账号我才能开发，没人提供其他平台账号测试我也没办法）
> - [x] 灵活配置文件
> - [x] 自动继续上次记录时长刷课
> - [x] 可部署服务器
> - [x] 部分平台支持暴力模式（无视前置课程学习限制，一门课所有视屏同刷！！！）

## 支持平台：

> - [x] 英华学堂（不支持暴力模式）
> - [x] 创能平台（不支持暴力模式）
> - [x] 仓辉实训（支持暴力模式，支持自动考试）
> - [ ] 智慧树（暂不支持，除非有人提供账号支持开发测试）
> - [ ] 盗梦空间抢活动（估计要等比较久的时间再整合了）
> - [ ] 学习通（暂不支持，除非有人提供账号支持开发测试）
> - [ ] 学习公社（暂不支持，除非有人提供账号支持开发测试）

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
>     "users": [
>         {
>             "accountType": "CANGHUI",//指定平台，"YINGHUA"代表英华学堂（创能平台也使用这个），CANGHUI代表仓辉平台
>             "url": "url",//平台主页的根url，不同学校url不同，比如https://mooc.xxx.cn/，注意千万别带uri指别写成https://mooc.xxx.cn/xxx/xxx这样。
>             "account": "账号",//账号
>             "password": "密码",//密码
>             "coursesCostom": {
>                 "videoModel": 1,//模式设定，0代表不刷视屏，1为普通模式，2为暴力模式，默认为1，暴力模式目前只支持仓辉
>                 "autoExam": 1,//是否自动考试，0代表不考，1代表考，默认为0，注意，目前自动考试只支持仓辉！！！
>                 "excludeCourses": ["课程1", "课程2"],//这个参数代表需要排除不刷的课程，复制课程的名称填入即可（一字不差）
>                 "includeCourses": ["课程3", "课程4"],//这个指的是需要刷的课程，如果不填默认刷全部课程除非设置了排除课程
>                 "coursesSettings": [
>                     {
>                         "name": "大学生劳动教育", //对应需要单独需要客制化的课程名称
>                         "includeExams": ["试卷名称1","试卷名称2"],//对应课程需要考试的试卷名称
>                         "excludeExams": ["试卷名称3","试卷名称4"],//对应课程不需要考试的试卷名称
>                     }
>                 ]
>             }
>         }
>     ]
> }
> ```
>
> 刷课支持多账号，根据需求自行进行改动。
>
> 示例1：
>
> ```json
> {
>     "users": [
>         {
>             "accountType": "YINGHUA",
>             "url": "张三的url",
>             "account": "张三的账号",
>             "password": "张三的密码"
>         },
>         {
>             "accountType": "CANGHUI",
>             "url": "李四的url",
>             "account": "李四的账号",
>             "password": "李四的密码",
>             "coursesCostom": {
>                 "videoModel": 2,
>                 "autoExam": 1
>             }
>         }
>     ]
> }
> ```
>

## 免责声明：

> 代码已开源，程序只供学习使用，严禁贩卖，若对贵公司造成损失立马删库（保命(doge)）。

