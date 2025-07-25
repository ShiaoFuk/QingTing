package com.example.qingting.mvi.intent

import com.example.qingting.bean.Album

sealed class FragmentDiscoverIntent {
    object startLoading: FragmentDiscoverIntent()
    data class loadingSuccess(val albumList: List<List<Album>>) : FragmentDiscoverIntent()
    object loadingFailed: FragmentDiscoverIntent()
}


