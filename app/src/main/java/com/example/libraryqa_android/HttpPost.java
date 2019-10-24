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

public class HttpPost {
    String gAsw;
    String img;
    String url2;

    public void hPost(String ask, final Info info, String url) {
        //传递json格式
        url2=url;
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        String requestBody = "{\"question\": \" " + ask + "\"}";
        Request request = new Request.Builder()
                .url("http://" + url + ":8888/?target=graph_qa")
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
                Gson gson = new Gson();
                System.out.println("12333333333123");
                String str=response.body().string();
                Info result = gson.fromJson(str, Info.class);
                if(result.getFirst().equals("repeat"))
                {
                    info.setfirstNull();
                    HttpPost httpPost = new HttpPost();
                    //向服务器发送post请求
                    httpPost.hPost(result.getSecond(), info, url2);
                }
                info.setFirst(result.getFirst());
                info.setSecond(result.getSecond());
                info.setSearch_answer(result.getSearch_answer());
                //Info result = gson.fromJson(response.body().string(), Info.class);
                //gAsw = result.graph_answer;
                //img = result.img;
                //info.setGraph_answer(gAsw);
                //info.setSearch_answer(result.getSearch_answer());
                //info.setBitmap(result.getBitmap());
                //info.setRepeat(result.getRepeat());
                //System.out.println("hello world");
                //System.out.println(response.body().string());
            }
        });
    }
}
