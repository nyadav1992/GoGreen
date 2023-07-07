package com.example.gogreen.ui.stationinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.gogreen.MainActivity
import com.example.gogreen.R
import com.example.gogreen.data.PayChargingRequest
import com.example.gogreen.data.StartChargingRequest
import com.example.gogreen.data.StartChargingResponse
import com.example.gogreen.data.StationData
import com.example.gogreen.data.StopChargingRequest
import com.example.gogreen.data.StopChargingResponse
import com.example.gogreen.databinding.FragmentStationInfoChargingBinding
import com.example.gogreen.dialog.IssuerInfoDialog
import com.example.gogreen.dialog.PayConfirmDialog
import com.example.gogreen.ui.home.AppConstants
import com.example.gogreen.utils.Preferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import java.text.DecimalFormat

@AndroidEntryPoint
class StationInfoChargingFragment(stationInfo: StationData) : Fragment(), OnClickListener {
    private lateinit var stopChargingJob: Job
    private lateinit var payChargingJob: Job
    private lateinit var stopChargingResponse: StopChargingResponse
    private lateinit var startChargingJob: Job
    private lateinit var verifyIssuerJob: Job
    private lateinit var statusJob: Job
    private var stationInfoData: StationData = stationInfo

    private var energy: Double = 0.00
    private var price: Double = 0.00
    private var _binding: FragmentStationInfoChargingBinding? = null
    private val dfZero: DecimalFormat = DecimalFormat("0.00")

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    companion object {
        fun newInstance(stationInfo: StationData) = StationInfoChargingFragment(stationInfo)
    }

    private val viewModel: StationInfoChargingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStationInfoChargingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity).supportActionBar!!.hide()

        binding.ivBack.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.ivIssuerInfo.setOnClickListener(this)

        initialViewSetup()

        calculateEnteredValue()
    }

    private fun calculateEnteredValue() {

        binding.tvEnergyValueEdit.setOnTouchListener { view, motionEvent ->
            binding.tvEnergyValueEdit.visibility = View.GONE
            binding.tvCostValueEdit.visibility = View.VISIBLE
            binding.etCostValueEdit.isEnabled = false
            binding.etEnergyValueEdit.isEnabled = true
            binding.etEnergyValueEdit.requestFocus()

            if (energy.equals(0.0))
                binding.etEnergyValueEdit.setText("")
            else
                binding.etEnergyValueEdit.setText(dfZero.format(energy))
            showSoftKeyboard(binding.etEnergyValueEdit)
            true
        }

        binding.tvCostValueEdit.setOnTouchListener { view, motionEvent ->
            binding.tvEnergyValueEdit.visibility = View.VISIBLE
            binding.tvCostValueEdit.visibility = View.GONE
            binding.etCostValueEdit.isEnabled = true
            binding.etEnergyValueEdit.isEnabled = false
            binding.etCostValueEdit.requestFocus()

            if (price.equals(0.0))
                binding.etCostValueEdit.setText("")
            else
                binding.etCostValueEdit.setText(dfZero.format(price))

            showSoftKeyboard(binding.etCostValueEdit)
            true
        }

        binding.etEnergyValueEdit.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.btnSubmit.isEnabled = false
                binding.tvCostValueEdit.text = getString(R.string.eu) + " 0.00"
                return@addTextChangedListener
            } else binding.btnSubmit.isEnabled = it.toString().toDouble() > 0

            price = it.toString().toDouble() * stationInfoData.energy_rate
            energy = it.toString().toDouble()

            binding.tvCostValueEdit.text =
                getString(R.string.eu) + " " + dfZero.format(price).toString()
        }
        binding.etCostValueEdit.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.btnSubmit.isEnabled = false
                binding.tvEnergyValueEdit.text = "0 " + Preferences.getData(AppConstants.ENERGY_UNIT,"")
                return@addTextChangedListener
            } else binding.btnSubmit.isEnabled = it.toString().toDouble() > 0
            energy = it.toString().toDouble() / stationInfoData.energy_rate
            price = it.toString().toDouble()

            binding.tvEnergyValueEdit.text =
                dfZero.format(energy).toString() + " " + Preferences.getData(AppConstants.ENERGY_UNIT,"")
        }
    }

    private fun showSoftKeyboard(etView: EditText) {
        val imm = getSystemService(
            requireActivity(),
            InputMethodManager::class.java
        ) as InputMethodManager?
        imm!!.showSoftInput(etView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(
            requireActivity(),
            InputMethodManager::class.java
        ) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(
            binding.etEnergyValueEdit.windowToken,
            InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun initialViewSetup() {
        binding.llEnergyInfoEdit.visibility = View.VISIBLE
        binding.llCostInfoEdit.visibility = View.VISIBLE
        binding.llEnergyInfo.visibility = View.GONE
        binding.llTimeInfo.visibility = View.GONE
        binding.llCostInfo.visibility = View.GONE
        binding.ivChargeDone.visibility = View.GONE
        binding.btnSubmit.text = getString(R.string.startCharging)
        binding.btnSubmit.isEnabled = false
        binding.tvEnergyValueEdit.hint = "Energy in " + stationInfoData.energy_unit
        binding.tvCostValueEdit.hint =
            "1 " + stationInfoData.energy_unit + " = " + getString(R.string.eu) + stationInfoData.energy_rate
        binding.tvTitleValue.text =
            " " + stationInfoData.available_energy.toString() + " " + stationInfoData.energy_unit
        binding.tvAddress.text =
            stationInfoData.station_name + "\n" + stationInfoData.station_address
    }

    override fun onDetach() {
        super.onDetach()
        requireActivity().viewModelStore.clear()
    }

    override fun onClick(p0: View?) {
        hideSoftKeyboard()
        when (p0) {
            binding.ivBack -> {
                (activity as MainActivity).onBackPressed()
            }

            binding.btnSubmit -> {
                when (binding.btnSubmit.text) {
                    getString(R.string.startCharging) -> {
                        startCharging()
                    }

                    getString(R.string.stopCharging) -> {
//                        startChargingUiSetup(it)
                        statusJob.cancel()
                        startChargingJob.cancel()
                        stopCharging()
                    }

                    getString(R.string.pay) -> {
                        startChargingJob.cancel()
                        payForCharging()
//                        showPayDialog(stopChargingResponse)
                    }
                }

            }

            binding.ivIssuerInfo -> {
                verifyIssuer()
            }
        }
    }

    private fun verifyIssuer() {
        binding.pBar.visibility = View.VISIBLE
        binding.pBar.isClickable = true
        binding.etCostValueEdit.isEnabled = false
        binding.etEnergyValueEdit.isEnabled = false

        verifyIssuerJob = viewModel.verifyIssuer(Preferences.getData(AppConstants.STATION_ID,"")!!)

        observeVerifyData()
    }

    private fun startCharging() {
        binding.pBar.visibility = View.VISIBLE
        binding.pBar.isClickable = true
        binding.etCostValueEdit.isEnabled = false
        binding.etEnergyValueEdit.isEnabled = false
        val startChargingRequest =
            StartChargingRequest(
                Preferences.getData(AppConstants.STATION_ID,"")!!,
                Preferences.getData(AppConstants.WALLET_ADDRESS, "")!!,
                energy.toDouble()
            )
        startChargingJob = viewModel.startCharging(startChargingRequest)

        observeChargingData()
    }

    private fun stopCharging() {
        binding.pBar.visibility = View.VISIBLE
        binding.pBar.isClickable = true
        val stopChargingRequest =
            StopChargingRequest(
                Preferences.getData(AppConstants.STATION_ID,"")!!,
                Preferences.getData(AppConstants.WALLET_ADDRESS, "")!!
            )
        stopChargingJob = viewModel.stopCharging(stopChargingRequest)
        observeStopCharging()
    }

    private fun payForCharging() {
        binding.pBar.visibility = View.VISIBLE
        binding.pBar.isClickable = true
        val payChargingRequest =
            PayChargingRequest(
                stopChargingResponse.invoice!!.order_id,
                true,
                Preferences.getData(AppConstants.WALLET_ADDRESS, "")!!
            )
        payChargingJob = viewModel.payCharging(payChargingRequest)
        observePayCharging()
    }

    private fun observeVerifyData() {
        viewModel.verifyIssuerData.observe(this) {
            binding.pBar.visibility = View.GONE
            binding.pBar.isClickable = false
            it?.let {
                when (it.status) {
                    true -> {
                        IssuerInfoDialog(it.data).show(childFragmentManager, "IssuerDialog")
                    }

                    else -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observeChargingData() {
        viewModel.chargingStatusData.observe(this) {

            it?.let {
                when (it.status) {
                    true -> {
                        println(it.message)
                        savePreferences(it)
                        startChargingUiSetup(it)
                        hideProgressBar()
                        println("success")
                        callProgressApi(it)

                    }

                    else -> {
//                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }


        }
    }

    private fun observeStopCharging() {
        viewModel.chargingStopData.observe(viewLifecycleOwner) {
            it?.let {
                binding.pBar.visibility = View.GONE
                when (it.status) {
                    true -> {
                        stopChargingResponse = it
                        binding.tvTitle.text = getString(R.string.charging_done)
                        binding.ivChargeDone.visibility = View.VISIBLE
                        binding.btnSubmit.text = getString(R.string.pay)
                    }

                    else -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun observePayCharging() {
        viewModel.chargingPayData.observe(viewLifecycleOwner) {
            it?.let {
                binding.pBar.visibility = View.GONE
                when (it.status) {
                    true -> {
                        Preferences.saveData(AppConstants.CHARGE_START, false)
                        showPayDialog(it)
                    }

                    else -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showPayDialog(stopChargingResponse: StopChargingResponse) {
        PayConfirmDialog(stopChargingResponse).show(childFragmentManager, "IssuerDialog")
    }

    private fun callProgressApi(startChargingResponse: StartChargingResponse) {
        statusJob =
            viewModel.getProgress(Preferences.getData(AppConstants.WALLET_ADDRESS, "")!!, Preferences.getData(AppConstants.STATION_ID,"")!!)

        if (startChargingResponse.energyConsumed!! >= startChargingResponse.requiredEnergy!!) {
            stopCharging()
            statusJob.cancel()
        }
    }

    private fun hideProgressBar() {
        binding.pBar.visibility = View.GONE
        binding.pBar.isClickable = false
        binding.etCostValueEdit.isEnabled = true
        binding.etEnergyValueEdit.isEnabled = true
    }

    private fun savePreferences(startChargingResponse: StartChargingResponse) {
        Preferences.saveData(AppConstants.ENERGY_UNIT, stationInfoData.energy_unit)
        Preferences.saveData(AppConstants.CHARGE_START, true)
        Preferences.saveData(
            AppConstants.REQUIRED_ENERGY,
            startChargingResponse.requiredEnergy.toString()
        )
        Preferences.saveData(AppConstants.ENERGY_RATE, stationInfoData.energy_rate.toString())
    }

    private fun startChargingUiSetup(startChargingResponse: StartChargingResponse) {
        binding.llEnergyInfoEdit.visibility = View.GONE
        binding.llCostInfoEdit.visibility = View.GONE
        binding.llEnergyInfo.visibility = View.VISIBLE
        binding.llTimeInfo.visibility = View.VISIBLE
        binding.llCostInfo.visibility = View.VISIBLE
        binding.ivChargeDone.visibility = View.GONE
        binding.tvTitleValue.visibility = View.GONE
        binding.ivBack.visibility = View.GONE
        binding.rlTopAddress.visibility = View.GONE
        binding.tvTitle.text = getString(R.string.charging_continued)

        binding.tvEnergyValue.text =
            startChargingResponse.energyConsumed.toString() + " " + Preferences.getData(
                AppConstants.ENERGY_UNIT,
                ""
            )
        var timeString = "0"
        if (startChargingResponse.timePassed!! > 0) {
            val totalSecs = startChargingResponse.timePassed!! / 1000
            //        val hours = totalSecs / 3600;
//        val minutes = (totalSecs % 3600) / 60;
            val minutes = (totalSecs) / 60;
            val seconds = totalSecs % 60;

            if (minutes == 0)
                timeString = String.format("%02d Sec", seconds)
            else
                timeString = String.format("%02d Min %02d Sec ", minutes, seconds)

        }

        binding.tvTimeValue.text = timeString
        if (startChargingResponse.price == 0.0)
            binding.tvCostValue.text =
                getString(R.string.eu)+" " + dfZero.format((startChargingResponse.totalPrice!! * startChargingResponse.energyConsumed!!)).toString()
        else
            binding.tvCostValue.text =
                getString(R.string.eu)+" " + dfZero.format(startChargingResponse.price!! * startChargingResponse.energyConsumed!!).toString()

        val percent: Double =
            (startChargingResponse.energyConsumed!!.toDouble() / startChargingResponse.requiredEnergy!!.toDouble()) * 100

        binding.tvPercent.text = percent.toInt().toString() + "%"
        binding.progressCircle.progress = percent.toInt()
        binding.btnSubmit.text = getString(R.string.stopCharging)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}