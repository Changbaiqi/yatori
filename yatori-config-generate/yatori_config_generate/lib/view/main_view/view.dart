import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'logic.dart';
import 'state.dart';

class MainViewPage extends StatelessWidget {
  MainViewPage({Key? key}) : super(key: key);

  final MainViewLogic logic = Get.put(MainViewLogic());
  final MainViewState state = Get.find<MainViewLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        child: Column(
          children: [
            logic.settingPart(),
            logic.userPart()
          ],
        ),
      ),
    );
  }

}
