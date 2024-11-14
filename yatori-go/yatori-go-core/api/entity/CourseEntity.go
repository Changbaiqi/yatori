package entity

// XueXiTCourse 课程所有信息
type XueXiTCourse struct {
	Result           int           `json:"result"`
	Msg              string        `json:"msg"`
	ChannelList      []ChannelItem `json:"channelList"`
	Mcode            string        `json:"mcode"`
	Createcourse     int           `json:"createcoursed"`
	TeacherEndCourse int           `json:"teacherEndCourse"`
	ShowEndCourse    int           `json:"showEndCourse"`
	HasMore          bool          `json:"hasMore"`
	StuEndCourse     int           `json:"stuEndCourse"`
}

// ChannelItem 课程列表
type ChannelItem struct {
	Cfid     int    `json:"cfid"`
	Norder   int    `json:"norder"`
	CataName string `json:"cataName"`
	Cataid   string `json:"cataid"`
	Id       int    `json:"id"`
	Cpi      int64  `json:"cpi"`
	Key      any    `json:"key"`
	Content  struct {
		Studentcount int    `json:"studentcount"`
		Chatid       string `json:"chatid"`
		IsFiled      int    `json:"isFiled"`
		Isthirdaq    int    `json:"isthirdaq"`
		Isstart      bool   `json:"isstart"`
		Isretire     int    `json:"isretire"`
		Name         string `json:"name"`
		Course       struct {
			Data []struct {
				BelongSchoolId     string `json:"belongSchoolId"`
				Coursestate        int    `json:"coursestate"`
				Teacherfactor      string `json:"teacherfactor"`
				IsCourseSquare     int    `json:"isCourseSquare"`
				Schools            string `json:"schools"`
				CourseSquareUrl    string `json:"courseSquareUrl"`
				Imageurl           string `json:"imageurl"`
				AppInfo            string `json:"appInfo"`
				Name               string `json:"name"`
				DefaultShowCatalog int    `json:"defaultShowCatalog"`
				Id                 int    `json:"id"`
				AppData            int    `json:"appData"`
			} `json:"data"`
		} `json:"course"`
		Roletype int    `json:"roletype"`
		Id       int    `json:"id"`
		State    int    `json:"state"`
		Cpi      int    `json:"cpi"`
		Bbsid    string `json:"bbsid"`
		IsSquare int    `json:"isSquare"`
	} `json:"content"`
	Topsign int `json:"topsign"`
}
