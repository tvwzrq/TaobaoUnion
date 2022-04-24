package com.example.taobaounion.uitl

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.taobaounion.MainApplication

class KeyBoardUtil {
    companion object{
        fun hide(view: View){
            val inputManager=MainApplication.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken,0)
        }
        fun show(view: View){
            val inputManager=MainApplication.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(view.windowToken,0)
        }
    }
}