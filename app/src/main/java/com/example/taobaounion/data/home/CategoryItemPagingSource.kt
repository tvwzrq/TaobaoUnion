package com.example.taobaounion.data.home

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.taobaounion.api.TaobaoUnionApi
import javax.inject.Inject

class CategoryItemPagingSource @Inject constructor(
    private val api: TaobaoUnionApi,
    private val categoryId:Int
    ):PagingSource<Int,HomePageContentItem>(){
    override fun getRefreshKey(state: PagingState<Int, HomePageContentItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomePageContentItem> {
        return try {
            val page=params.key?:0
            val response=api.getCategoryItem(categoryId,page)
            val items= response.data
            val nextPage = if (page<10)page + 1 else null//接口最多只有10页
            val preyPage = if (page > 0) page - 1 else null
            LoadResult.Page(items,preyPage,nextPage)
        }catch (e:Exception){
            LoadResult.Error(e)
        }
    }
    }