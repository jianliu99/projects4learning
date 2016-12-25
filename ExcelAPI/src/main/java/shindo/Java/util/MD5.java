package shindo.Java.util;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import shindo.Java.map.Map_sortByKey;

public class MD5 {
    /**
     * MD5 加密
     */
    public String getMD5(final byte[] source) {
        String s = null;
        final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };// 用来将字节转换成16进制表示的字符
        try {
            final java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            md.update(source);
            final byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            final char str[] = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            // 进制需要 32 个字符
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
                // 进制字符的转换
                final byte byte0 = tmp[i];// 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

            }
            s = new String(str);// 换后的结果转换为字符串

        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    public boolean signValid(Map<String, String> paras, Map<String, String> paras_decrypt, Map<Object, Object> sess) throws Exception {
        boolean flag = false;
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put("cardNo", paras_decrypt.get("cardNo"));
            map.put("certNo", paras_decrypt.get("certNo"));
            map.put("channel", paras.get("channel"));
            map.put("channelType", paras.get("channelType"));
            map.put("openID", paras.get("openID"));
            map.put("telePhone", paras_decrypt.get("telePhone"));
            map.put("userid", paras.get("userid"));

            List<Map.Entry<String, String>> infos = new Map_sortByKey().sortMap(map);
            StringBuffer orgin = new StringBuffer();
            for (final Map.Entry<String, String> m : infos) {
                orgin.append(m.getValue());
            }
            // 加签字段
            final String signStr = orgin.toString() + "&" + sess.get("inMd5Key").toString();
            // 算摘要
            String signature = getMD5(signStr.getBytes("utf-8"));
            if (paras.get("sign").equals(signature.toUpperCase())) {
                flag = true;
            }
        } catch (Exception e) {
            throw new Exception("浦发所属新渠道入口Md5验签失败！");
        }
        return flag;
    }

    public static void main(String[] args) throws Exception {
        String orgin = "6222770001007744420106198203279258000000000000002139782451352383694000000005561866";
        String signStr = orgin + "&123456";
        String sign = new MD5().getMD5(signStr.getBytes("utf-8"));
        System.out.println(sign);
    }
}
