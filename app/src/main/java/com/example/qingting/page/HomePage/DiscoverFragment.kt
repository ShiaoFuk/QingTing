package com.example.qingting.page.HomePage

import android.accounts.NetworkErrorException
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.qingting.adapter.AlbumAdapter
import com.example.qingting.bean.Album
import com.example.qingting.bean.Music
import com.example.qingting.R
import com.example.qingting.utils.JsonUtils
import com.example.qingting.databinding.FragmentDiscoverBinding
import com.example.qingting.mvi.intent.FragmentDiscoverIntent
import com.example.qingting.mvi.state.DataLoading
import com.example.qingting.mvi.state.FragmentDiscoverState
import com.example.qingting.mvi.viewmodel.FragmentDiscoverViewModel
import com.example.qingting.net.request.recommend.DailyRecommendRequest
import com.example.qingting.net.request.recommend.RealTimeRecommendRequest
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DiscoverFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var viewModel: FragmentDiscoverViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val lock = Any()

    private fun dealWithAlbumElement(
        element: JsonElement?,
        albumList: ArrayList<Album>
    ) {
        if (element == null) throw Exception("element is null")
        val data = element.asJsonObject
        if (data != null) {
            val musicArray = data.get("recommendMusics")
            val playListId = data.get("playlistId").asInt
            val musicListType =
                object : TypeToken<List<Music?>?>() {}.type
            val musicList: List<Music> = JsonUtils.getJsonParser()
                .fromJson(musicArray, musicListType)
            synchronized(lock) {
                albumCount++
                albumList.add(Album(musicList, playListId))
            }
        } else {
            throw NetworkErrorException("获取歌单失败")
        }
    }

    var albumCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, null, false)
        viewModel = ViewModelProvider(this).get(FragmentDiscoverViewModel::class.java)
        initRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.processIntent(FragmentDiscoverIntent.startLoading)
                // 发起网络请求
                Log.d(TAG, "开始发起网络请求")
                val virtualAlbumListListData = ArrayList<ArrayList<Album>>()
                albumCount = 0
                for (i in 0 until 3) {
                    val virtualAlbumListData = ArrayList<Album>()
                    for (j in 0 until 5) {
                        withContext(Dispatchers.IO) {
                            if (i == 0 && j == 0) {
                                object : DailyRecommendRequest() {
                                    override fun onSuccess(element: JsonElement?) {
                                        dealWithAlbumElement(
                                            element,
                                            virtualAlbumListData
                                        )
                                    }

                                    override fun onError(e: Exception?) {
                                        Log.e(TAG, "获取日推歌单失败: ${e?.message}")
                                    }
                                }.request()
                            } else {
                                object : RealTimeRecommendRequest() {
                                    override fun onSuccess(element: JsonElement?) {
                                        dealWithAlbumElement(
                                            element,
                                            virtualAlbumListData
                                        )
                                    }

                                    override fun onError(e: Exception?) {
                                        Log.e(TAG, "获取日推歌单失败: ${e?.message}")
                                    }
                                }.request()
                            }
                        }
                    }
                    synchronized(lock) {
                        virtualAlbumListListData.add(virtualAlbumListData)
                    }
                }
                Log.d(TAG, "网络请求成功")
                viewModel.processIntent(
                    if (albumCount != 0) {
                        FragmentDiscoverIntent.loadingSuccess(
                            virtualAlbumListListData
                        )
                    } else {
                        FragmentDiscoverIntent.loadingFailed
                    }
                )
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.fragmentState.collectLatest { state -> render(state) }
            }
        }
        initTestView()
        return binding.getRoot()
    }

    private fun render(state: FragmentDiscoverState) {
        Log.d(TAG, "当前的歌单加载状态: ${state.loadingState}")
        binding.apply {
            this.bgLoadingTip.visibility = when (state.loadingState) {
                DataLoading.BEFORE_LOADING -> View.VISIBLE
                DataLoading.LOADING -> View.VISIBLE
                DataLoading.LOADING_SUCCESS -> View.GONE
                DataLoading.LOADING_FAILED -> View.VISIBLE
            }
            this.bgLoadingTip.text = when (state.loadingState) {
                DataLoading.LOADING_FAILED -> getString(R.string.discover_page_loading_tip_fail)
                else -> getString(R.string.discover_page_loading_tip_loading)
            }
            if (state.loadingState == DataLoading.LOADING_SUCCESS) {
                this.albumRecyclerview.visibility = View.VISIBLE
                this.albumRecyclerview.adapter.apply {
                    if (this is AlbumAdapter) {
                        this.updateData(state.albumListList)
                    }
                }
            } else {
                this.albumRecyclerview.visibility = View.GONE
            }
        }
    }

    private fun initRecyclerView() {
        binding.apply {
            this.albumRecyclerview.layoutManager = LinearLayoutManager(context)
            this.albumRecyclerview.adapter = AlbumAdapter(ArrayList())
        }
    }

    private fun initTestView() {
//        binding.testBtnLoadFail.setOnClickListener {
//            viewModel.processIntent(FragmentDiscoverIntent.loadingFailed)
//        }
//        binding.testBtnLoadSuccess.setOnClickListener {
//            viewModel.processIntent(FragmentDiscoverIntent.loadingSuccess)
//        }
//        binding.testBtnStartLoading.setOnClickListener {
//            viewModel.processIntent(FragmentDiscoverIntent.startLoading)
//        }
    }


    companion object {
        const val TAG: String = "DiscoverFragment"
        var fragment: DiscoverFragment? = null
        val instance: DiscoverFragment?
            get() {
                if (fragment == null) fragment = DiscoverFragment()
                return fragment
            }
    }
}