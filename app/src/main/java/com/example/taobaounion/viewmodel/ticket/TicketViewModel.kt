package com.example.taobaounion.viewmodel.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.taobaounion.api.TaobaoUnionResponse
import com.example.taobaounion.data.TaobaoUnionRepository
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import dagger.hilt.android.lifecycle.HiltViewModel
import java.net.UnknownHostException
import javax.inject.Inject
@HiltViewModel
class TicketViewModel @Inject constructor(
    private val repository: TaobaoUnionRepository
):ViewModel() {

    fun getTicket(url:String,title:String?)= liveData {
        try {
            emit(repository.getTicket(url, title))
        }catch (e:Exception){
            emit(TaobaoUnionResponse(false, API_RESPONSE_NO_NET,e.toString(),null))
        }
    }
}