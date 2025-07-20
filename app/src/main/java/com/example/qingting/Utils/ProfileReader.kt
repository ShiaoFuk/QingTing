package com.example.qingting.Utils

import android.content.Context
import android.util.Log
import com.king.view.circleprogressview.BuildConfig


class ProfileReader {
    companion object {
        @JvmStatic
        fun getProperty(context: Context, key: String): String? {
            return try {
                com.example.qingting.BuildConfig.jwtKey
            } catch (e: Exception) {
                Log.e("ProfileReader", e.message ?: "")
                null
            }
        }
    }
}