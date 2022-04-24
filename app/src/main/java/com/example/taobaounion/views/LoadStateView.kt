package com.example.taobaounion.views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.taobaounion.R
import com.google.android.material.imageview.ShapeableImageView

const val STATE_LOADING=0
const val STATE_ERROR=1
const val STATE_SUCCESS=2
class LoadStateView:ConstraintLayout{
    init {
       LayoutInflater.from(context).inflate(R.layout.view_loadstate,this)
    }
    private var loadStateText:TextView = this.findViewById(R.id.load_state_text)
    private var loadFailed:ShapeableImageView = this.findViewById(R.id.load_failed_image)
    private var loading:LoadingView = this.findViewById(R.id.loading)

    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)
    constructor(context: Context):super(context)

    fun setLoadState(loadState:Int){
        when(loadState){
            STATE_LOADING->{
                this.visibility=View.VISIBLE
                loading.visibility=View.VISIBLE
                loadStateText.visibility=View.VISIBLE
                loadStateText.text=context.getString(R.string.LOADING)
                loadFailed.visibility=View.GONE
            }
            STATE_ERROR->{
                this.visibility=View.VISIBLE
                loading.visibility=View.GONE
                loadStateText.visibility=View.VISIBLE
                loadStateText.text=context.getString(R.string.LOADING_ERROR)
                loadFailed.visibility=View.VISIBLE
            }
            STATE_SUCCESS->{
                this.visibility=View.GONE
            }
        }
    }


}