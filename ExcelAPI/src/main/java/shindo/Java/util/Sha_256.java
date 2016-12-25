package shindo.Java.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author shindo.yang
 */
public class Sha_256 {

    public static final String ALGORITHM = "SHA-256";

    /**
     * 对加签内容使用sha256算法计算签值
     * 
     * @param signContent
     *            需要计算签值的字符串
     * @return String 签值
     */
    public String SHA256Encrypt(final String signContent) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (null != md) {
            final byte[] origBytes = signContent.getBytes();
            md.update(origBytes);
            final byte[] digestRes = md.digest();
            final String digestStr = getDigestStr(digestRes);
            return digestStr;
        }

        return null;
    }

    private static String getDigestStr(final byte[] origBytes) {
        String tempStr = null;
        final StringBuilder stb = new StringBuilder();
        for (int i = 0; i < origBytes.length; i++) {
            /*
             * 这里按位与是为了把字节转整时候取其正确的整数，java中一个int是4个字节 如果origBytes[i]最高位为1，则转为int时，int的前三个字节都被1填充了
             */
            tempStr = Integer.toHexString(origBytes[i] & 0xff);
            if (tempStr.length() == 1) {
                stb.append("0");
            }
            stb.append(tempStr);

        }
        return stb.toString();
    }
}
