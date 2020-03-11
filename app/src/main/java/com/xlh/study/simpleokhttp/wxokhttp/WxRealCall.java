package com.xlh.study.simpleokhttp.wxokhttp;

import com.xlh.study.simpleokhttp.wxokhttp.chain.ChainManager;
import com.xlh.study.simpleokhttp.wxokhttp.chain.ConnectionServerInterceptor;
import com.xlh.study.simpleokhttp.wxokhttp.chain.ReRequestInterceptor;
import com.xlh.study.simpleokhttp.wxokhttp.chain.RequestHeaderInterceptor;
import com.xlh.study.simpleokhttp.wxokhttp.chain.WxInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description:
 * version:0.0.1
 */
public class WxRealCall implements WxCall{

    private WxOkHttpClient wxOkHttpClient;

    private WxRequest wxRequest;

    // 标识任务是否执行
    private boolean executed;

    public WxOkHttpClient getWxOkHttpClient() {
        return wxOkHttpClient;
    }

    public WxRequest getWxRequest() {
        return wxRequest;
    }

    public boolean isExecuted(){
        return executed;
    }

    public WxRealCall(WxOkHttpClient wxOkHttpClient, WxRequest wxRequest) {
        this.wxOkHttpClient = wxOkHttpClient;
        this.wxRequest = wxRequest;
    }

    @Override
    public void enqueue(WxCallback reponseCallback) {
        // 同步锁，判断一个任务是否重复执行
        synchronized (this){
            if(executed){
                executed = true;
                throw new IllegalStateException("enqueue Already Executed");
            }
        }

        wxOkHttpClient.wxDispatcher.enqueue(new WxAsyncCall(reponseCallback));
    }

    final class WxAsyncCall implements Runnable {

        public WxRequest getWxRequest(){
            return WxRealCall.this.wxRequest;
        }

        private WxCallback wxCallback;

        public WxAsyncCall(WxCallback wxCallback) {
            this.wxCallback = wxCallback;
        }

        @Override
        public void run() {
            boolean signalledCallback = false;

            try {
                // 通过责任链获取返回结果
                WxResponse wxResponse = getResponseWithInterceptorChain();

                if(wxOkHttpClient.isCanceled){
                    signalledCallback = true;
                    wxCallback.onFailure(WxRealCall.this,new IOException("user Canceled"));
                }else {
                    signalledCallback = true;
                    wxCallback.onResponse(WxRealCall.this,wxResponse);
                }

            } catch (IOException e) {
                if (signalledCallback) {
                    // 如果等于true，回调给用户了，是用户操作的时候 报错
                    System.out.println("user error");
                } else {
                    wxCallback.onFailure(WxRealCall.this,new IOException("WxOkHttp getResponseWithInterceptorChain error "+e.toString()));
                }
            } finally {
                // 回收处理
                wxOkHttpClient.wxDispatcher().finished(this);
            }
        }
    }

    private WxResponse getResponseWithInterceptorChain() throws IOException{

        List<WxInterceptor> wxInterceptorList = new ArrayList<>();
        // 添加重试拦截器
        wxInterceptorList.add(new ReRequestInterceptor());
        // 添加请求头拦截器
        wxInterceptorList.add(new RequestHeaderInterceptor());
        // 连接服务器拦截器
        wxInterceptorList.add(new ConnectionServerInterceptor());

        ChainManager chainManager = new ChainManager(wxInterceptorList,0,wxRequest,WxRealCall.this);

        return chainManager.getResponse(wxRequest);
    }

}
