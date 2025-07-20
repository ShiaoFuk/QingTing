package com.example.qingting.net.request.PlayListRequest

import com.example.qingting.net.ResponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

abstract class UpdatePlayListRequest : RequestImpl() {
    override fun httpRequest(input: Any?): JsonElement? {
        if (input is Array<*> && input.size == 3 && input[0] is String && input[1] is Int && input[2] is String) {
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType = "text/plain".toMediaType()
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://suansun.top/playList/update?token=${input[0]}&playListId=${input[1]}&name=${input[2]}")
                .method("PUT", body)
                .build()
            val response = client.newCall(request).execute()
            return ResponseUtils.doWithResponse(response)
        }
        return null
    }
}