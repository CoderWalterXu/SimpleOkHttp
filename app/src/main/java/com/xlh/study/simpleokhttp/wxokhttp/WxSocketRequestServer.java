package com.xlh.study.simpleokhttp.wxokhttp;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public class WxSocketRequestServer {

    private final String BLANK = " ";
    // http协议版本
    private final String VERSION = "HTTP/1.1";
    private final String GRGN = "\r\n";

    /**
     * 获取主机域名
     *
     * @param wxRequest
     * @return
     */
    public String getHost(WxRequest wxRequest) {
        try {
            URL url = new URL(wxRequest.getUrl());
            return url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取端口
     *
     * @param wxRequest
     * @return
     */
    public int getPort(WxRequest wxRequest) {
        try {
            URL url = new URL(wxRequest.getUrl());
            int port = url.getPort();
            return port == -1 ? url.getDefaultPort() : port;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取请求头所有信息
     *
     * @param wxRequest
     * @return
     */
    public String getRequestHeaderAll(WxRequest wxRequest) {
        URL url = null;
        try {
            url = new URL(wxRequest.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        String file = url.getFile();

        StringBuffer stringBuffer = new StringBuffer();
        // 拼接请求头
        stringBuffer.append(wxRequest.getRequestMethod())
                .append(BLANK)
                .append(file)
                .append(BLANK)
                .append(VERSION)
                .append(GRGN);

        // 请求头集合拼接
        if (!wxRequest.getmHeaderList().isEmpty()) {
            Map<String, String> mapList = wxRequest.getmHeaderList();
            for (Map.Entry<String, String> entry : mapList.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append(":")
                        .append(BLANK)
                        .append(entry.getValue())
                        .append(GRGN);
            }
            stringBuffer.append(GRGN);
        }

        // POST请求才拼接请求体
        if("POST".equalsIgnoreCase(wxRequest.getRequestMethod())){
            stringBuffer.append(wxRequest.getWxRequestBody().getBody()).append(GRGN);
        }

        return stringBuffer.toString();
    }

}
