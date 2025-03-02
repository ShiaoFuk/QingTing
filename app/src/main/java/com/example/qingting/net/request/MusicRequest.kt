package com.example.qingting.net.request

import com.example.qingting.net.ReponseUtils
import com.example.qingting.net.ServerConf
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * 获取音乐
 */
abstract class MusicRequest: RequestImpl() {
    override fun httpRequest(input: Any?): JsonElement? {
        if (input != null && input is String) {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType? = "text/plain".toMediaTypeOrNull()
            // 使用 HttpUrl.Builder 来构建带有查询参数的 URL
            val url = (ServerConf.ADDRESS + "/music").toHttpUrl()
                .newBuilder()
                .addQueryParameter("name", input) // 添加第一个参数
                .build()
            val request: Request = Request.Builder()
                .url(url)
                .method("GET", null)
                .build()
            val response = client.newCall(request).execute()
            return ReponseUtils.doWithResponse(response)
        }
        return null
    }
}