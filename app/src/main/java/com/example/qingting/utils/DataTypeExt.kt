package com.example.qingting.utils

import android.content.res.Resources

    fun Int.px() = this.toFloat() * Resources.getSystem().displayMetrics.density

    fun Float.dp() = this * Resources.getSystem().displayMetrics.density
