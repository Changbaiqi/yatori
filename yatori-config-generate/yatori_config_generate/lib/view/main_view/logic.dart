import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'state.dart';

class MainViewLogic extends GetxController {
  final MainViewState state = MainViewState();

  settingPart(){
    return ExpansionTile(
      title: Text("设置列表"),
      leading: Icon(Icons.settings),
      children: [
        ExpansionTile(
          title: Text('基础设置'),
          children: [
            TextField(decoration: InputDecoration(
              hintText: "是否为彩色日志"
            ),), //平台
            TextField(), //账号
            TextField() //密码
          ],
        )
      ],
    );
  }
  userPart() {
    return ExpansionTile(
      title: Text("账号列表"),
      leading: Icon(Icons.people),
      children: [
        ExpansionTile(
          title: Text('用户1'),
          children: [
            TextField(decoration: InputDecoration(
                hintText: "平台类型"
            )), //平台
            TextField(decoration: InputDecoration(
                hintText: "账号"
            )), //账号
            TextField(decoration: InputDecoration(
                hintText: "密码"
            )) //密码
          ],
        )
      ],
    );
  }
}
