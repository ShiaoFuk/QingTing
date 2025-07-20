package com.example.qingting.net.request.PlayListRequest

import com.example.qingting.net.ResponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class GetAllPlayListRequest : RequestImpl() {
    override fun httpRequest(input: Any?): JsonElement? {
        if (input is String) {
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url("http://suansun.top/playList/get?token=$input")
                .build()
            val response = client.newCall(request).execute()
            return ResponseUtils.doWithResponse(response)
        }
        throw IllegalArgumentException("请检查登陆状态")
        return null
    }
}