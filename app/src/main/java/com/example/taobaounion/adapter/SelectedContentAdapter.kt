package com.example.taobaounion.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taobaounion.R
import com.example.taobaounion.data.selected.SelectedContentItem
import com.example.taobaounion.databinding.ListItemSelectedContentBinding
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.uitl.UrlUtils

class SelectedContentAdapter:ListAdapter<SelectedContentItem,SelectedContentAdapter.ViewHolder>(ContentDiff()){

    class ContentDiff:DiffUtil.ItemCallback<SelectedContentItem>(){
        override fun areContentsTheSame(oldItemSelected: SelectedContentItem, newItemSelected: SelectedContentItem): Boolean {
            return oldItemSelected==newItemSelected
        }

        override fun areItemsTheSame(oldItemSelected: SelectedContentItem, newItemSelected: SelectedContentItem): Boolean {
            return oldItemSelected.item_id==newItemSelected.item_id
        }
    }
    class ViewHolder(
      val  binding:ListItemSelectedContentBinding
    ):RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(selectedContentItem: SelectedContentItem){
            binding.content=selectedContentItem
            Glide.with(binding.selectedCover).load(UrlUtils.getIntactUrl(selectedContentItem.pict_url)).into(binding.selectedCover)
            if (selectedContentItem.coupon_click_url.isNullOrEmpty()){
                binding.selectedBuyButton.visibility=View.GONE
                binding.selectedOriginalPrice.text="晚了,没有优惠卷了"
            }else{
                binding.selectedBuyButton.visibility=View.VISIBLE
                binding.selectedBuyButton.text=binding.root.context.getString(R.string.ling_juan)
                binding.selectedOriginalPrice.text="原价:¥${selectedContentItem.zk_final_price}"
            }
            if (selectedContentItem.coupon_info.isEmpty()){
                binding.selectedOffPrice.visibility=View.GONE
            }else{
                binding.selectedOffPrice.visibility=View.VISIBLE
                binding.selectedOffPrice.text=selectedContentItem.coupon_info
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ListItemSelectedContentBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contentItem=getItem(position)
        LogUtil.d("CONTENTITEM",contentItem.toString())
        contentItem?.let {
            holder.bind(contentItem)
        }
    }
}