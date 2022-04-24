package com.example.taobaounion.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.taobaounion.MainApplication
import com.example.taobaounion.R
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.databinding.ListItemFirstBinding
import com.example.taobaounion.uitl.BANNER_SIZE
import com.example.taobaounion.views.NestedConstrainLayout


class HomeFirstAdapter :RecyclerView.Adapter <HomeFirstAdapter.ViewHolder>(){
    private lateinit var viewHolder:ViewHolder
    private var bannerList:List<HomePageContentItem> = ArrayList()
    @SuppressLint("NotifyDataSetChanged")
    fun setBannerList(list:List<HomePageContentItem>){
        this.bannerList=list
    }

    fun autoScroll(){
        if (this::viewHolder.isInitialized){
            viewHolder.binding.banner.apply {
                if (!(parent as NestedConstrainLayout).isTouched){
                    currentItem += 1
                }
            }
        }
    }

    class ViewHolder(
      val  binding:ListItemFirstBinding,
    ):RecyclerView.ViewHolder(binding.root){
       private  var bannerAdapter: HomeBannerAdapter
        init {
            initIndicator()
            bannerAdapter= HomeBannerAdapter()
            binding.banner.registerOnPageChangeCallback(object :ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    val relativePosition=position % BANNER_SIZE
                    updateIndicator(relativePosition)
                    super.onPageSelected(position)
                }
            })
        }
        fun bindBanner(list: List<HomePageContentItem>){
            bannerAdapter.setBannerList(list)
            binding.banner.adapter=bannerAdapter
            val position=Int.MAX_VALUE/2
            val targetPosition=position-position % BANNER_SIZE
            binding.banner.currentItem=targetPosition
        }
        private fun initIndicator(){
            for (index in 1.. BANNER_SIZE){
                val imageView= ImageView(MainApplication.context)
                val layoutParams= LinearLayout.LayoutParams(30,30)
                layoutParams.marginEnd=10
                layoutParams.marginStart=10
                layoutParams.bottomMargin=10
                imageView.layoutParams=layoutParams
                binding.indicator.addView(imageView)
            }
        }
        private fun updateIndicator(position:Int){
            for (index in 1..BANNER_SIZE){
                if (index==(position+1)){
                    Log.d("INDICATOR",position.toString())
                    binding.indicator.getChildAt(index-1).setBackgroundResource(R.drawable.shape_indicator_selected)
                }else{
                    binding.indicator.getChildAt(index-1).setBackgroundResource(R.drawable.shape_indicator_normal)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       viewHolder= ViewHolder(ListItemFirstBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        return viewHolder
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindBanner(bannerList)
    }
    fun refresh(){
        viewHolder.bindBanner(bannerList)
    }

}