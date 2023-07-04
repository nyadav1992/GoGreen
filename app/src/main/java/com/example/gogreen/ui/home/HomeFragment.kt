package com.example.gogreen.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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
import kotlinx.coroutines.Job
import org.json.JSONException

import org.json.JSONObject





@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var job: Job
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
        Preferences.saveData(AppConstants.STATION_ID, "0xfDb51BEC9453E011780000bbd5257397AB78c452")

        binding.ivBarcode.setOnClickListener {

//            launchChargingInfoFragment()
//            binding.pBar.visibility = View.VISIBLE
//            job = homeViewModel.getStationInfo("1")
//            observeStationInfoData()
//            homeViewModel.getStationInfo("0xfDb51BEC9453E011780000bbd5257397AB78c452")



            launchBarcodeScanner()

        }

    }

    private fun observeStationInfoData() {
        homeViewModel.stationData.observeOnce(this){
            it?.let {
                binding.pBar.visibility = View.GONE
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

        val scannedQrCode = "{\"station-id\":\"Nissan Urvan Original 12v\",\"price\":\"2500\"}"
        try {
            val mainObject = JSONObject(scannedQrCode)
            Log.e("TESTING", "" + mainObject.get("name") + mainObject.get("price"))
        } catch (jsonException: JSONException) {
            jsonException.printStackTrace()
        }

            val options = ScanOptions()
            options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            options.setPrompt("Scan a Qr Code")
            options.captureActivity = CaptureActivityPortrait::class.java
            options.setBeepEnabled(true)
            barcodeLauncher.launch(options)
    }

    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(requireActivity(), "No data capture!!, Please Scan Again", Toast.LENGTH_LONG).show()
        } else {
            binding.pBar.visibility = View.VISIBLE
            try {
                val mainObject = JSONObject(result.contents.toString())
                val id:String = mainObject.get("station-id").toString()
                Preferences.saveData(AppConstants.STATION_ID, id)
                job = homeViewModel.getStationInfo(id)
                Toast.makeText(requireActivity(), id, Toast.LENGTH_LONG).show()
            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
                Toast.makeText(requireActivity(), jsonException.toString(), Toast.LENGTH_LONG).show()
            }
            observeStationInfoData()

//            launchChargingInfoFragment()
        }
    }

    private fun launchChargingInfoFragment(stationInfo: StationInfo) {
        sharedViewModel.showBottomNavigation.value = false

        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.add(R.id.container, StationInfoChargingFragment.newInstance(stationInfo.station_data!!), "Station")
        transaction.addToBackStack(null)
        transaction.commitAllowingStateLoss()

        job.cancel()
    }

    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
        observe(owner, object: Observer<T> {
            override fun onChanged(value: T) {
                removeObserver(this)
                observer(value)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}