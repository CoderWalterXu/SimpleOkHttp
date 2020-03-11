package com.xlh.study.simpleokhttp.wxokhttp;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 构建者模式
 * version:0.0.1
 */
public class WxOkHttpClient {

    // 任务调度
    WxDispatcher wxDispatcher;

    // 标识是否取消请求
    boolean isCanceled;

    // 重试次数
    int recount;

    public WxOkHttpClient() {
        this(new Builder());
    }

    public WxOkHttpClient(Builder builder) {
        wxDispatcher = builder.wxDispatcher;
        isCanceled = builder.isCanceled;
        recount = builder.recount;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public int getRecount() {
        return recount;
    }

    public final static class Builder{
        // 任务调度
        WxDispatcher wxDispatcher;

        // 标识是否取消请求
        boolean isCanceled;

        // 重试次数
        int recount = 3;

        public Builder() {
            this.wxDispatcher = new WxDispatcher();
        }

        public Builder wxdispatcher(WxDispatcher wxDispatcher){
            this.wxDispatcher = wxDispatcher;
            return this;
        }

        public Builder canceled(WxDispatcher wxDispatcher){
            this.isCanceled = true;
            return this;
        }

        public Builder recount(int recount){
            this.recount = recount;
            return this;
        }

        public WxOkHttpClient build(){
            return new WxOkHttpClient(this);
        }

    }

    public WxCall newWxCall(WxRequest wxRequest){
        return new WxRealCall(this,wxRequest);
    }

    public WxDispatcher wxDispatcher(){
        return wxDispatcher;
    }

}
