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
                        //?????????????????????????????????
                        getContent(list[0])
                        //??????????????????
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
        //??????????????????
        rightContentAdapter= SelectedContentAdapter()
        leftAdapter= SelectedCategoryAdapter{selectedCategory ->getContent(selectedCategory)  }
        //????????????
        viewModel.getSelectedCategory()
        //???????????????
        binding.leftCategory.layoutManager=LinearLayoutManager(this.context)
        binding.leftCategory.adapter=leftAdapter
        binding.rightContentList.layoutManager=LinearLayoutManager(this.context)
        binding.rightContentList.adapter=rightContentAdapter
    }

   @SuppressLint("NotifyDataSetChanged")
   private fun getContent(category:SelectedCategory){

       binding.loadingView.visibility=View.VISIBLE
       //????????????????????????????????????
       binding.rightContentList.alpha=0.5f
       //??????????????????????????????
        if (!category.isSelected){

            //????????????
            viewModel.getSelectedContent(category.favorites_id).observe(viewLifecycleOwner){
                when(it.code){
                    API_RESPONSE_SUCCESS->{
                        //????????????????????????
                        leftAdapter.currentList.forEach { item->
                            item.isSelected=false
                        }
                        category.isSelected=true
                        leftAdapter.notifyDataSetChanged()
                        //??????????????????
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