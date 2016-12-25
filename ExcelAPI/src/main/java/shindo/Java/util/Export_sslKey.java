package shindo.Java.util;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;

import sun.misc.BASE64Encoder;

/**
 * 详细使用见博文：http://www.cnblogs.com/shindo/p/6117647.html
 * @ClassName Export_sslKey
 * @Description TODO 针对自签名的cer证书，导出私钥key
 * @author shindo.yang
 * @Date 2016年12月2日 下午2:50:08
 * @version 1.0.0
 */
public class Export_sslKey {

    public static KeyStore getKeyStore(String keyStorePath, String password) throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(is, password.toCharArray());
        is.close();
        return ks;
    }

    public static PrivateKey getPrivateKey() {
        try {
            BASE64Encoder encoder = new BASE64Encoder();
            KeyStore ks = getKeyStore("D:/Macaumarket/key/test.keystore", "macaumarket");
            PrivateKey key = (PrivateKey) ks.getKey("test", "macaumarket".toCharArray());
            String encoded = encoder.encode(key.getEncoded());
            System.out.println("-----BEGIN RSA PRIVATE KEY-----");
            System.out.println(encoded);
            System.out.println("-----END RSA PRIVATE KEY-----");
            return key;
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) {
        getPrivateKey();
    }

}
