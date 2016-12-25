package shindo.Java.util;

import java.security.MessageDigest;
import java.util.Arrays;

public class Sha_1 {
    public static byte[] IV = new byte[8];

    public static String encode(final String str, final String sign) {
        if (str == null) {
            return null;
        }
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] result = messageDigest.digest();
            result = Arrays.copyOf(result, 20);
            final byte[] signs = Arrays.copyOf(sign.getBytes(), 20);
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < result.length; i++) {
                final int temp = result[i] & 0xFF ^ signs[i] & 0xFF;
                if ((temp <= 15) || (temp < 15)) {
                    sb.append(0);
                }
                sb.append(Integer.toHexString(temp));
            }
            return sb.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static String bytes2Hex(final byte[] bts) {
        final StringBuffer sb = new StringBuffer();
        String tmp = null;
        for (int i = 0; i < bts.length; i++) {
            tmp = Integer.toHexString(bts[i] & 0xFF);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    public static void main(final String[] args) {
        final String s = "{\"mobile\":\"18576659012\",\"token\":\"b46c158b-a70a-49bc-b405-6c07784e8ae61451359141974\",\"type\":\"2\"}";
    }
}
