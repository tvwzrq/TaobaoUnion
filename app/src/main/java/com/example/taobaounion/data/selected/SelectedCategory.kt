package com.example.taobaounion.data.selected

data class SelectedCategory(
    val  type: Int,
    val favorites_id: Long,
    val favorites_title:String,
    var isSelected:Boolean=false
)