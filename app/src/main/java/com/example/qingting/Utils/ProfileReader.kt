package com.example.qingting.Utils

import android.content.Context
import java.io.InputStream
import java.util.Properties


class ProfileReader {
    companion object {
        @JvmStatic
        fun getProperty(context: Context, key: String): String? {
            return try {
                val inputStream: InputStream = context.assets.open("local.properties")
                val properties = Properties()
                properties.load(inputStream)
                inputStream.close()
                properties.getProperty(key)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}