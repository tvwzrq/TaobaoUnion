package com.example.taobaounion.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.taobaounion.data.selected.SelectedCategory
import com.example.taobaounion.databinding.ListItemSelectedCategoryBinding

class SelectedCategoryAdapter(val getContent:(SelectedCategory)->Unit):ListAdapter<SelectedCategory,SelectedCategoryAdapter.ViewHolder>(SelectedDiff()){
    class SelectedDiff:DiffUtil.ItemCallback<SelectedCategory>(){
        override fun areContentsTheSame(
            oldItem: SelectedCategory,
            newItem: SelectedCategory
        ): Boolean {
            return oldItem==newItem
        }

        override fun areItemsTheSame(
            oldItem: SelectedCategory,
            newItem: SelectedCategory
        ): Boolean {
            return oldItem.favorites_id==newItem.favorites_id
        }
    }

    class ViewHolder(
        val binding:ListItemSelectedCategoryBinding
    ):RecyclerView.ViewHolder(binding.root){
        fun bind(category:SelectedCategory){
            binding.category=category
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):ViewHolder {
        return ViewHolder(ListItemSelectedCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category=getItem(position)
        category?.let {
            holder.bind(category)
            holder.binding.categoryName.setOnClickListener {
                getContent(category)
            }
        }

    }

}

