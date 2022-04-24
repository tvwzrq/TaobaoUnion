package com.example.taobaounion.adapter

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.taobaounion.R
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.data.onsell.OnSellContentItem
import com.example.taobaounion.data.search.SearchContentItem
import com.example.taobaounion.data.selected.SelectedContentItem
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.uitl.UrlUtils
import com.example.taobaounion.views.FlowTextLayout
import com.google.android.material.imageview.ShapeableImageView

@BindingAdapter("loadImageFromUrl")
fun loadImageFromUrl(view:ShapeableImageView,url:String?){
    //传入的url缺少协议
    try {
        //拼接图片大小
        val imageUrl = UrlUtils.getIntactUrl(url)+"_200x200.jpg"
        Glide.with(view.context).load(imageUrl).into(view)
    }catch (e:Exception){
        e.printStackTrace()
        LogUtil.d("IMAGEERROR",url.toString())
    }
}

@SuppressLint("SetTextI18n")
@BindingAdapter("goods_off_price_text")
fun setGoodsOffPrice(view: TextView,offPrice:Int){
    view.text="省${offPrice}元"
}



@SuppressLint("SetTextI18n")
@BindingAdapter("goods_price_text")
fun setGoodsPriceText(view: TextView, homePageContentItem: HomePageContentItem){
    val resultPrice=String.format("%.2f",homePageContentItem.zk_final_price.toFloat()-homePageContentItem.coupon_amount)
    view.text="劵后价: ¥$resultPrice"
}

@BindingAdapter("ticket_link")
fun setTicketLink(view:View,categoryItem: Any?){
    categoryItem?.let {item->
        when(item){
            is HomePageContentItem->{
                view.setOnClickListener {
                    //放入请求参数
                    val bundle=Bundle().apply {
                        if (item.coupon_click_url.isNotEmpty()){
                            putString("url",UrlUtils.getIntactUrl(item.coupon_click_url))
                        }else{
                            putString("url",UrlUtils.getIntactUrl(item.click_url))
                        }
                        putString("title",item.title)
                        putString("cover",UrlUtils.getIntactUrl(item.pict_url))
                    }
                    view.findNavController().navigate(R.id.ticketFragment,bundle)
                }
            }

            is SelectedContentItem->{
                view.setOnClickListener {
                    //放入请求参数
                    val bundle=Bundle().apply {
                        if (item.coupon_click_url.isNullOrEmpty()){
                            putString("url",UrlUtils.getIntactUrl(item.coupon_click_url))
                        }else{
                            putString("url",UrlUtils.getIntactUrl(item.click_url))
                        }
                        putString("title",item.title)
                        putString("cover",UrlUtils.getIntactUrl(item.pict_url))
                    }
                    view.findNavController().navigate(R.id.ticketFragment,bundle)
                }
            }
            is OnSellContentItem->{
                view.setOnClickListener {
                    //放入请求参数
                    val bundle=Bundle().apply {
                        if (item.coupon_click_url.isEmpty()){
                            putString("url",UrlUtils.getIntactUrl(item.coupon_click_url))
                        }else{
                            putString("url",UrlUtils.getIntactUrl(item.click_url))
                        }
                        putString("title",item.title)
                        putString("cover",UrlUtils.getIntactUrl(item.pict_url))
                    }
                    view.findNavController().navigate(R.id.ticketFragment,bundle)
                }
            }
           is SearchContentItem->{
                view.setOnClickListener {
                    //放入请求参数
                    val bundle=Bundle().apply {
                        if (item.coupon_share_url.isNullOrEmpty()){
                            putString("url",UrlUtils.getIntactUrl(item.coupon_share_url))
                        }else{
                            putString("url",UrlUtils.getIntactUrl(item.url))
                        }
                        putString("title",item.title)
                        putString("cover",UrlUtils.getIntactUrl(item.pict_url))
                    }
                    view.findNavController().navigate(R.id.ticketFragment,bundle)
                }
            }
            else-> null
        }


    }
}

//设置flowtextlayout内容
@BindingAdapter("text_list")
fun setFlowTextLayoutList(view:FlowTextLayout,list: ArrayList<String>?){
    if (list.isNullOrEmpty()){
        view.clear()
    }else{
        view.setTextList(view.context,list)
    }

}