package shindo.Java;

import java.util.Date;

public class TestSystemIn {
    public static void main(String[] args) {
        // Scanner 的输入是可见的，所以不适用于录入密码
        /*Scanner in = new Scanner(System.in);
        System.out.println("who are you?");
        String name = in.nextLine();
        System.out.println("I'm " + name);*/

        // 格式化输出日期
        System.out.printf("%tF", new Date());

        // 获取当前的路径
        String dir = System.getProperty("user.dir");
        System.out.println(dir);
    }
}
