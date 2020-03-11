package com.xlh.study.simpleokhttp.wxokhttp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 请求体对象
 * version:0.0.1
 */
public class WxRequestBody {

    // 请求体表单提交
    public static final String TYPE = "application/x-www-form-urlencoded";

    // 编码格式
    private final String ENC = "utf-8";

    // 请求体属性集合
    Map<String, String> bodys = new HashMap<>();

    /**
     * 添加请求体信息
     *
     * @param key
     * @param value
     */
    public void addBody(String key, String value) {
        try {
            // URL编码
            bodys.put(URLEncoder.encode(key, ENC), URLEncoder.encode(value, ENC));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到请求体信息
     *
     * @return
     */
    public String getBody() {
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry<String, String> stringStringEntry : bodys.entrySet()) {
            // 拼接参数
            stringBuffer.append(stringStringEntry.getKey())
                    .append("=")
                    .append(stringStringEntry.getValue())
                    .append("&");
        }

        if (stringBuffer.length() != 0) {
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }


}
