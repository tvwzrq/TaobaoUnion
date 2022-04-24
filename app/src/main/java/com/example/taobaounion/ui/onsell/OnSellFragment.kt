package com.example.taobaounion.ui.onsell

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.taobaounion.adapter.FooterAdapter
import com.example.taobaounion.adapter.OnSellContentAdapter
import com.example.taobaounion.databinding.FragmentOnSellBinding
import com.example.taobaounion.uitl.LOADING
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.viewmodel.onsell.OnSellViewModel
import com.example.taobaounion.views.STATE_ERROR
import com.example.taobaounion.views.STATE_LOADING
import com.example.taobaounion.views.STATE_SUCCESS
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OnSellFragment:Fragment() {
    private lateinit var binding:FragmentOnSellBinding
    private lateinit var adapter:OnSellContentAdapter
    private val viewModel by viewModels<OnSellViewModel>()
    private var job:Job?=null
    private lateinit var footAdapter:FooterAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentOnSellBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }

    private fun initUi() {

        adapter= OnSellContentAdapter()
        footAdapter= FooterAdapter { adapter.retry() }
        binding.loadStateView.setOnClickListener {
            getOnSellContent()
            binding.loadStateView.setLoadState(STATE_LOADING)
        }

        adapter.addLoadStateListener {
            when(it.refresh){
                is LoadState.Loading-> {
                    binding.loadStateView.setLoadState(STATE_LOADING)
                    LogUtil.d("STATE","---->loading")
                }
                is LoadState.NotLoading-> binding.loadStateView.setLoadState(STATE_SUCCESS)
                is LoadState.Error-> binding.loadStateView.setLoadState(STATE_ERROR)
            }
        }
        getOnSellContent()
        val layoutManager=GridLayoutManager(this.context,2)
        layoutManager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                return if (position==adapter.itemCount&& footAdapter.itemCount>0) 2 else 1
            }
        }
        binding.onSellContent.layoutManager=layoutManager
        binding.onSellContent.adapter=adapter.withLoadStateFooter(footAdapter)
    }

    private fun getOnSellContent(){
        job?.cancel()
        job=lifecycleScope.launch {
            viewModel.getOnSellContent().collect {
                adapter.submitData(it)
            }
        }
    }
}