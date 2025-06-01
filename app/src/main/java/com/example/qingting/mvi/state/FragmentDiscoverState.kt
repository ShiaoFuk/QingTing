package com.example.qingting.mvi.state

import com.example.qingting.Bean.Album

data class FragmentDiscoverState(
    var loadingState: DataLoading = DataLoading.BEFORE_LOADING,
    var albumListList: List<List<Album>> = ArrayList()
    )

enum class DataLoading {
    BEFORE_LOADING, LOADING, LOADING_SUCCESS, LOADING_FAILED

}
