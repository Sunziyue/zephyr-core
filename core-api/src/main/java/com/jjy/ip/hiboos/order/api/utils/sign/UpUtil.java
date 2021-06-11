package com.jjy.ip.hiboos.order.api.utils.sign;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpUtil {

    /**
     * 初始化向海博系统发送的信息
     * @param requestDto
     * @return
     */
    private static String initParams(RequestDto requestDto, String appId) {
        Map<String, Object> params = new HashMap<>();
        params.put("appId", appId);
        params.put("timestamp", requestDto.getTimestamp());
        params.put("sign", requestDto.getSign());
        params.put("body", requestDto.getBody());
        StringBuilder requestparams = new StringBuilder();
        for (Map.Entry<String, Object> map : params.entrySet()) {
            requestparams.append(map.getKey()).append("=").append(map.getValue()).append("&");
        }
        return requestparams.substring(0, requestparams.length() - 1);
    }

    public static String sendPost(String baseUrl, String url, String appId, String appSecret, Map<String,Object> body) {
        String bodyString = JSONObject.toJSONString(body);
        RequestDto requestDto = new RequestDto();
        long timestamp = System.currentTimeMillis() ;
        String sign = SignUtil.getSign(bodyString, appId, appSecret, timestamp);
        requestDto.setSign(sign);
        bodyString = translation(bodyString);
        requestDto.setBody(bodyString);
        requestDto.setTimestamp(timestamp);

        //发送的参数
        String param = initParams(requestDto, appId);
        String result = HttpRequestUtil.sendPost(baseUrl + url, param);
        result =JSONObject.parseObject(result).toJSONString();
        return result;
    }

    public static String translation(String body){
        body = body.replaceAll("%", "\\%25");
        body = body.replaceAll("\\+", "\\%2B");
        body = body.replaceAll("&", "\\%26");
        body = body.replaceAll("=", "\\%3D");
        return body;
    }

}
