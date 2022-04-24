package com.example.taobaounion.views

import android.content.Context
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.example.taobaounion.R

class RTextView:AppCompatTextView {
    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):super(context,attributeSet){
        val typedArray=context.obtainStyledAttributes(attributeSet,R.styleable.RTextView)
        for(index in 1..typedArray.indexCount){
            when(typedArray.getIndex(index-1)){
                R.styleable.RTextView_flag->{
                    when(typedArray.getInt(R.styleable.RTextView_flag,1   )){
                        1->this.paint.flags=Paint.UNDERLINE_TEXT_FLAG
                        2->this.paint.flags=Paint.STRIKE_THRU_TEXT_FLAG
                    }
                }

            }
        }
        typedArray.recycle()
    }



}