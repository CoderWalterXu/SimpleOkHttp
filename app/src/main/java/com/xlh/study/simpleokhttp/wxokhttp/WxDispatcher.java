package com.xlh.study.simpleokhttp.wxokhttp;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 调度线程任务
 * version:0.0.1
 */
public class WxDispatcher {

    // 同时访问任务，最大限制64个
    private int maxRequest = 64;

    // 同时访问同一个服务器域名的任务，最大限制5个
    private int maxRequestPerHost = 5;

    // 运行异步请求的队列
    private Deque<WxRealCall.WxAsyncCall> runningAsyncCalls = new ArrayDeque<>();

    // 等待运行异步请求的队列
    private Deque<WxRealCall.WxAsyncCall> readyAsyncCalls = new ArrayDeque<>();


    public void enqueue(WxRealCall.WxAsyncCall wxAsyncCall) {

        if (runningAsyncCalls.size() < maxRequest && runningCallsForHost(wxAsyncCall) < maxRequestPerHost) {
            // 添加到运行异步请求的队列
            runningAsyncCalls.add(wxAsyncCall);
            // 执行异步任务
            executorService().execute(wxAsyncCall);
        } else {
            // 添加到等待运行异步请求的队列
            readyAsyncCalls.add(wxAsyncCall);
        }

    }

    /**
     * 线程池
     *
     * @return
     */
    public ExecutorService executorService() {
        ExecutorService executorService =
                new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                        60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),
                        new ThreadFactory() {
                            @Override
                            public Thread newThread(Runnable r) {
                                Thread thread = new Thread(r);
                                thread.setName("自定义的线程....");
                                thread.setDaemon(false); // 不是守护线程
                                return thread;
                            }
                        });
        return executorService;
    }

    /**
     * 遍历运行异步请求的队列，得到所有请求的同一主机域名的个数
     *
     * @param call
     * @return
     */
    private int runningCallsForHost(WxRealCall.WxAsyncCall call) {
        int count = 0;

        if (runningAsyncCalls.isEmpty()) {
            return 0;
        }

        WxSocketRequestServer wxSocketRequestServer = new WxSocketRequestServer();

        for (WxRealCall.WxAsyncCall runningAsyncCall : runningAsyncCalls) {
            if (wxSocketRequestServer.getHost(runningAsyncCall.getWxRequest()).equals(call.getWxRequest())) {
                count++;
            }

        }
        return count;
    }

    /**
     * 移除运行完成的任务
     * 把等待队列里面所有的任务取出来执行
     *
     * @param call
     */
    public void finished(WxRealCall.WxAsyncCall call) {
        // 移除运行完成的任务
        runningAsyncCalls.remove(call);

        if (readyAsyncCalls.isEmpty()) {
            return;
        }

        for (WxRealCall.WxAsyncCall readyAsyncCall : readyAsyncCalls) {
            // 移除等待队列的任务
            readyAsyncCalls.remove(readyAsyncCall);
            // 把等待队列的任务加入到运行队列
            runningAsyncCalls.add(readyAsyncCall);
            // 开始执行任务
            executorService().execute(readyAsyncCall);
        }

    }

}
