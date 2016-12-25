package shindo.Java.util.threeDES;

import java.net.URLDecoder;
import java.security.Key;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Decoder;

public class ThreeDESCBC {
    /**
     * 
     * @Description ECB加密，不要IV
     * @param key 密钥
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     * @author Shindo   
     * @date 2016年11月15日 下午4:42:56
     */
    public static byte[] des3EncodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 
     * @Description ECB解密，不要IV
     * @param key 密钥
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     * @author Shindo   
     * @date 2016年11月15日 下午5:01:23
     */
    public static byte[] ees3DecodeECB(byte[] key, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, deskey);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 
     * @Description CBC加密
     * @param key 密钥
     * @param keyiv IV
     * @param data 明文
     * @return Base64编码的密文
     * @throws Exception
     * @author Shindo   
     * @date 2016年11月15日 下午5:26:46
     */
    public static byte[] des3EncodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 
     * @Description CBC解密
     * @param key 密钥
     * @param keyiv IV
     * @param data Base64编码的密文
     * @return 明文
     * @throws Exception
     * @author Shindo   
     * @date 2016年11月16日 上午10:13:49
     */
    public static byte[] des3DecodeCBC(byte[] key, byte[] keyiv, byte[] data) throws Exception {
        Key deskey = null;
        DESedeKeySpec spec = new DESedeKeySpec(key);
        SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
        deskey = keyfactory.generateSecret(spec);
        Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
        IvParameterSpec ips = new IvParameterSpec(keyiv);
        cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
        byte[] bOut = cipher.doFinal(data);
        return bOut;
    }

    /**
     * 
     * @Description 浦发所属渠道入口3DES解密方法 
     * @param paras 加密参数
     * @param key 3DES密钥
     * @return 解密明文
     * @author Shindo   
     * @throws Exception 
     * @date 2016年11月22日 上午9:34:07
     */
    public Map<String, String> parasDecryptCBC(Map<String, String> paras, String key) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        try {
            byte[] pf_3des_key = new BASE64Decoder().decodeBuffer(key);
            byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };// 3DES解密IV值
            String telePhone = paras.get("telePhone");// 浦发新接口电话不加密

            byte[] card = new BASE64Decoder().decodeBuffer(URLDecoder.decode(paras.get("cardNo"), "UTF-8"));
            byte[] cert = new BASE64Decoder().decodeBuffer(URLDecoder.decode(paras.get("certNo"), "UTF-8"));

            String cardNo = new String(des3DecodeCBC(pf_3des_key, keyiv, card), "UTF-8");// 卡号
            String certNo = new String(des3DecodeCBC(pf_3des_key, keyiv, cert), "UTF-8");// 证件号码
            map.put("telePhone", telePhone);
            map.put("cardNo", cardNo);
            map.put("certNo", certNo);
        } catch (Exception e) {
            throw new Exception(" 浦发所属渠道入口参数3DES CBC解密失败!");
        }
        return map;
    }

    /**
     * 
     * @Description 调试方法
     * @param args
     * @throws Exception
     * @author Shindo   
     * @date 2016年11月22日 上午9:28:22
     */
    public static void main(String[] args) throws Exception {
        byte[] key = new BASE64Decoder().decodeBuffer("YWJjZGVmZ2hpamtsbW5vcHFyc3R1dnd4");
        byte[] keyiv = { 1, 2, 3, 4, 5, 6, 7, 8 };
        byte[] data = "420106198203279258".getBytes("UTF-8");
        /*System.out.println("ECB加密解密");
        byte[] str3 = des3EncodeECB(key, data);
        byte[] str4 = ees3DecodeECB(key, str3);
        System.out.println(new BASE64Encoder().encode(str3));
        System.out.println(new String(str4, "UTF-8"));
        System.out.println();*/

        /*System.out.println("CBC加密解密");
        byte[] str5 = des3EncodeCBC(key, keyiv, data);
        byte[] str6 = des3DecodeCBC(key, keyiv, str5);
        System.out.println(new BASE64Encoder().encode(str5));
        System.out.println(new String(str6, "UTF-8"));*/

        String str7 = "uHrew7Thp2taL2NJpSJhF2mdFMP7BZ1W";
        byte[] str8 = new BASE64Decoder().decodeBuffer(str7);
        byte[] str9 = des3DecodeCBC(key, keyiv, str8);
        System.out.println(new String(str9, "UTF-8"));

    }

}
