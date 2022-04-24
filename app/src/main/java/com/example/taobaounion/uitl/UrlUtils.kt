package com.example.taobaounion.uitl

class  UrlUtils{
    companion object {
        fun getIntactUrl(url: String?): String {
            url?.let {
                if (it.startsWith("http")||it.startsWith("https")){
                    return it
                }
            }
            //出错了有catch
            return "https:$url"
        }
    }
}