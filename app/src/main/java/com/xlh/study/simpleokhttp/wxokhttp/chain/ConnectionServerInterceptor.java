package com.xlh.study.simpleokhttp.wxokhttp.chain;

import com.xlh.study.simpleokhttp.wxokhttp.WxRequest;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;
import com.xlh.study.simpleokhttp.wxokhttp.WxSocketRequestServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * @author: Watler Xu
 * time:2020/3/11
 * description: 连接服务器的拦截器
 * version:0.0.1
 */
public class ConnectionServerInterceptor implements WxInterceptor {

    @Override
    public WxResponse proceed(WxChain wxChain) throws IOException {

        WxSocketRequestServer srs = new WxSocketRequestServer();

        WxRequest wxRequest = wxChain.getRequest();

        Socket socket = new Socket(srs.getHost(wxRequest),srs.getPort(wxRequest));

        OutputStream os = socket.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os));
        String requestAll = srs.getRequestHeaderAll(wxRequest);

        // 给服务器发送请求
        bufferedWriter.write(requestAll);
        bufferedWriter.flush();

        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        WxResponse wxResponse = new WxResponse();

        // 读取响应头信息的第一行----HTTP/1.1 200 OK
        String readLine = bufferedReader.readLine();
        // 通过空格分割
        String[] strings = readLine.split(" ");
        // 设置响应码
        wxResponse.setStatusCode(Integer.parseInt(strings[1]));


        String readerLine = null;

        try {
            while((readerLine = bufferedReader.readLine())!=null){
                if ("".equals(readerLine)){
                    // 设置响应体
                    wxResponse.setBody(bufferedReader.readLine());
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("我是wxResponse:"+wxResponse);

        return wxResponse;
    }
}
