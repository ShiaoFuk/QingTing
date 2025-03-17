package com.example.qingting.mvi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qingting.mvi.intent.FragmentDiscoverIntent
import com.example.qingting.mvi.state.DataLoading
import com.example.qingting.mvi.state.FragmentDiscoverState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentDiscoverViewModel: ViewModel() {

    companion object {
        const val TAG = "FragmentDiscoverViewModel"
    }


//    private lateinit var binding: FragmentDiscoverBinding

    private val _state = MutableStateFlow(FragmentDiscoverState())
    val fragmentState = _state.asStateFlow()  // 对外暴露的状态


    fun processIntent(intent: FragmentDiscoverIntent) {
        viewModelScope.launch {
            when (intent) {
                is FragmentDiscoverIntent.startLoading -> {
                    _state.value = _state.value.copy(loadingState = DataLoading.LOADING)
                    Log.d(TAG, "开始加载数据")
                }
                is FragmentDiscoverIntent.loadingSuccess -> {
                    _state.value = _state.value.copy(loadingState = DataLoading.LOADING_SUCCESS)
                    Log.d(TAG, "加载数据成功")
                }
                is FragmentDiscoverIntent.loadingFailed -> {
                    _state.value = _state.value.copy(loadingState = DataLoading.LOADING_FAILED)
                    Log.d(TAG, "加载数据失败")
                }
            }
        }
    }
}