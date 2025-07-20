package com.example.qingting.net.request.recommend

import com.example.qingting.net.ResponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody


abstract class RealTimeRecommendRequest : RequestImpl() {
    override fun httpRequest(input: Any?): JsonElement? {
        val client = OkHttpClient().newBuilder()
            .build()
        val mediaType: MediaType = "application/json".toMediaType()
        val body = RequestBody.create(mediaType, "0")
        val request: Request = Request.Builder()
            .url("http://suansun.top/api/musics")
            .method("POST", body)
            .addHeader("Content-Type", "application/json")
            .build()
        val response = client.newCall(request).execute()
        return ResponseUtils.doWithResponse(response)
    }
}