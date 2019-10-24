package com.example.libraryqa_android;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpPost2 {
    String age;
    String sex;
    String img;

    public void hPost(String age,String sex,String img, String url) {
        //传递json格式
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"age\":\""+age+"\",\"sex\":\""+sex+"\",\"img\":\""+img+"\"}";
        Request request = new Request.Builder()
                .url("http://" + url + ":8888/?target=recognition")
                .post(RequestBody.create(mediaType, requestBody))
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        //回调
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println("hello world");
            }
        });
    }
}
