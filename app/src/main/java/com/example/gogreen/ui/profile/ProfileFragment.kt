package com.example.gogreen.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gogreen.MainActivity
import com.example.gogreen.data.StationInfo
import com.example.gogreen.databinding.FragmentDashboardBinding
import com.example.gogreen.ui.home.AppConstants
import com.example.gogreen.utils.Preferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val profileViewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        val textView: TextView = binding.tvName
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llMain.visibility = View.GONE
        binding.shimmer.visibility = View.VISIBLE
        binding.shimmer.startShimmer()

        profileViewModel.getProfileInfo(Preferences.getData(AppConstants.WALLET_ADDRESS,"")!!)

        observeProfileData()
    }

    private fun observeProfileData() {
        profileViewModel.profileData.observe(viewLifecycleOwner){
            it?.let {
                when (it.status){
                    true ->
                    {
                        binding.shimmer.stopShimmer()
                        setUpUi(it)
                        binding.llMain.visibility = View.VISIBLE
                        binding.shimmer.visibility = View.GONE
                        println("success fail")
                    }
                    else -> {
                        println("fail")
                    }
                }
            }
        }
    }

    private fun setUpUi(stationInfo: StationInfo) {
        binding.tvName.text = stationInfo.station_data!!.station_name
        binding.tvAddressValue.text = stationInfo.station_data!!.station_address
        binding.tvCurrentValue.text = stationInfo.station_data!!.current.toString() + " A"
        binding.tvVoltageValue.text = stationInfo.station_data!!.voltage.toString() + " V"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}