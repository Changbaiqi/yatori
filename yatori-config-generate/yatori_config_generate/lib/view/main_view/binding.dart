import 'package:get/get.dart';

import 'logic.dart';

class MainViewBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => MainViewLogic());
  }
}
