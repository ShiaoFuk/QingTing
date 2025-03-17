package com.example.qingting.mvi.intent

sealed class FragmentDiscoverIntent {
    object startLoading: FragmentDiscoverIntent()
    object loadingSuccess: FragmentDiscoverIntent()
    object loadingFailed: FragmentDiscoverIntent()
}


