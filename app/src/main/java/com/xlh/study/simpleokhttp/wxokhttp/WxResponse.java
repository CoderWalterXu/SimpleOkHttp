package com.xlh.study.simpleokhttp.wxokhttp;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 从服务器返回的结果信息
 * version:0.0.1
 */
public class WxResponse {

    private int statusCode;

    private String body;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "WxResponse{" +
                "statusCode=" + statusCode +
                ", body='" + body + '\'' +
                '}';
    }
}
