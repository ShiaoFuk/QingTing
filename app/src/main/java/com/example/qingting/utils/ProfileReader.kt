package com.example.qingting.utils

import android.content.Context
import android.util.Log


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