package com.example.qingting.Utils

import android.os.Handler
import android.os.Looper

class PostHelper {
    private object PostHandler {
        val mainThreadHandler = Handler(Looper.getMainLooper())
    }

    companion object {
        @JvmStatic
        fun runOnMainThread(task: Runnable) {
            PostHandler.mainThreadHandler.post(task)
        }
    }
}