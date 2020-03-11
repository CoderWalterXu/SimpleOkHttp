package com.xlh.study.simpleokhttp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xlh.study.simpleokhttp.wxokhttp.WxCall;
import com.xlh.study.simpleokhttp.wxokhttp.WxCallback;
import com.xlh.study.simpleokhttp.wxokhttp.WxOkHttpClient;
import com.xlh.study.simpleokhttp.wxokhttp.WxRequest;
import com.xlh.study.simpleokhttp.wxokhttp.WxResponse;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnSendRequest;
    TextView tvContent;

    private static final String PATH = "http://restapi.amap.com/v3/weather/weatherInfo?city=110101&key=13cb58f5884f9749287abbead9c658f2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSendRequest = findViewById(R.id.btn_send_request);
        tvContent = findViewById(R.id.tv_content);

        btnSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest();
            }
        });
    }

    private void sendRequest() {

        WxOkHttpClient wxOkHttpClient = new WxOkHttpClient.Builder().build();

        WxRequest wxRequest = new WxRequest.Builder().get().url(PATH).Build();

        WxCall wxCall = wxOkHttpClient.newWxCall(wxRequest);

        wxCall.enqueue(new WxCallback() {
            @Override
            public void onFailure(WxCall call, IOException e) {
                showUI("WxOkHttp请求失败");
            }

            @Override
            public void onResponse(WxCall call, WxResponse wxResponse) throws IOException {

                showUI("WxOkHttp请求成功\r\n"+
                        "响应码:"+wxResponse.getStatusCode()+"\r\n"+
                        "响应体:"+wxResponse.getBody()
                );

            }
        });

    }

    private void showUI(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvContent.setText(text);
            }
        });
    }
}
