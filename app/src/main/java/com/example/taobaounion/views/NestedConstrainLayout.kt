package com.example.taobaounion.views


import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.absoluteValue

class NestedConstrainLayout:ConstraintLayout{
    constructor(context: Context):super(context)
    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet)
    private var touchSlop=0
    private var initialX=0f
    private var initialY=0f

    var isTouched=false
    init {
        touchSlop=ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        handleTouchEvent(ev)
        return super.onInterceptTouchEvent(ev)
    }

    private fun handleTouchEvent(event: MotionEvent?) {
        when(event?.action){
            MotionEvent.ACTION_DOWN->{
                initialX=event.x
                initialY=event.y
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_MOVE->{
                val dx=event.x-initialX
                val dy=event.y-initialY

                //让X方向滑动优先
                val scaledX=dx.absoluteValue*1.5
                val scaledY=dy.absoluteValue*1
                if (scaledX>touchSlop||scaledY>touchSlop) {
                    if (scaledX>scaledY){
                        //阻止父view消费事件
                        parent.requestDisallowInterceptTouchEvent(true)
                    }else{
                        //让父view消费事件,比如recyclerview
                        parent.requestDisallowInterceptTouchEvent(false)
                    }

                }

            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action){
            MotionEvent.ACTION_DOWN->isTouched=true
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL->isTouched=false
        }
        return super.dispatchTouchEvent(ev)
    }
}