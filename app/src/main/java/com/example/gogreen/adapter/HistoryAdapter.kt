package com.example.gogreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gogreen.R
import com.example.gogreen.data.MyOrder
import com.example.gogreen.databinding.RowHistoryBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(requireActivity: FragmentActivity) :
    ListAdapter<MyOrder, HistoryAdapter.HistoryViewHolder>(DiffUtil()) {
    var context = requireActivity
    lateinit var rowHistoryBinding: RowHistoryBinding

    class HistoryViewHolder(view: RowHistoryBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        rowHistoryBinding =
            RowHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(rowHistoryBinding)
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<MyOrder>() {
        override fun areItemsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem.order_id == newItem.order_id
        }

        override fun areContentsTheSame(oldItem: MyOrder, newItem: MyOrder): Boolean {
            return oldItem == newItem
        }

    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = getItem(position)
        val totalSecs = item.energyTransferTimeInSeconds / 1000
//        val hours = totalSecs / 3600;
//        val minutes = (totalSecs % 3600) / 60;
        val minutes = (totalSecs) / 60;
        val seconds = totalSecs % 60;
        var timeString: String = ""
        timeString = if (minutes == 0)
            String.format("%02d Sec", seconds)
        else
            String.format("%02d Min %02d Sec ", minutes, seconds)

        item.chargeDuration = timeString!!

        val inputDate = item.updationDate
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
        val outputFormat = SimpleDateFormat("dd/MM/yyyy")
        val outputTimeFormat = SimpleDateFormat("hh:mm a")
        val date: Date = inputFormat.parse(inputDate)
        val outputDate: String = outputFormat.format(date)
        val outputTime: String = outputTimeFormat.format(date)
        println(outputDate + " at " + outputTime)
        item.transactionDate = "$outputDate at $outputTime"

        rowHistoryBinding.myOrder = item
        if (position % 2 == 0)
            rowHistoryBinding.llMain.setBackgroundColor(
                context.resources.getColor(
                    R.color.background_green,
                    null
                )
            )
        else
            rowHistoryBinding.llMain.setBackgroundColor(
                context.resources.getColor(
                    R.color.background_grey,
                    null
                )
            )

    }
}