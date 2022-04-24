package com.example.taobaounion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.databinding.ListItemHomeContentBinding
import com.example.taobaounion.uitl.BANNER_SIZE
import com.example.taobaounion.uitl.CategoryItemCallback

class CategoryItemAdapter(val callback:CategoryItemCallback):PagingDataAdapter<HomePageContentItem,CategoryItemAdapter.ViewHolder>(CategoryItemDiffCallback()) {
//只回调一次
    private var FLAG=true
    class ViewHolder(
        val binding:ListItemHomeContentBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(homePageContentItem: HomePageContentItem){
            binding.categoryItem=homePageContentItem
            binding.executePendingBindings()
        }
    }
   class CategoryItemDiffCallback:DiffUtil.ItemCallback<HomePageContentItem>(){
        override fun areContentsTheSame(oldItem: HomePageContentItem, newItem: HomePageContentItem): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(oldItem: HomePageContentItem, newItem: HomePageContentItem): Boolean {
            return oldItem.category_id==newItem.category_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ListItemHomeContentBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryItem=getItem(position)
        categoryItem?.let {
            holder.bind(it)
        }
        val items=this.snapshot().items
        if (items.size>= BANNER_SIZE&&FLAG){
            callback.onSuccess(items)
            FLAG=false
        }
    }


}