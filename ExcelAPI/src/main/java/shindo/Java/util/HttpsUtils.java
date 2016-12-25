package shindo.Java.util;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * 
 * @author shiwei
 * 
 */
public class HttpsUtils {
    public static void main(String[] args) {
        String url = "https://www.macaumarket.com/";
        sendPayMsg(url, null);
    }

    public static String sendPayMsg(String url, Map<Object, Object> map) {
        String cotent = "";
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            X509TrustManager tm = new X509TrustManager() {
                // @Override
                public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                // @Override
                public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
                }

                // @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpClient base = new DefaultHttpClient();
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            HttpContext context = new BasicHttpContext();
            context.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

            HttpPost post = new HttpPost(url);
            // post.addHeader("Content-Type", "text/html;charset=UTF-8");
            if (null != map && !map.isEmpty()) {
                List<NameValuePair> formparams = new ArrayList<NameValuePair>();
                Set<Object> keySet = map.keySet();
                for (Object key : keySet) {
                    formparams.add(new BasicNameValuePair(key.toString(), map.get(key).toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
                post.setEntity(entity);
            }

            HttpResponse response = base.execute(post, context);
            HttpEntity entity = response.getEntity();
            if (entity == null) {
                System.out.println("Empty");
            }

            try {
//              payState = String.valueOf(response.getStatusLine().getStatusCode());
                cotent = EntityUtils.toString(entity);
                long len = entity.getContentLength();
                if (len < 10 * 1024 * 1024) {
                    System.out.println(cotent);
                } else {
                    System.out.println("Empty");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            cotent = url;

            System.out.println(post.getParams().getParameter("payResult"));
            ;

            return cotent;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return cotent;
    }
}
