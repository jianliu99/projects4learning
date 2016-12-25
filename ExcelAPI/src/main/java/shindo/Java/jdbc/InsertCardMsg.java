package shindo.Java.jdbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class InsertCardMsg {
    public static void main(String[] args) throws Exception {
        Connection conn = null;

        // 读取文本信息
        File file = new File("D:\\Test.txt");// Text文件
        BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
        // jdbc:mysql://172.16.0.161/xyj_coupon?useUnicode=true&characterEncoding=UTF-8
        String url = "jdbc:mysql://172.16.1.251/xyj_coupon?user=terry_lau&password=123321&useUnicode=true&characterEncoding=UTF-8";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("成功加载mysql驱动程序");
            conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement();

            String s = null;
            int num = 0;
            while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
                StringBuffer sql = new StringBuffer();
                Map<String, String> map = split(new String(s.getBytes(), "utf-8"));

//                sql = "INSERT INTO `t_card` VALUES ('68010100000000000013', '234567', '11', '1', '31', '1', '0', '2016-12-31 17:35:27', '0', null, null, '0', '2016-12-07 17:35:38', null, '')";
//                sql = "insert into t_card (CARD_NO,VALID_DATE) values('123456','2016-12-09');"
                sql.append("insert into t_card (CARD_NO,VALID_DATE) values('").append(map.get("cardNo")).append("','").append(map.get("validDate"))
                        .append("');");
                String txt = sql.toString();
                txt = txt.replaceAll("/", "-");
                int result = stmt.executeUpdate(sql.toString());
                num = num + result;
                System.out.println(num);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
            conn.close();
        }
    }

    public static Map<String, String> split(String str) {
        Map<String, String> map = new HashMap<String, String>();
        String strr = str.trim();
        String[] str_split = strr.split("[\\p{Space}]+");
        String cardNo = str_split[0];
//        String password = str_split[1];
        String validDate = str_split[1];
        map.put("cardNo", cardNo);
//        map.put("password", password);
        map.put("validDate", validDate);
        return map;
    }
}
