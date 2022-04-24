package com.example.taobaounion.data.onsell

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taobaounion.api.TaobaoUnionApi
import com.example.taobaounion.uitl.LogUtil
import java.lang.Exception
import java.net.UnknownHostException

class OnSellPagingSource(
    private val api: TaobaoUnionApi
) :PagingSource<Int,OnSellContentItem>(){
    override fun getRefreshKey(state: PagingState<Int, OnSellContentItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OnSellContentItem> {
       return try{
            val page=params.key?:0
            val response=api.getOnSellContent(page)
            val items=response.data.tbk_dg_optimus_material_response.result_list.map_data
            LogUtil.d("ITEMS",items.toString())
            val prePage=if (page>0)page-1 else null
            val nextPage=if (items.isNotEmpty()) page+1 else null
            LoadResult.Page(items,prePage,nextPage)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
}