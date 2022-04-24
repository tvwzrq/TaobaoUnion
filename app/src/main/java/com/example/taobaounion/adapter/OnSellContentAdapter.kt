package com.example.taobaounion.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taobaounion.data.onsell.OnSellContentItem
import com.example.taobaounion.databinding.ListItemOnSellContentBinding
import com.example.taobaounion.uitl.UrlUtils

class OnSellContentAdapter:PagingDataAdapter<OnSellContentItem,OnSellContentAdapter.ViewHolder>(OnSellDiff()) {
    class ViewHolder(
        val binding:ListItemOnSellContentBinding
    ):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(onSellContentItem: OnSellContentItem){
            binding.contentItem=onSellContentItem
            Glide.with(binding.root.context).load(UrlUtils.getIntactUrl(onSellContentItem.pict_url)).into(binding.onSellCover)
            binding.onSellOriginalPrice.text="¥${onSellContentItem.zk_final_price}"
            binding.onSellOffPrice.text="劵后价 ¥${String.format("%.2f",onSellContentItem.zk_final_price.toFloat()-onSellContentItem.coupon_amount)}"
            binding.executePendingBindings()
        }


    }

    class OnSellDiff : DiffUtil.ItemCallback<OnSellContentItem>() {
        override fun areContentsTheSame(
            oldItem: OnSellContentItem,
            newItem: OnSellContentItem
        ): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(
            oldItem: OnSellContentItem,
            newItem: OnSellContentItem
        ): Boolean {
            return oldItem.category_id==newItem.category_id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemOnSellContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contentItem=getItem(position)
        contentItem?.let {
            holder.bind(contentItem)
        }
    }
}

