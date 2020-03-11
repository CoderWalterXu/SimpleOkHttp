package com.xlh.study.simpleokhttp.wxokhttp.chain;

import com.xlh.study.simpleokhttp.wxokhttp.WxRequest;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;

import java.io.IOException;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public interface WxInterceptor {

    WxResponse proceed(WxChain wxChain) throws IOException;

}
