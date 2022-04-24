package com.example.taobaounion.viewmodel.onsell

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.taobaounion.data.TaobaoUnionRepository
import com.example.taobaounion.data.onsell.OnSellContentItem
import com.example.taobaounion.uitl.LogUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class OnSellViewModel @Inject constructor(
    private val repository: TaobaoUnionRepository
):ViewModel() {

    fun getOnSellContent():Flow<PagingData<OnSellContentItem>>{
        return repository.getOnSellContent().cachedIn(viewModelScope)
    }
init {
    LogUtil.d("INITONSELL","init")
}

}