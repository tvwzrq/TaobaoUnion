package com.example.taobaounion.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.taobaounion.R
import com.example.taobaounion.adapter.HomePageAdapter
import com.example.taobaounion.data.home.Category
import com.example.taobaounion.databinding.FragmentHomeBinding
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import com.example.taobaounion.uitl.API_RESPONSE_SUCCESS
import com.example.taobaounion.uitl.LOADING
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.viewmodel.home.HomeViewModel
import com.example.taobaounion.views.STATE_ERROR
import com.example.taobaounion.views.STATE_LOADING
import com.example.taobaounion.views.STATE_SUCCESS
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment:Fragment() {
    private lateinit var binding:FragmentHomeBinding
    private val viewModel by activityViewModels<HomeViewModel>()
    private lateinit var homePageAdapter: HomePageAdapter
    private lateinit var categoryList:List<Category>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryList=ArrayList()
        initUi()
    }

    private fun initUi(){
        binding.loadStateView.setLoadState(STATE_LOADING)

        binding.loadStateView.setOnClickListener {
            viewModel.getCategory()
            binding.loadStateView.setLoadState(LOADING)
        }
        //获取首页分类
        viewModel.categoryItems.observe(viewLifecycleOwner){
            when(it.code){
                //获取成功
                API_RESPONSE_SUCCESS->{
                    it.data?.let { list ->
                        categoryList=list
                        binding.loadStateView.setLoadState(STATE_SUCCESS)
                        initViewPager(list)
                    LogUtil.d("LIST",it.toString())}
                }
                API_RESPONSE_NO_NET->{
                    binding.loadStateView.setLoadState(STATE_ERROR)
                }
            }

        }
        viewModel.getCategory()
        }
      private fun initViewPager(list: List<Category>){
          if (list.isEmpty()){
              return
          }
            homePageAdapter= HomePageAdapter(list,this)

          binding.homePage.adapter=homePageAdapter

         // binding.viewpager.offscreenPageLimit=list.size//设置预加载数量
          //关联tablayout和viewpager2
            TabLayoutMediator(binding.tabLayout,binding.homePage){tab,position->
                tab.text=list[position].title
            }.attach()
          binding.searchHotkey.setOnClickListener {
              findNavController().navigate(com.example.taobaounion.R.id.fragment_search)
          }
          binding.scan.setOnClickListener {
             binding.homePage.currentItem=3
          }
    }


}

