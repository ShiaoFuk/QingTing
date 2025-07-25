package com.example.qingting.net.request

import com.example.qingting.bean.User
import com.example.qingting.utils.JsonUtils
import com.example.qingting.net.ResponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

abstract class LoginRequest : RequestImpl() {
    companion object {
        const val TAG = "LoginRequest"
    }

    override fun httpRequest(user: Any?): JsonElement? {
        if (user != null && user is User) {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType = "application/json".toMediaTypeOrNull()
            val body = RequestBody.create(mediaType, JsonUtils.toJson(user))
            val request = Request.Builder()
                .url("http://suansun.top/login")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build()
            val response = client.newCall(request).execute()
            return ResponseUtils.doWithResponse(response)
        }
        return null
    }
}