package com.xlh.study.simpleokhttp.wxokhttp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 请求头信息,构建者模式
 * version:0.0.1
 */
public class WxRequest {

    public static final String GET = "GET";

    public static final String POST = "POST";

    // 请求url
    private String url;

    // 请求方式，默认GET
    private String requestMethod = GET;

    // 请求头属性集合
    private Map<String, String> mHeaderList = new HashMap<>();

    // 请求体
    private WxRequestBody wxRequestBody;

    public String getUrl() {
        return url;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public Map<String, String> getmHeaderList() {
        return mHeaderList;
    }

    public WxRequestBody getWxRequestBody() {
        return wxRequestBody;
    }

    public WxRequest() {
        this(new Builder());
    }

    public WxRequest(Builder builder) {
        this.url = builder.url;
        this.requestMethod = builder.requestMethod;
        this.mHeaderList = builder.mHeaderList;
        this.wxRequestBody = builder.wxRequestBody;
    }

    public final static class Builder {
        // 请求url
        private String url;

        // 请求方式，默认GET
        private String requestMethod = GET;

        // 请求头属性集合
        private Map<String, String> mHeaderList = new HashMap<>();

        // 请求体
        private WxRequestBody wxRequestBody;

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        /**
         * 设置请求方式为GET
         *
         * @return
         */
        public Builder get() {
            requestMethod = GET;
            return this;
        }

        /**
         * 设置请求方式为POST
         *
         * @param wxRequestBody
         * @return
         */
        public Builder post(WxRequestBody wxRequestBody) {
            requestMethod = POST;
            this.wxRequestBody = wxRequestBody;
            return this;
        }

        /**
         * 添加请求头信息
         *
         * @param key
         * @param value
         * @return
         */
        public Builder addRequestHeader(String key, String value) {
            mHeaderList.put(key, value);
            return this;
        }

        public WxRequest Build(){
            return new WxRequest(this);
        }

    }

}
