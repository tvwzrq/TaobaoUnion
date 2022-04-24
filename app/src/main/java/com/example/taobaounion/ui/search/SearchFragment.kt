package com.example.taobaounion.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taobaounion.adapter.SearchResultAdapter
import com.example.taobaounion.data.search.SearchContentItem
import com.example.taobaounion.databinding.FragmentSearchBinding
import com.example.taobaounion.uitl.*
import com.example.taobaounion.viewmodel.search.SearchViewModel
import com.example.taobaounion.views.STATE_ERROR
import com.example.taobaounion.views.STATE_LOADING
import com.example.taobaounion.views.STATE_SUCCESS
import com.example.taobaounion.views.TextClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment:Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private val viewModel by viewModels<SearchViewModel>()
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var searchContentItems=ArrayList<SearchContentItem>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchBinding.inflate(inflater,container,false)
        binding.lifecycleOwner=this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel=viewModel
        initUi()
    }

    private fun initUi() {

        searchResultAdapter= SearchResultAdapter()
        binding.searchResultContent.layoutManager=LinearLayoutManager(this.context)
        binding.searchResultContent.adapter=searchResultAdapter
//通过键盘开始搜索
        binding.searchEdittext.setOnEditorActionListener { textview_, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                toSearch()
            }
            true
        }

        //动态显示输入框清空按钮
        viewModel.keyWord.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                binding.searchRemove.visibility=View.VISIBLE
            }else{
                binding.searchRemove.visibility=View.GONE
            }
        }

        binding.historiesDelete.setOnClickListener {
            viewModel.deleteHistories()
            viewModel.historyVisibility.postValue(8)

            //清空flowtextlayout
            viewModel.histories.postValue(null)
        }
//点击推荐开始搜索
        binding.recommends.setClickListener(object :TextClickListener{
            override fun onTextClickListener(text: String) {
                viewModel.keyWord.value=text
                toSearch()
            }
        })
        //点击历史开始搜索
        binding.histories.setClickListener(object :TextClickListener{
            override fun onTextClickListener(text: String) {
                viewModel.keyWord.value=text
                toSearch()
            }
        })

            //搜索结果
        viewModel.searchContent.observe(viewLifecycleOwner){
            when(it?.code){
                API_RESPONSE_SUCCESS->{
                    binding.loadStateView.setLoadState(STATE_SUCCESS)
                    val items=it.data?.tbk_dg_material_optional_response?.result_list?.map_data
                    items?.let {
                        viewModel.historyVisibility.postValue(8)
                        viewModel.recommendVisibility.postValue(8)
                        viewModel.refreshLayoutVisibility.value=View.VISIBLE

                         if (binding.smartRefreshLayout.isLoading) {
                            //加载更多的时候添加数据
                            binding.smartRefreshLayout.finishLoadMore(true)
                            LogUtil.d("LOAD", "-->loading")
                            searchContentItems.addAll(items)
                            searchResultAdapter.notifyItemRangeInserted(searchContentItems.size - items.size,
                                items.size)
                        }else{
                            //第一次搜索或者刷新的时候清空绑定的列表
                                if (binding.smartRefreshLayout.isRefreshing){
                                    binding.smartRefreshLayout.finishRefresh(true)
                                }
                            searchContentItems.clear()
                             searchContentItems.addAll(items)
                            searchResultAdapter.submitList(searchContentItems)
                             searchResultAdapter.notifyItemRangeChanged(0,items.size)
                        }
                    }
                }
                API_RESPONSE_FAILED->{
                    binding.loadStateView.setLoadState(STATE_SUCCESS)
                    viewModel.refreshLayoutVisibility.postValue(8)
                    if (! viewModel.histories.value.isNullOrEmpty()){
                        viewModel.historyVisibility.postValue(0)
                    }
                    viewModel.recommendVisibility.postValue(0)
                    Toast.makeText(this.context, it.message, Toast.LENGTH_SHORT).show()
                }
                API_RESPONSE_NO_NET->{
                    viewModel.refreshLayoutVisibility.postValue(8)
                    binding.loadStateView.setLoadState(STATE_ERROR)
                }
            }
        }

        //下拉刷新和上拉加载
        binding.smartRefreshLayout.setOnRefreshListener {
            //重新搜索
            viewModel.search()
        }

        binding.smartRefreshLayout.setOnLoadMoreListener {
            viewModel.searchLoadMore()
        }

        //清空输入框
        binding.searchRemove.setOnClickListener {
            searchContentItems.clear()
            viewModel.apply {
                keyWord.postValue("")
                refreshLayoutVisibility.value = 8
                getHistory()
                getRecommend()
            }
        }
        //通过搜索按钮搜索
        binding.searchButton.setOnClickListener {
              toSearch()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //清空搜索相关内容
        viewModel.clearSearchContent()
    }

    private fun toSearch(){
        searchContentItems.clear()
        viewModel.search()
        binding.loadStateView.setLoadState(STATE_LOADING)
        KeyBoardUtil.hide(binding.root)
      //  binding.searchEdittext.isFocusable=true
        binding.searchEdittext.setSelectAllOnFocus(true)
    }
}