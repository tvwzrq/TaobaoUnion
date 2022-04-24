package com.example.taobaounion.views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.taobaounion.R
import com.example.taobaounion.uitl.LogUtil

const val DEFAULT_SPACE=10f
class FlowTextLayout:ViewGroup {
    private var horizontalSpace= DEFAULT_SPACE
    private var verticalSpace=DEFAULT_SPACE
    private var line=ArrayList<TextView>()
    private var lines =ArrayList<List<TextView>>()
    private var selfWidth=0
    private var selfHeight=0
    private var childHeight=0//子view高度
    private var textClickListener:TextClickListener?=null

    constructor(context: Context):this(context,null)
    constructor(context: Context,attributeSet: AttributeSet?):this(context,attributeSet,0)
    constructor(context: Context,attributeSet: AttributeSet?,defStyle:Int):super(context,attributeSet,defStyle){
        val typedArray=context.obtainStyledAttributes(attributeSet,R.styleable.FlowTextLayout)
        LogUtil.d("INDEX",typedArray.indexCount.toString())
        horizontalSpace=typedArray.getDimension(R.styleable.FlowTextLayout_horizontal_space,
            DEFAULT_SPACE)
        LogUtil.d("SPACE",horizontalSpace.toString())
        verticalSpace=typedArray.getDimension(R.styleable.FlowTextLayout_vertical_space,
            DEFAULT_SPACE)
        LogUtil.d("SPACE",verticalSpace.toString())
        typedArray.recycle()
    }
    private var textList = ArrayList<String>()



    fun setTextList(context: Context?,list:List<String>){
        this.textList=list as ArrayList<String>
        this.removeAllViews()
        for (text in textList){
           val item=LayoutInflater.from(context).inflate(R.layout.view_flow_text,this,false)as TextView
            item.text=text
            this.addView(item)

        }
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        lines=ArrayList()
        line= ArrayList()
        selfWidth=MeasureSpec.getSize(widthMeasureSpec)-paddingLeft-paddingRight
        LogUtil.d("SELFWIDTH","--->$selfWidth")
        //测量child
        for (index in 1..childCount){
            val view=getChildAt(index-1)as TextView
            if (view.visibility!=View.VISIBLE){
                continue
            }
            LogUtil.d("MEASURE","width--->${view.measuredWidth}")
            measureChild(view,widthMeasureSpec,heightMeasureSpec)
            LogUtil.d("MEASURE","width--->${view.measuredWidth}")
            childHeight=view.measuredHeight
            if (canAddView(view,line)){
                line.add(view)
            }else{
                //一行满了之后加到lines里面
                lines.add(line)
                line=ArrayList()
                line.add(view)
            }
        }
        //测量自己
        selfHeight = if (lines.isNotEmpty()||line.isNotEmpty()){
            if (line.size>0){
                (childHeight*(lines.size+1)+lines.size*verticalSpace).toInt()+paddingTop+paddingBottom
            }else{
                childHeight+paddingTop+paddingBottom
            }
        }else{
            0
        }
        setMeasuredDimension(selfWidth ,selfHeight)
    }
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var topOffset=paddingTop//到顶部的距离
        var leftOffset=paddingLeft//到左侧的距离
      for (viewList in lines){
          for (textView in viewList){
              textView.layout(leftOffset,topOffset,textView.measuredWidth+leftOffset,childHeight+topOffset)
              leftOffset+=(textView.measuredWidth+horizontalSpace).toInt()
          }
          leftOffset=paddingLeft//放完一行清0
          if (viewList.isNotEmpty()){
              topOffset+=(childHeight+verticalSpace).toInt()
          }
      }
        //不够一行的view没有加到lines里
        for(textView in line){
            textView.layout(leftOffset,topOffset,textView.measuredWidth+leftOffset,topOffset+childHeight)
            leftOffset+=(textView.measuredWidth+horizontalSpace).toInt()
        }
        initListener()
    }

    private fun canAddView(view:TextView,line:List<View>): Boolean {
        var totalWidth=0f
        for (viewItem in line){
            totalWidth+=viewItem.measuredWidth
        }
        LogUtil.d("canAddView",view.text.toString())
        totalWidth+=view.measuredWidth
        LogUtil.d("HSPACE","--.$horizontalSpace")
        totalWidth+=(line.size*horizontalSpace)//n个item有n-1个间距
        LogUtil.d("total_width","-->$totalWidth")
        return totalWidth<=selfWidth//总宽度小于控件宽度则可以添加
    }
      fun setClickListener(listener:TextClickListener){
          this.textClickListener=listener
     }
    private fun  initListener(){
        textClickListener?.let {listener->
            for (index in 1..childCount){
                getChildAt(index-1).setOnClickListener { view->
                    listener.onTextClickListener((view as TextView).text.toString())
                    LogUtil.d("SERACTKEY",view.text.toString())
                }
            }
        }
        }

    fun addText(text: String?) {

        if (text.isNullOrEmpty()||textList.contains(text)){
            return
        }
        val item=LayoutInflater.from(context).inflate(R.layout.view_flow_text,this,false)as TextView
        item.text=text

        textList.add(text)
        this.addView(item)
    }

    fun clear() {
        this.textList.clear()
        this.removeAllViews()
    }

}
interface TextClickListener{
  fun onTextClickListener(text:String)
}