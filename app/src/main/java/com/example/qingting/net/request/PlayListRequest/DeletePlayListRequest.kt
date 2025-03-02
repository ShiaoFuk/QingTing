package com.example.qingting.net.request.PlayListRequest

import com.example.qingting.net.ReponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

abstract class DeletePlayListRequest: RequestImpl() {
    override fun httpRequest(input: Any?): JsonElement? {
        if (input is Array<*> && input.size == 2 && input[0] is String && input[1] is Int) {
            val token = input[0] as String
            val playListId = input[1] as Int
            val client = OkHttpClient().newBuilder()
                .build()
            val mediaType: MediaType = "text/plain".toMediaType()
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url("http://suansun.top/playList/delete?token=$token&playListId=$playListId")
                .method("DELETE", body)
                .build()
            val response = client.newCall(request).execute()
            return ReponseUtils.doWithResponse(response)
        }
        return null
    }
}