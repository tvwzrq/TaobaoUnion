package com.example.taobaounion.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taobaounion.R
import com.example.taobaounion.databinding.ListItemFooterBinding
import com.example.taobaounion.uitl.LogUtil

class FooterAdapter(
    private val retry:()->Unit
) :LoadStateAdapter<FooterAdapter.FooterViewHolder>(){

    class FooterViewHolder(
        val binding:ListItemFooterBinding,
      val retry: () -> Unit
    ):RecyclerView.ViewHolder(binding.root){
        init {
            binding.retry.setOnClickListener {
                retry()
            }
        }
        fun bindLoadState(state:LoadState){
            binding.apply {
                when {
                    state is LoadState.Error -> {
                        LogUtil.d("LOADSTATE", "error")
                        msg.text=state.error.localizedMessage
                    }
                    state.endOfPaginationReached -> {
                        //加载完数据
                        msg.text=binding.root.resources.getString(R.string.load_end)
                    }
                    state is LoadState.Loading -> {
                        msg.text=binding.root.resources.getString(R.string.LOADING)
                    }
                }
                msg.isVisible= state is LoadState.Error || state is LoadState.Loading || state.endOfPaginationReached
                loading.isVisible=state is LoadState.Loading
                retry.isVisible=state is LoadState.Error
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterViewHolder {
        return FooterViewHolder(ListItemFooterBinding.inflate(LayoutInflater.from(parent.context),parent,false),
            retry)
    }

    override fun displayLoadStateAsItem(loadState: LoadState): Boolean {
        return loadState is LoadState.Error
                ||loadState is LoadState.Loading
                ||(loadState is LoadState.NotLoading &&loadState.endOfPaginationReached)
    }

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bindLoadState(loadState)
    }
}