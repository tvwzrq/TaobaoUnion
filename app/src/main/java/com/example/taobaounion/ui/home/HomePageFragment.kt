package com.example.taobaounion.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taobaounion.adapter.CategoryItemAdapter
import com.example.taobaounion.adapter.FooterAdapter
import com.example.taobaounion.adapter.HomeFirstAdapter
import com.example.taobaounion.data.home.HomePageContentItem
import com.example.taobaounion.databinding.FragmentHomePageBinding
import com.example.taobaounion.uitl.BANNER_SIZE
import com.example.taobaounion.uitl.CategoryItemCallback
import com.example.taobaounion.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomePageFragment:Fragment (){
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var binding:FragmentHomePageBinding
    private lateinit var categoryItemAdapter:CategoryItemAdapter
    private var job:Job?=null
    private lateinit var firstAdapter: HomeFirstAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomePageBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle->
            initUi(bundle.getInt("categoryId"))
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initUi(categoryId:Int){
        firstAdapter= HomeFirstAdapter()
        //Pagindata不能直接转换为List集合,这里用回调在recyclerview里获取, 应该有更好的办法
        categoryItemAdapter=CategoryItemAdapter(object :CategoryItemCallback{
            override fun onSuccess(items: List<HomePageContentItem>) {
                val bannerList=items.subList(items.size - BANNER_SIZE,items.size-1)
                firstAdapter.setBannerList(bannerList)
                firstAdapter.refresh()
            }
        })

        job?.cancel()
        job=lifecycleScope.launch {
            viewModel.getCategoryItem(categoryId)
                .collect{
                categoryItemAdapter.submitData(it)
            }
        }
        categoryItemAdapter.addLoadStateListener {
            binding.homePageRefresh.isRefreshing=it.refresh is LoadState.Loading
        }
        binding.homePageRefresh.setOnRefreshListener {
            categoryItemAdapter.refresh()
        }
      //合并适配器
        val concatAdapter=categoryItemAdapter.withLoadStateFooter(
            FooterAdapter{categoryItemAdapter.retry()}
        )
        concatAdapter.addAdapter(0,firstAdapter)
        binding.categoryItemList.layoutManager=LinearLayoutManager(context)
        binding.categoryItemList.adapter=concatAdapter

        //自动轮播
        viewModel.autoScrollBanner(3000).observe(viewLifecycleOwner){
            firstAdapter.autoScroll()
        }
    }
}