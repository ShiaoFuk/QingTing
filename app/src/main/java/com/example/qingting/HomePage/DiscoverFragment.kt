package com.example.qingting.HomePage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.qingting.R
import com.example.qingting.databinding.FragmentDiscoverBinding
import com.example.qingting.mvi.intent.FragmentDiscoverIntent
import com.example.qingting.mvi.state.DataLoading
import com.example.qingting.mvi.state.FragmentDiscoverState
import com.example.qingting.mvi.viewmodel.FragmentDiscoverViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.processIntent(FragmentDiscoverIntent.startLoading)
                viewModel.fragmentState.collectLatest { state -> render(state) }
                launch {
                    delay(2000)
                    viewModel.processIntent(FragmentDiscoverIntent.loadingSuccess)
                }

            }
        }
        return binding.getRoot()
    }

    fun render(state: FragmentDiscoverState) {
        binding.apply {
            this.bgLoadingTip.visibility = when (state.loadingState) {
                DataLoading.BEFORE_LOADING -> View.VISIBLE
                DataLoading.LOADING -> View.VISIBLE
                DataLoading.LOADING_SUCCESS -> View.GONE
                DataLoading.LOADING_FAILED -> View.GONE
            }
            this.bgLoadingTip.text = when (state.loadingState) {
                DataLoading.LOADING_FAILED -> getString(R.string.discover_page_loading_tip_fail)
                else -> getString(R.string.discover_page_loading_tip_loading)
            }
        }
    }

    companion object {
        var fragment: DiscoverFragment? = null
        val instance: DiscoverFragment?
            get() {
                if (fragment == null) fragment = DiscoverFragment()
                return fragment
            }
    }
}