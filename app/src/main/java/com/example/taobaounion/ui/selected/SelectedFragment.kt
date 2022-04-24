package com.example.taobaounion.ui.selected

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taobaounion.R
import com.example.taobaounion.adapter.SelectedCategoryAdapter
import com.example.taobaounion.adapter.SelectedContentAdapter
import com.example.taobaounion.data.selected.SelectedCategory
import com.example.taobaounion.databinding.FragmentSelectedBinding
import com.example.taobaounion.uitl.API_RESPONSE_NO_NET
import com.example.taobaounion.uitl.API_RESPONSE_SUCCESS
import com.example.taobaounion.uitl.LogUtil
import com.example.taobaounion.viewmodel.selected.SelectedViewModel
import com.example.taobaounion.views.STATE_ERROR
import com.example.taobaounion.views.STATE_LOADING
import com.example.taobaounion.views.STATE_SUCCESS
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SelectedFragment:Fragment() {
    private lateinit var binding:FragmentSelectedBinding
    private val viewModel by viewModels<SelectedViewModel>()
    private lateinit var leftAdapter:SelectedCategoryAdapter
    private lateinit var rightContentAdapter: SelectedContentAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSelectedBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        initObserver()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
    }
    private fun initObserver(){
        viewModel.categories.observe(viewLifecycleOwner){
            LogUtil.d("RESPONSE",it.toString())
            when(it.code){
                API_RESPONSE_SUCCESS->{
                   it.data?.let {list->
                        //右侧默认加载第一个类型
                        getContent(list[0])
                        //加载左侧列表
                        leftAdapter.submitList(list)
                    }
                    binding.loadStateView.setLoadState(STATE_SUCCESS)
                }
                API_RESPONSE_NO_NET->{
                    binding.loadStateView.setLoadState(STATE_ERROR)
                }
            }
        }
    }

    private fun initUi() {
        binding.loadStateView.setLoadState(STATE_LOADING)
        binding.loadStateView.setOnClickListener {
            viewModel.getSelectedCategory()
            binding.loadStateView.setLoadState(STATE_LOADING)
        }
        //实例化适配器
        rightContentAdapter= SelectedContentAdapter()
        leftAdapter= SelectedCategoryAdapter{selectedCategory ->getContent(selectedCategory)  }
        //获取分类
        viewModel.getSelectedCategory()
        //设置适配器
        binding.leftCategory.layoutManager=LinearLayoutManager(this.context)
        binding.leftCategory.adapter=leftAdapter
        binding.rightContentList.layoutManager=LinearLayoutManager(this.context)
        binding.rightContentList.adapter=rightContentAdapter
    }

   @SuppressLint("NotifyDataSetChanged")
   private fun getContent(category:SelectedCategory){

       binding.loadingView.visibility=View.VISIBLE
       //改一下加载的时候的透明度
       binding.rightContentList.alpha=0.5f
       //重复点击不会重新加载
        if (!category.isSelected){

            //获取数据
            viewModel.getSelectedContent(category.favorites_id).observe(viewLifecycleOwner){
                when(it.code){
                    API_RESPONSE_SUCCESS->{
                        //改变左侧选中状态
                        leftAdapter.currentList.forEach { item->
                            item.isSelected=false
                        }
                        category.isSelected=true
                        leftAdapter.notifyDataSetChanged()
                        //加载右侧列表
                        val items=it.data?.tbk_dg_optimus_material_response?.result_list?.map_data
                        LogUtil.d("CONTENT",items.toString())
                        binding.loadingView.visibility=View.GONE
                        binding.rightContentList.alpha=1f
                        rightContentAdapter.submitList(items)


                    }
                    API_RESPONSE_NO_NET->{
                        binding.rightContentList.alpha=1f
                        binding.loadingView.visibility=View.GONE
                        Toast.makeText(this.context, getString(R.string.NO_NET), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}