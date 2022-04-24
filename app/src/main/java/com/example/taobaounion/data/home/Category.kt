package com.example.taobaounion.data.home

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")val id:Int,
    @SerializedName("title")val title:String
)
