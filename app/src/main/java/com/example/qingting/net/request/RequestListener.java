package com.example.qingting.net.request;

import com.google.gson.JsonElement;

public interface RequestListener {
    // 定义发送请求的状态：
    // 发送前、发送后，接收后(成功，失败)，请求结束
    Object onPrepare(Object object);  // 发送前，返回处理后的数据
    void onRequest();  // 发送前后
    void onReceive();  // 接收响应后 trigger onSuccess | onError
    void onSuccess(JsonElement element);  // 响应成功
    void onError(Exception e);  // 响应失败
    void onFinish();  // 结束


}
