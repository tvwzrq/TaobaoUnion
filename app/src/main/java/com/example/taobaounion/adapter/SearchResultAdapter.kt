package com.example.taobaounion.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taobaounion.data.search.SearchContentItem
import com.example.taobaounion.databinding.ListItemSearchContentBinding


class SearchResultAdapter: ListAdapter<SearchContentItem,SearchResultAdapter.SearchDiff.ViewHolder>(SearchDiff()) {
    class SearchDiff:DiffUtil.ItemCallback<SearchContentItem>() {
        override fun areContentsTheSame(
            oldItem: SearchContentItem,
            newItem: SearchContentItem
        ): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(
            oldItem: SearchContentItem,
            newItem: SearchContentItem
        ): Boolean {
            return oldItem.category_id==newItem.category_id
        }

        class ViewHolder(
            val binding:ListItemSearchContentBinding
        ):RecyclerView.ViewHolder(binding.root){
            @SuppressLint("SetTextI18n")
            fun bind(searchContentItem: SearchContentItem){
                binding.contentItem=searchContentItem
                val resultPrice=String.format("%.2f",searchContentItem.zk_final_price.toFloat()-searchContentItem.coupon_amount)
                binding.goodsPrice.text="劵后价: ¥$resultPrice"
                binding.executePendingBindings()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDiff.ViewHolder {
        return SearchDiff.ViewHolder(ListItemSearchContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: SearchDiff.ViewHolder, position: Int) {
        val item=getItem(position)
        item?.let {
            holder.bind(item)
        }
    }
}