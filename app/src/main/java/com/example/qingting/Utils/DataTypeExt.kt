package com.example.qingting.Utils

import android.content.res.Resources

    fun Int.px() = this.toFloat() * Resources.getSystem().displayMetrics.density

    fun Float.dp() = this * Resources.getSystem().displayMetrics.density
