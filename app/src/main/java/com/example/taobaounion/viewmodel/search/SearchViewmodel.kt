package com.example.taobaounion.viewmodel.search

import android.view.View
import androidx.lifecycle.*
import com.example.taobaounion.api.TaobaoUnionResponse
import com.example.taobaounion.data.TaobaoUnionRepository
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import com.example.taobaounion.uitl.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: TaobaoUnionRepository
) : ViewModel(){

//不知道为啥,viewmodel不会随着fragment销毁,可见性放在这里容易管理
    val historyVisibility=MutableLiveData<Int>()
    val  recommendVisibility=MutableLiveData<Int>()
    val refreshLayoutVisibility=MutableLiveData<Int>()

    val histories=MutableLiveData<ArrayList<String>>()
    val recommends=MutableLiveData<ArrayList<String>>()

    private var curPage=MutableLiveData<Int>()
    val keyWord=MutableLiveData<String>()


    init {
        historyVisibility.value=View.VISIBLE
        recommendVisibility.value=View.VISIBLE
        //默认不可见
        refreshLayoutVisibility.value=View.GONE
         //获取搜索历史
        getHistory()
        //获取搜索热词
        getRecommend()
    }

    fun getRecommend(){
        viewModelScope.launch {
            try {
                val response=repository.getRecommend()
                val searchRecommendList=response.data
                val recommends=ArrayList<String>()
                searchRecommendList.forEach {
                    recommends.add(it.keyword)
                }
                if (recommends.isNullOrEmpty()){
                    this@SearchViewModel.recommendVisibility.value=View.GONE
                }else if (refreshLayoutVisibility.value!=View.VISIBLE){
                    this@SearchViewModel.recommendVisibility.value=View.VISIBLE
                    this@SearchViewModel.recommends.postValue(recommends)
                }
            }catch (e:Exception){
                this@SearchViewModel.recommendVisibility.value=View.GONE
            }

        }
    }

    fun search(){
        this.curPage.postValue(0)
    }

    fun searchLoadMore(){
        this.curPage.postValue(curPage.value!!+1 )
    }

    var searchContent=Transformations.switchMap(curPage){page->
        liveData {
            try {
                //存入搜索历史
                saveHistories(keyWord.value?:"")
                if (page<0){
                    emit(null)
                }else{
                    emit(repository.search(page,keyWord.value))
                }
            }catch (e:Exception){
                emit(TaobaoUnionResponse(false, API_RESPONSE_NO_NET,e.toString(),null))
            }
        }
    }

    fun clearSearchContent(){
        curPage.value=-1
        keyWord.value=""
        historyVisibility.value=View.VISIBLE
        recommendVisibility.value=View.VISIBLE
        refreshLayoutVisibility.value=View.GONE
    }

     fun getHistory() {
             try {
                 val histories=repository.getHistories()
                 if (!histories.isNullOrEmpty()){
                     this@SearchViewModel.histories.postValue(histories)
                   if (refreshLayoutVisibility.value==View.GONE){
                       historyVisibility.value=View.VISIBLE
                   }else{
                       historyVisibility.value=View.GONE
                   }
                 }
             }catch (e:Exception){
                 historyVisibility.value=View.GONE
             }

    }

    fun deleteHistories(){
        repository.deleteHistories()
    }

    private fun saveHistories(history:String){
        if (history.isBlank()){
            return
        }
        val historyWord=history.trim()
        repository.saveHistories(historyWord)
        getHistory()
    }



}