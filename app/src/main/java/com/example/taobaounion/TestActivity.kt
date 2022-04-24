package com.example.taobaounion

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.views.FlowTextLayout

class TestActivity:AppCompatActivity (){
    private lateinit var flowTextLayout:FlowTextLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        flowTextLayout=findViewById(R.id.test_flow_text_layout)
        val list=ArrayList<String>()
        list.add("first")
        list.add("second")
        list.add("third")
        list.add("four")
        list.add("five")
        list.add("six")
    }
}