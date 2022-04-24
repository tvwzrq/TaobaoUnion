package com.example.taobaounion.api

import com.google.gson.annotations.SerializedName

data class TaobaoUnionResponse<T>(
    @SerializedName("success")val success:Boolean,
    @SerializedName("code")val code:Int,
    @SerializedName("message")val message:String,
    @SerializedName("data")val data:T
) {
}