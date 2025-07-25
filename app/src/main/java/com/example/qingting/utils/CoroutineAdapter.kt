package com.example.qingting.utils

import kotlinx.coroutines.runBlocking

abstract class CoroutineAdapter {
    fun runInBlocking() {
        runBlocking {
            runInCoroutineScope()
        }
    }

    abstract suspend fun runInCoroutineScope()

}