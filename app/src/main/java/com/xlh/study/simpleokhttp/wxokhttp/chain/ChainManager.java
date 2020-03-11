package com.xlh.study.simpleokhttp.wxokhttp.chain;

import com.xlh.study.simpleokhttp.wxokhttp.WxRealCall;
import com.xlh.study.simpleokhttp.wxokhttp.WxRequest;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;

import java.io.IOException;
import java.util.List;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public class ChainManager implements WxChain {

    private List<WxInterceptor> wxInterceptors;
    private int index;
    private WxRequest wxRequest;
    private WxRealCall wxRealCall;

    public List<WxInterceptor> getWxInterceptors() {
        return wxInterceptors;
    }

    public int getIndex() {
        return index;
    }

    public WxRequest getWxRequest() {
        return wxRequest;
    }

    public WxRealCall getWxRealCall() {
        return wxRealCall;
    }

    public ChainManager(List<WxInterceptor> wxInterceptors, int index, WxRequest wxRequest, WxRealCall wxRealCall) {
        this.wxInterceptors = wxInterceptors;
        this.index = index;
        this.wxRequest = wxRequest;
        this.wxRealCall = wxRealCall;
    }

    @Override
    public WxRequest getRequest() {
        return wxRequest;
    }

    @Override
    public WxResponse getResponse(WxRequest wxRequest) throws IOException {

        if(index >= wxInterceptors.size()){
            throw new AssertionError();
        }

        if(wxInterceptors.isEmpty()){
            throw new IOException("wxInterceptors is empty");
        }

        // 根据index值依次取出拦截器
        WxInterceptor wxInterceptor = wxInterceptors.get(index);

        ChainManager chainManager = new ChainManager(wxInterceptors,index+1,wxRequest,wxRealCall);

        WxResponse wxResponse = wxInterceptor.proceed(chainManager);

        return wxResponse;
    }

}
