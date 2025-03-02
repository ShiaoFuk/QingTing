package com.example.qingting.net.request.PlayListMusicRequest

import com.example.qingting.Bean.Music
import com.example.qingting.net.ReponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

abstract class DeleteMusicRequest: RequestImpl() {

    override fun httpRequest(input: Any?): JsonElement? {
        if (input is Array<*> && input.size == 3 && input[0] is String && input[1] is Int && input[2] is List<*>) {
            val token = input[0] as String
            val playListId = input[1] as Int
            val musicList = input[2] as List<*>
            if (musicList.isEmpty()) {
                throw IllegalArgumentException("传入音乐列表长度需要大于0")
            }
            val sb = StringBuilder()
            musicList.apply {
                for (s in this.subList(0, this.size - 1)) {
                    sb.append(s).append(",")
                }
                sb.append(this.last())
            }
            val client = OkHttpClient().newBuilder()
                .build()
            val url = "http://suansun.top/playList/song/deleteMany".toHttpUrl()
                .newBuilder()
                .addQueryParameter("token", token) // 添加查询参数 q
                .addQueryParameter("playListId", playListId.toString()) // 添加查询参数 limit
                .addQueryParameter("musicList", sb.toString())
                .build()
            val mediaType: MediaType = "text/plain".toMediaType()
            val body = RequestBody.create(mediaType, "")
            val request: Request = Request.Builder()
                .url(url)
                .method("DELETE", body)
                .build()
            val response = client.newCall(request).execute()
            return ReponseUtils.doWithResponse(response)


        }
        return null
    }
}