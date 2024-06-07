package com.cbq.yatori.console.display;

public class RichConsole {
    static private StringBuffer stringBuffer = new StringBuffer();


    /**
     * 添加控件
     * @param widget
     */
    public void addWidge(Widget widget){
        stringBuffer.append(widget.txt());
    }
    public void display(){
        clear();
        System.out.print(stringBuffer); //通知
    }

    /**
     * 清除控制台显示
     */
    private void clear(){
        try {
            String operatingSystem = System.getProperty("os.name"); // Check the current operating system

            if (operatingSystem.contains("Windows")) {
                ProcessBuilder pb = new ProcessBuilder("cmd", "/c", "cls");
                pb.inheritIO().start().waitFor();
            }
            else {
                ProcessBuilder pb = new ProcessBuilder("clear");
                Process startProcess = pb.inheritIO().start();
                startProcess.waitFor();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
