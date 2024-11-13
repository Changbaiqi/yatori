package internal

type LoginInterface interface {
	LoginApi() (string, error)
	VerificationCodeApi() (string, string)
}
