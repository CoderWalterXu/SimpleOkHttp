package com.xlh.study.simpleokhttp.wxokhttp.chain;

import com.xlh.study.simpleokhttp.wxokhttp.WxOkHttpClient;
import com.xlh.study.simpleokhttp.wxokhttp.WxRealCall;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;

import java.io.IOException;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public class ReRequestInterceptor implements WxInterceptor {


    @Override
    public WxResponse proceed(WxChain wxChain) throws IOException {

        ChainManager chainManager = (ChainManager) wxChain;

        WxRealCall wxRealCall = chainManager.getWxRealCall();
        WxOkHttpClient wxOkHttpClient = wxRealCall.getWxOkHttpClient();

        IOException ioException = null;

        if (wxOkHttpClient.getRecount() != 0) {
            for (int i = 0; i < wxOkHttpClient.getRecount(); i++) {
                try {
                    WxResponse response = wxChain.getResponse(chainManager.getWxRequest());
                    return response;
                } catch (IOException e) {
                    e.printStackTrace();
                    ioException = e;
                }
            }
        }
        throw ioException;
    }
}
