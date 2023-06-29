package com.example.gogreen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.gogreen.CaptureActivityPortrait
import com.example.gogreen.MainActivity
import com.example.gogreen.R
import com.example.gogreen.data.StationInfo
import com.example.gogreen.databinding.FragmentHomeBinding
import com.example.gogreen.ui.stationinfo.StationInfoChargingFragment
import com.example.gogreen.utils.Preferences
import com.example.gogreen.viewmodel.SharedViewModel
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val homeViewModel : HomeViewModel by viewModels()
    private val sharedViewModel : SharedViewModel by activityViewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        val textView: TextView = binding.textHome
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ivBarcode.setOnClickListener {

//            launchChargingInfoFragment()

            homeViewModel.getStationInfo("0x08803E271cD9097aD002Bf95Bf01142A654BAf6E")



//            launchBarcodeScanner()

        }

        observeStationInfoData()

    }

    private fun observeStationInfoData() {
        homeViewModel.stationData.observe(this){
            it?.let {
                when (it.status){
                    true ->
                    {
                        launchChargingInfoFragment(it)
                        println("success fail")
                    }
                    else -> {
                        println("fail")
                    }
                }
            }
        }
    }

    private fun launchBarcodeScanner() {
            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
            options.setPrompt("Scan a Qr Code")
            options.captureActivity = CaptureActivityPortrait::class.java
            options.setBeepEnabled(true)
            barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
//            Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show()
        } else {

            homeViewModel.getStationInfo(result.contents)
            Preferences.saveData(AppConstants.STATION_ID, "0xb8E506fF8F26E9517b158F5Ac9dCD1bc4D29E890")

//            launchChargingInfoFragment()
        }
    }

    private fun launchChargingInfoFragment(stationInfo: StationInfo) {
        sharedViewModel.showBottomNavigation.value = false

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, StationInfoChargingFragment.newInstance(stationInfo.station_data!!), "Station")
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()

        requireActivity().viewModelStore.clear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}