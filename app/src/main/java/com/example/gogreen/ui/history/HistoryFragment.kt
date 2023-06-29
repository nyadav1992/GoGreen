package com.example.gogreen.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gogreen.MainActivity
import com.example.gogreen.adapter.HistoryAdapter
import com.example.gogreen.databinding.FragmentHistoryBinding
import com.example.gogreen.ui.home.AppConstants
import com.example.gogreen.utils.Preferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null

    private val historyViewModel: HistoryViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        (requireActivity() as MainActivity).supportActionBar!!.hide()

        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textHistoryTitle
        historyViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        Preferences.saveData(AppConstants.STATION_ID, "0xb8E506fF8F26E9517b158F5Ac9dCD1bc4D29E890")

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.shimmer.startShimmer()
        historyViewModel.getHistoryData(Preferences.getData(AppConstants.STATION_ID,"")!!)

        observeHistoryData()
    }

    private fun observeHistoryData() {
        historyViewModel.historyData.observe(this){
            val adapter: HistoryAdapter = HistoryAdapter()
            binding.rvHistory.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            binding.rvHistory.adapter = adapter
            adapter.submitList(it.myOrders)
            binding.shimmer.stopShimmer()
            binding.shimmer.visibility = View.GONE
//            it.status
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}