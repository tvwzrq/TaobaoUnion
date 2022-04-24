package com.example.taobaounion.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.databinding.ListItemBannerBinding
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.uitl.UrlUtils

class HomeBannerAdapter:RecyclerView.Adapter<HomeBannerAdapter.BannerViewHolder> (){
    private var homePageContentItemList:List<HomePageContentItem> =ArrayList()
    class BannerViewHolder(
        val binding:ListItemBannerBinding
    ):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        return BannerViewHolder(ListItemBannerBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        if (homePageContentItemList.isNotEmpty()){
            val relativePosition=position % homePageContentItemList.size
            val categoryItem=homePageContentItemList[relativePosition]
            holder.binding.categoryItem=categoryItem
            Glide.with(holder.binding.bannerPage).load(UrlUtils.getIntactUrl(categoryItem.pict_url)).into(holder.binding.bannerPage)
        }
    }

    override fun getItemCount(): Int {
        return Int.MAX_VALUE
    }

    fun setBannerList(list: List<HomePageContentItem>){
        this.homePageContentItemList=list
        LogUtil.d("CATEGORYCHANGE",homePageContentItemList.size.toString())
        this.notifyDataSetChanged()
    }


}