package com.example.taobaounion.data.selected

data class SelectedContentItem(
    val category_id: Int,
    val click_url: String,
    val commission_rate: String,
    val coupon_amount: Int,
    val coupon_click_url: String?,
    val coupon_end_time: String?,
    val coupon_info: String,
    val coupon_remain_count: Int,
    val coupon_share_url: String?,
    val coupon_start_fee: String,
    val coupon_start_time: String?,
    val coupon_total_count: Int,
    val item_id: Long,
    val level_one_category_id: Int,
    val nick: String?,
    val pict_url: String,
    val reserve_price: String,
    val seller_id: Long,
    val shop_title: String?,
    val small_images: SmallImages?,
    val title: String,
    val user_type: Int,
    val volume: Int,
    val white_image: String?,
    val zk_final_price: String?
)