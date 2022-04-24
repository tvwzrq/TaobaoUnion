package com.example.taobaounion.uitl

import android.content.Context
import com.example.taobaounion.MainApplication
import com.example.taobaounion.data.CacheWithDuration
import com.google.gson.Gson

object GsonCacheUtil {
    private const val JSON_CACHE_SP_NAME="json_cache_sp_name"
    private val pref=MainApplication.context.getSharedPreferences(JSON_CACHE_SP_NAME,Context.MODE_PRIVATE)
    private val mGson=Gson()

    fun saveCache(key:String,value:Any){
        this.saveCache(key,value,-1L)
    }
    fun saveCache(key:String,value:Any,duration:Long){
         pref.edit().apply {
             val mDuration=if (duration==-1L) duration else System.currentTimeMillis()+duration
             val cacheWithTime= mGson.toJson(CacheWithDuration(mGson.toJson(value),mDuration))
             putString(key,cacheWithTime)
             apply()
         }
    }

    fun deleteCache(key:String){
        pref.edit().remove(key).apply()
    }

    fun< T> getValue(key: String,classOfT:Class<T>): T? {
        val valueWithDuration= pref.getString(key,null) ?: return null
        val cacheWithDuration= mGson.fromJson(valueWithDuration,CacheWithDuration::class.java)
        return if (cacheWithDuration.duration!=-1L&&cacheWithDuration.duration-System.currentTimeMillis()<=0){
            //过期了
            null
        }else{
            //没过期
            mGson.fromJson(cacheWithDuration.jsonObject,classOfT)
        }

    }


}