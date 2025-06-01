package com.example.qingting.HomePage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.qingting.Adapter.AlbumAdapter
import com.example.qingting.Bean.Album
import com.example.qingting.R
import com.example.qingting.databinding.FragmentDiscoverBinding
import com.example.qingting.mvi.intent.FragmentDiscoverIntent
import com.example.qingting.mvi.state.DataLoading
import com.example.qingting.mvi.state.FragmentDiscoverState
import com.example.qingting.mvi.viewmodel.FragmentDiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.random.Random

class DiscoverFragment private constructor() : Fragment() {
    private lateinit var binding: FragmentDiscoverBinding
    private lateinit var viewModel: FragmentDiscoverViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiscoverBinding.inflate(inflater, null, false)
        viewModel = ViewModelProvider(this).get(FragmentDiscoverViewModel::class.java)
        initRecyclerView()
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                // 发起网络请求
                Log.d(TAG, "开始发起网络请求")
                delay(2000)
                Log.d(TAG, "网络请求成功")
                val virtualAlbumListListData: ArrayList<ArrayList<Album>> = ArrayList()
                for (i in 0 until 3) {
                    val virtualAlbumListData = ArrayList<Album>()
                    for (j in 0 until 10) {
                        virtualAlbumListData.add(Album(ArrayList()).apply { this.id = Random.nextInt() })
                    }
                    virtualAlbumListListData.add(virtualAlbumListData)
                }
                viewModel.processIntent(FragmentDiscoverIntent.loadingSuccess(virtualAlbumListListData))
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.processIntent(FragmentDiscoverIntent.startLoading)
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