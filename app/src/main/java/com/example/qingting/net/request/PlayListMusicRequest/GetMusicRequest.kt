package com.example.qingting.net.request.PlayListMusicRequest

import com.example.qingting.net.ReponseUtils
import com.example.qingting.net.request.listener.RequestImpl
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.Request

abstract class GetMusicRequest : RequestImpl() {

    /**
     * 传入数据，string,int
     */
    override fun httpRequest(input: Any?): JsonElement? {
        if (input is Array<*> && input.size == 2 && input[0] is String && input[1] is Int) {
            val token = input[0] as String
            val playListId = input[1] as Int
            val client = OkHttpClient().newBuilder()
                .build()
            val request: Request = Request.Builder()
                .url(
                    String.format(
                        "http://suansun.top/playList/song/get?token=%s&playListId=%s",
                        token,
                        playListId.toString()
                    )
                )
                .build()
            val response = client.newCall(request).execute()
            return ReponseUtils.doWithResponse(response)
        }
        return null
    }
}