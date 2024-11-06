package entity

type UserEntity struct {
	PreUrl   string //前置url
	Account  string //账号
	Password string //用户密码
	VerCode  string //验证码
	Cookie   string //验证码用的session
	Token    string //保持会话的Token
}
