package shindo.Java.util.threeDES;

import java.net.URLDecoder;
import java.net.URLEncoder;

public class Test_ThreeDESECB {
    public static void main(final String[] args) throws Exception {
        /*
         * 深圳通：
         * szt10f84904a44cc8a7f48fc4134e801  --测试
         * 159A8D5663B7F45FC141773F59A77ADA  --生产
         * 备用：
         * cxz10fuiytga44cc8a7f48fc4134op01
         * 城联：
         * clt10f84904a44cc8a7f48fc4134e801  --测试
         * 559A8D5663B7F45FC141773F59A77ACL  --生产
         * 
         * --最新
         * Acl2mhiPCT3sPB5MsRGKawn57xj3TNKg  --test
         * MaigTil28hVETWTssFHGtYDxzeKc2PkT  --product
         * 
         * --澳门通
         * oCNz0o7gi4pDwwMk98vCpsApYBxRb5SC  --测试
         * ACKNzpctQsgPARB09WwzKTQTqgY0karf  --生产
         * 
         * --优惠券
         * W5Bl354oPpdCZSrp6nmPWN2iHpkBaGZU  --测试
         * n7LDq5TMehC3MWZl7ViVXuhVnpZ9Mug0  --生产
         * 
         */
        final String pf_key = "159A8D5663B7F45FC141773F59A77ADA";
        final String szt_key = "szt10f84904a44cc8a7f48fc4134e801";
        final String cl_key = "Acl2mhiPCT3sPB5MsRGKawn57xj3TNKg";
        final String mPlusPay_key = "oCNz0o7gi4pDwwMk98vCpsApYBxRb5SC";
        final String coupon_key = "W5Bl354oPpdCZSrp6nmPWN2iHpkBaGZU";

        final String channel = "2";// 测试渠道选择开关
        String key = "";
        if ("555555".equals(channel)) {
            System.out.println("使用城联key");
            key = cl_key;
        } else if ("666666".equals(channel)) {
            System.out.println("使用小浦key");
            key = pf_key;
        } else if ("777777".equals(channel)) {
            System.out.println("使用深圳通key");
            key = szt_key;
        } else if ("1".equals(channel)) {
            System.out.println("使用澳门通key");
            key = mPlusPay_key;
        } else if ("2".equals(channel)) {
            System.out.println("使用优惠券key");
            key = coupon_key;
        }

        // ===============================================================
        // 加密流程
        final String telePhone = "13750003443";// 18111111111 --13417778473 --18576659012
        final ThreeDESECB threeDES = new ThreeDESECB();
        String telePhone_encrypt = "";
        telePhone_encrypt = threeDES.encryptThreeDESECB(URLEncoder.encode(telePhone, "UTF-8"), key);
        System.out.println(telePhone_encrypt);// nWRVeJuoCrs8a+Ajn/3S8g==
        telePhone_encrypt = URLEncoder.encode(telePhone_encrypt, "UTF-8");
        System.out.println(telePhone_encrypt);// nWRVeJuoCrs8a%2BAjn%2F3S8g%3D%3D

        // 解密流程
        String tele_decrypt = "";
        System.out.println("原始值：" + telePhone_encrypt);
        // 模拟浏览器自动decode
        tele_decrypt = URLDecoder.decode(telePhone_encrypt, "UTF-8");
        System.out.println("模拟浏览器自动decode:" + tele_decrypt);
        // 如果不encode直接decode那么＋号就会变为空
        // tele_decrypt = URLDecoder.decode(tele_decrypt, "UTF-8");
        // System.out.println("不encode直接decode:" + tele_decrypt);
        if (tele_decrypt.contains("+")) {
            tele_decrypt = URLEncoder.encode(tele_decrypt, "UTF-8");
            System.out.println("如果有加号，再次encode:" + tele_decrypt);
        }
        // 模拟代码解密
        // 先decode
        tele_decrypt = URLDecoder.decode(tele_decrypt, "UTF-8");
        System.out.println("解密前的decode：" + tele_decrypt);
        // 解密
        tele_decrypt = threeDES.decryptThreeDESECB(tele_decrypt, key);
        System.out.println("模拟代码解密:" + tele_decrypt);

        // ===============================================================
        final String certNo = "440104197509141121"; // --440104197509141121
        final String certNo_e = threeDES.encryptThreeDESECB(URLEncoder.encode(certNo, "UTF-8"), key);
        System.out.println(URLEncoder.encode(certNo_e, "UTF-8"));
        System.out.println(certNo_e);

        final String certNo_d = threeDES.decryptThreeDESECB(certNo_e, key);
        System.out.println(certNo_d);

        // ===============================================================
//         final String city = "珠海";
//         System.out.println(URLEncoder.encode(URLEncoder.encode(city, "UTF-8"), "UTF-8"));
//         city=%25E4%25B8%258A%25E6%25B5%25B7
        final String userName = "李毅";
        System.out.println("==:" + URLEncoder.encode(URLEncoder.encode(userName, "UTF-8"), "UTF-8"));

    }
}
