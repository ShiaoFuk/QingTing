package com.example.qingting.mvi.state

data class FragmentDiscoverState(var loadingState: DataLoading = DataLoading.BEFORE_LOADING)

enum class DataLoading {
    BEFORE_LOADING, LOADING, LOADING_SUCCESS, LOADING_FAILED
}
