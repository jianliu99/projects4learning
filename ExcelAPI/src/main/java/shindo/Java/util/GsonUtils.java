package shindo.Java.util;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class GsonUtils {
    public static String getSendJson(final String json, final String sign) {
        final Gson gson = new Gson();
        final String mac = Sha_1.encode(json, sign);
        final Map result = gson.fromJson(json, Map.class);
        result.put("mac", mac);
        return gson.toJson(result);
    }

    public static String getParentSendJson(final String json, final String sign, final String accessCode) {
        final Gson gson = new Gson();
        final String mac = Sha_1.encode(json, sign);
        final Map result = gson.fromJson(json, Map.class);
        final Map retrunMap = new HashMap();
        retrunMap.put("message", result);
        retrunMap.put("mac", mac);
        retrunMap.put("accessCode", accessCode);
        return gson.toJson(retrunMap);
    }
}