package com.example.taobaounion.viewmodel.selected

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.taobaounion.api.TaobaoUnionResponse
import com.example.taobaounion.data.TaobaoUnionRepository
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectedViewModel @Inject constructor(
    private val repository: TaobaoUnionRepository
) :ViewModel(){


    private val _getSelectedCategory=MutableLiveData<Int>()
     fun getSelectedCategory(){
         _getSelectedCategory.postValue(0)
     }

    val categories=Transformations.switchMap(_getSelectedCategory){
        liveData {
            try {
                emit(repository.getSelectedCategory())
            }catch (e:Exception){
                emit(TaobaoUnionResponse(false, API_RESPONSE_NO_NET,e.toString(),null))
            }
        }
    }

    fun getSelectedContent(id:Long)= liveData {
        try {
            emit(repository.getSelectedContent(id))
        }catch(e:Exception){
            emit(TaobaoUnionResponse(true, API_RESPONSE_NO_NET,e.toString(),null))
        }
    }
}