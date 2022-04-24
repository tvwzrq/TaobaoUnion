package com.example.taobaounion.api

import com.example.taobaounion.data.Ticket
import com.example.taobaounion.data.ticket.TicketParams
import com.example.taobaounion.data.home.Category
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.data.onsell.OnSellContent
import com.example.taobaounion.data.search.SearchContent
import com.example.taobaounion.data.search.SearchRecommend
import com.example.taobaounion.data.selected.SelectedCategory
import com.example.taobaounion.data.selected.SelectedContent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

private const val BaseUrl="https://api.sunofbeaches.com/shop/"
interface TaobaoUnionApi {
    @GET("discovery/categories")
    suspend fun getCategory():TaobaoUnionResponse<List<Category>>

    @GET("discovery/{materialId}/{page}")
    suspend fun getCategoryItem(
        @Path("materialId")id:Int,
        @Path("page")page:Int
    ):TaobaoUnionResponse<List<HomePageContentItem>>

    @POST("tpwd")
    suspend fun getTicket(
        @Body ticketParams: TicketParams
    ):TaobaoUnionResponse<Ticket>

    @GET("recommend/categories")
    suspend fun getSelectedCategory():TaobaoUnionResponse<List<SelectedCategory>>

    @GET("recommend/{categoryId}")
    suspend fun getSelectedContent(
        @Path("categoryId")id:Long
    ):TaobaoUnionResponse<SelectedContent>

    @GET("onSell/{page}")
    suspend fun getOnSellContent(
        @Path("page")page:Int
    ):TaobaoUnionResponse<OnSellContent>

    @GET("search/recommend")
    suspend fun getRecommend():TaobaoUnionResponse<List<SearchRecommend>>

    @GET("search")
    suspend fun search(
        @Query("page")page:Int,
        @Query("keyword")keyword:String?
    ):TaobaoUnionResponse<SearchContent>

    companion object{
        private val logger=HttpLoggingInterceptor().apply {
            level=HttpLoggingInterceptor.Level.BASIC
        }
        private val client=OkHttpClient
            .Builder()
            .addInterceptor(logger)
            .callTimeout(8,TimeUnit.SECONDS)
            .readTimeout(5,TimeUnit.SECONDS)
            .build()
        fun create():TaobaoUnionApi{
            return Retrofit
                .Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build().create(TaobaoUnionApi::class.java)
        }

    }
}