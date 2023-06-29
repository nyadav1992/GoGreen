package com.example.gogreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.gogreen.data.MyOrder
import com.example.gogreen.databinding.RowHistoryBinding

class HistoryAdapter : ListAdapter<MyOrder, HistoryAdapter.HistoryViewHolder>(DiffUtil()) {

    lateinit var rowHistoryBinding: RowHistoryBinding

    class HistoryViewHolder(view: RowHistoryBinding): RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        rowHistoryBinding = RowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(rowHistoryBinding)
    }

    class DiffUtil: androidx.recyclerview.widget.DiffUtil.ItemCallback<MyOrder>(){
        override fun areItemsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem.order_id == newItem.order_id
        }

        override fun areContentsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        rowHistoryBinding.myOrder = item
    }
}