package enaea

import (
	"fmt"
	"yatori-go-core/api/enaea"
)

// EnaeaLoginAction 学习公社登录
func EnaeaLoginAction(cache *enaea.EnaeaUserCache) error {
	api, err := enaea.LoginApi(cache)
	if err != nil {
		return err
	}
	fmt.Println(api)

	return nil
}
