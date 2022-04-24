package com.example.taobaounion.viewmodel.home

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taobaounion.api.TaobaoUnionResponse
import com.example.taobaounion.data.TaobaoUnionRepository
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: TaobaoUnionRepository
) :ViewModel(){

    private val _getCategory=MutableLiveData<Int>()

    fun getCategory(){
        _getCategory.postValue(0)
    }

    val categoryItems=Transformations.switchMap(_getCategory){
        liveData {
            try {
                emit(repository.getCategory())
            }catch (e:Exception){
                emit(TaobaoUnionResponse(false, API_RESPONSE_NO_NET,e.toString(),null))
            }
        }
    }

    fun getCategoryItem(categoryId:Int):Flow<PagingData<HomePageContentItem>>{
        return repository.getCategoryItem(categoryId).cachedIn(viewModelScope)
    }

    fun autoScrollBanner(duration:Long)= liveData {
        var count=0
        while (true){
            emit(count)
            count++
            kotlinx.coroutines.delay(duration)
        }
    }
}