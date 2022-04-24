package com.example.taobaounion.views

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import com.example.taobaounion.R

class LoadingView:androidx.appcompat.widget.AppCompatImageView{
    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)
    private val animation:ObjectAnimator = ObjectAnimator.ofFloat(this,"rotation",0f,360f)

    init {
        animation.duration=1500
        animation.repeatCount=Animation.INFINITE
        animation.interpolator=LinearInterpolator()
        this.setImageResource(R.drawable.loading)
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        when(visibility){
            VISIBLE->animation.start()
            GONE->animation.cancel()
            else->animation.cancel()
        }
    }


}