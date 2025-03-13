package com.example.qingting.net.request.listener

import android.util.Log
import com.example.qingting.exception.UnknownException
import com.google.gson.JsonElement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import java.io.IOException

/**
 * 网络请求顶层接口类，注意默认在子线程跑，UI操作仍需要在主线程
 * 使用request方法发起请求
 */
abstract class RequestImpl: RequestListener {
    companion object {
        const val TAG = "RequestImpl"
        var DEBUG = true  // debug flag, if true will print log
    }


    /**
     * 请求的整体流程
     * @param obj http请求需要的参数，在prepare处传入并预处理，默认为空，若不需要传入参数则不传
     */
    suspend fun request(obj: Any? = null) {
        coroutineScope {
            val processDefer = async {
                // 预处理，可能是耗时的
                val processedObj = onPrepare(obj)
                if (DEBUG) {
                    Log.d(TAG, "ready: on request")
                }
                onRequest()
                if (DEBUG) {
                    Log.d(TAG, "end: on request")
                }
                processedObj
            }
            val processRes = processDefer.await()
            val requestDefer = async(Dispatchers.IO) {
                try {
                    val element: JsonElement = httpRequest(processRes) ?: throw NullPointerException()
                    element
                } catch (e: Exception) {
                    // 异常统一抛出处理
                    e
                }
            }
            when (val requestRes = requestDefer.await()) {
                is JsonElement -> {
                    // 请求成功
                    withContext(Dispatchers.Default) {
                        if (DEBUG) {
                            Log.d(TAG, "ready: on receive")
                        }
                        onReceive()
                        if (DEBUG) {
                            Log.d(TAG, "end: on receive")
                        }
                        try {
                            if (DEBUG) {
                                Log.d(TAG, "ready: on success")
                            }
                            onSuccess(requestRes)
                            if (DEBUG) {
                                Log.d(TAG, "end: on success")
                            }
                        } catch (e: Exception) {
                            if (DEBUG) {
                                Log.d(TAG, "ready: on success error(结果解析失败)")
                            }
                            // 解析失败，抛出错误
                            onError(e)
                            if (DEBUG) {
                                Log.d(TAG, "end: on success error(结果解析失败)")
                            }
                        } finally {
                            if (DEBUG) {
                                Log.d(TAG, "ready: on finish")
                            }
                            onFinish()
                            if (DEBUG) {
                                Log.d(TAG, "end: on finish")
                            }
                        }
                    }
                }

                is Exception -> {
                    if (DEBUG) {
                        Log.d(TAG, "ready: on request error(请求失败)")
                    }
                    // 请求失败，抛出错误
                    onError(requestRes)
                    if (DEBUG) {
                        Log.d(TAG, "end: on request error(请求失败)")
                    }
                    Unit
                }

                else -> {
                    if (DEBUG) {
                        Log.d(TAG, "ready: unknown error")
                    }
                    onError(UnknownException(RequestImpl::class.java))
                    if (DEBUG) {
                        Log.d(TAG, "unknown error")
                    }
                    Unit
                }
            }
        }
    }


    /**
     * 实现http请求的具体内容
     * @param input http请求需要的参数，默认为null，若不需要传入参数则不传
     * @return 返回http请求返回的完整element对象，如果返回的是null则会抛出NullptrException交给onError处理
     */
    @Throws(IOException::class)
    protected abstract fun httpRequest(input: Any?=null): JsonElement?

    /**
     * 预处理请求传入数据阶段，默认不处理
     */
    override fun onPrepare(obj: Any?): Any? {
        return obj
    }

    /**
     * 结束处理，默认空实现
     */
    override fun onFinish() {

    }


    /**
     * 接收到请求结果后
     */
    override fun onReceive() {

    }

    /**
     * 请求请求前，默认空实现
     */
    override fun onRequest() {

    }

}