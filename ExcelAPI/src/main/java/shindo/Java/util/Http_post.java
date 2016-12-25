package shindo.Java.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

/**
 * 深圳通登录校验接口调试
 * 
 * @author shindo.yang
 *
 */
public class Http_post {

    public static final String SIGN = "7e848c5062578918";
    public static final String ACCESSCODE = "Macaumarket";
    public static final String URL = "http://61.144.244.6:8087/third-web/enbrands/valiLogin";

    public static void main(final String[] args) {
        final String param = assembly();
        System.out.println(param);
        sendHttpsRequest(URL, param);

    }

    public static String assembly() {
        final String sign = SIGN;
        final String accessCode = ACCESSCODE;

        final JSONObject js = new JSONObject();
        final Map<String, String> mes = new HashMap<String, String>();
        mes.put("token", "ddaef26f-130c-4483-b0f4-69c257e7284d1472459463379");
        mes.put("mobileNo", "18576659012");

        final JSONObject messa = js.fromObject(mes);
        final String param = GsonUtils.getParentSendJson(messa.toString(), sign, accessCode);
        return param;
    }

    public static String sendHttpsRequest(final String url, final String json) {
        String result = "";
        // String mobile = "3_"+openId;
        final DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            final HttpPost httppost = new HttpPost(url);
            httppost.setHeader("Content-Type", "application/json; charset=utf-8");
            httppost.setHeader("Accept", "application/json");
            // httppost.setHeader("mobile",mobile);
            System.out.println("httppost" + httppost);

            final StringEntity strentity = new StringEntity(json, "UTF-8");

            httppost.setEntity(strentity);

            final long lo = System.currentTimeMillis();
            httpclient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);// 超时时间
            final HttpResponse response = httpclient.execute(httppost);
            System.out.println("time:" + (System.currentTimeMillis() - lo) + "ms");

            result = EntityUtils.toString(response.getEntity());
            // 成功：{"success":null,"code":null,"message":null,"data":null,"status":"1","token":null}
            // 失败：{"success":null,"code":null,"message":"token
            // 无效,请登录","data":null,"status":"0","token":null}
            final JSONObject fromObject = JSONObject.fromObject(result);
            final String status = (String) fromObject.get("status");
            System.out.println(status);

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {

            httpclient.getConnectionManager().shutdown();

        }
        return result;
    }

}
