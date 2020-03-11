package com.xlh.study.simpleokhttp.wxokhttp.chain;

import com.xlh.study.simpleokhttp.wxokhttp.WxRequest;
import com.xlh.study.simpleokhttp.wxokhttp.WxRequestBody;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;
import com.xlh.study.simpleokhttp.wxokhttp.WxSocketRequestServer;

import java.io.IOException;
import java.util.Map;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public class RequestHeaderInterceptor implements WxInterceptor {
    @Override
    public WxResponse proceed(WxChain wxChain) throws IOException {

        ChainManager chainManager = (ChainManager) wxChain;
        WxRequest wxRequest = chainManager.getRequest();

        Map<String,String> mHeaderList = wxRequest.getmHeaderList();

        // 请求头添加主机域名
        mHeaderList.put("Host",new WxSocketRequestServer().getHost(chainManager.getWxRequest()));

        // POST请求需要添加请求体
        if("POST".equalsIgnoreCase(wxRequest.getRequestMethod())){
            // 请求体添加Content-Length
            mHeaderList.put("Content-Length",wxRequest.getWxRequestBody().getBody().length()+"");
            // 请求体添加Content-Type
            mHeaderList.put("Content-Type", WxRequestBody.TYPE);
        }

        // 执行下一个拦截器
        return wxChain.getResponse(wxRequest);
    }
}
