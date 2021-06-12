package xyz.szy.zephyr.core.api.utils.sign;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.Iterator;
import java.util.TreeMap;

@Component
public class SignUtil {

    /**
     * 项目接口上行获取签名
     * @param body 请求业务参数JSON
     * @param appId 海博系统分配的应用标识
     * @param timestamp 时间戳
     * @return
     */
    public static String getSign(String body, String appId, String appSecret, long timestamp){
        TreeMap<String, Object> arr = new TreeMap<String, Object>();
        arr.put("appId", appId);
        arr.put("body", body);
        arr.put("timestamp", timestamp);
        StringBuilder strSignTmp = new StringBuilder("");
        Iterator it = arr.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            strSignTmp.append(key).append("=").append(arr.get(key)).append("&");
        }
        String strSign = StringUtils.substring(strSignTmp.toString(), 0, strSignTmp.toString().length()-1);
        strSign = appSecret + strSign + appSecret;
        String sign = DigestUtils.md5DigestAsHex(strSign.getBytes()).toUpperCase();
        return sign;
    }

    /**
     * 项目接口下行校验签名
     * @param body 请求业务参数JSON
     * @param appId 海博系统分配的应用标识
     * @param timestamp 时间戳
     * @param sign 签名值
     * @return
     */
    public static boolean checkSign(String body, String appId, String appSecret, long timestamp, String sign) {
        TreeMap<String, Object> arr = new TreeMap<String, Object>();
        arr.put("body", body);
        arr.put("appId", appId);
        arr.put("timestamp", timestamp);
        StringBuilder strSignTmp = new StringBuilder("");
        Iterator it = arr.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            strSignTmp.append(key).append("=").append(arr.get(key)).append("&");
        }
        String strSign = StringUtils.substring(strSignTmp.toString(), 0, strSignTmp.toString().length()-1);
        strSign = appSecret + strSign + appSecret;
        return DigestUtils.md5DigestAsHex(strSign.getBytes()).toUpperCase().equals(sign);
    }

}
