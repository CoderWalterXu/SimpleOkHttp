package com.xlh.study.simpleokhttp.wxokhttp;

import java.io.IOException;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 网络请求回调接口
 * version:0.0.1
 */
public interface WxCallback {

    void onFailure(WxCall call, IOException e);

    void onResponse(WxCall call, WxResponse wxResponse) throws IOException;

}
