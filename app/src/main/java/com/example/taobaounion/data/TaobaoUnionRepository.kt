package com.example.taobaounion.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.taobaounion.api.TaobaoUnionApi
import com.example.taobaounion.api.TaobaoUnionResponse
import com.example.taobaounion.data.home.Category
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.data.home.CategoryItemPagingSource
import com.example.taobaounion.data.onsell.OnSellContentItem
import com.example.taobaounion.data.onsell.OnSellPagingSource
import com.example.taobaounion.data.search.SearchContent
import com.example.taobaounion.data.search.SearchRecommend
import com.example.taobaounion.data.selected.SelectedCategory
import com.example.taobaounion.data.selected.SelectedContent
import com.example.taobaounion.data.ticket.TicketParams
import com.example.taobaounion.uitl.GsonCacheUtil
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

const val PAGE_SIZE=20
const val KEY_HISTORIES="key_histories"

@Singleton
class TaobaoUnionRepository @Inject constructor(
    private val api:TaobaoUnionApi
) {
    suspend fun getCategory():TaobaoUnionResponse<List<Category>>{
        return api.getCategory()
    }

    fun getCategoryItem(categoryId:Int):Flow<PagingData<HomePageContentItem>>{
        return Pager(
            config = PagingConfig(
                prefetchDistance = 5,
                enablePlaceholders = true,
                pageSize = PAGE_SIZE
            ),
            pagingSourceFactory = {CategoryItemPagingSource(api, categoryId)}
        ).flow
    }

   suspend fun getTicket(url:String,title:String?):TaobaoUnionResponse<Ticket>{
        return api.getTicket(TicketParams(url,title))
    }

    suspend fun getSelectedCategory():TaobaoUnionResponse<List<SelectedCategory>>{
        return api.getSelectedCategory()
    }

    suspend fun getSelectedContent(id:Long):TaobaoUnionResponse<SelectedContent>{
        return api.getSelectedContent(id)
    }

    fun getOnSellContent():Flow<PagingData<OnSellContentItem>>{
        return Pager(
            config = PagingConfig(
                prefetchDistance = 5,
                enablePlaceholders = true,
                pageSize = PAGE_SIZE*2
            ), pagingSourceFactory = {OnSellPagingSource(api)}
        ).flow
    }

    suspend fun getRecommend():TaobaoUnionResponse<List<SearchRecommend>>{
        return api.getRecommend()
    }

    suspend fun search(page:Int,keyword:String?):TaobaoUnionResponse<SearchContent>{
        return api.search(page,keyword)
    }

    fun getHistories():ArrayList<String>?{
        val histories=GsonCacheUtil.getValue(KEY_HISTORIES,Histories::class.java)
        if (histories!=null&&histories.histories.size>0){
            return histories.histories
        }
        return null
    }

    fun saveHistories(history:String){
        if (history.isBlank()){
            return
        }
        var histories=GsonCacheUtil.getValue(KEY_HISTORIES,Histories::class.java)
        var historiesList=ArrayList<String>()
        histories?.let {
            historiesList=it.histories
            //去掉重复的
            if (historiesList.isNotEmpty()&&historiesList.contains(history)){
                historiesList.remove(history)
            }
            if (historiesList.size>=10){
                //最多十个,多了删除第一个
                historiesList.removeAt(0)
            }
        }
        if (histories==null){
            //没有历史,histories==null,new一个实例
            histories=Histories(historiesList)
        }
        historiesList.add(history)
        GsonCacheUtil.saveCache(KEY_HISTORIES,histories)
    }

    fun deleteHistories(){
        GsonCacheUtil.deleteCache(KEY_HISTORIES)
    }

}

