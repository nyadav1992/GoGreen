package com.example.gogreen.ui.stationinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.gogreen.MainActivity
import com.example.gogreen.R
import com.example.gogreen.data.StartChargingRequest
import com.example.gogreen.data.StationData
import com.example.gogreen.databinding.FragmentStationInfoChargingBinding
import com.example.gogreen.dialog.IssuerInfoDialog
import com.example.gogreen.ui.payconfirm.PayConfirmationFragment
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat

@AndroidEntryPoint
class StationInfoChargingFragment(stationInfo: StationData) : Fragment(), OnClickListener {
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
                binding.tvCostValueEdit.text = getString(R.string.rs) + " 0.00"
                return@addTextChangedListener
            } else binding.btnSubmit.isEnabled = it.toString().toDouble() > 0

            price = it.toString().toDouble() * 17
            energy = it.toString().toDouble()

            binding.tvCostValueEdit.text =
                getString(R.string.rs) + " " + dfZero.format(price).toString()
        }
        binding.etCostValueEdit.addTextChangedListener {
            if (it.toString().isEmpty()) {
                binding.btnSubmit.isEnabled = false
                binding.tvEnergyValueEdit.text = "0 " + getString(R.string.kwh)
                return@addTextChangedListener
            } else binding.btnSubmit.isEnabled = it.toString().toDouble() > 0
            energy = it.toString().toDouble() / 17
            price = it.toString().toDouble()

            binding.tvEnergyValueEdit.text =
                dfZero.format(energy).toString() + " " + getString(R.string.kwh)
        }
    }

    private fun showSoftKeyboard(etView: EditText) {
        val imm = getSystemService(requireActivity(), InputMethodManager::class.java) as InputMethodManager?
        imm!!.showSoftInput(etView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun hideSoftKeyboard() {
        val imm = getSystemService(requireActivity(), InputMethodManager::class.java) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(binding.etEnergyValueEdit.windowToken, InputMethodManager.SHOW_IMPLICIT)
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

        binding.tvTitleValue.text = stationInfoData.available_energy.toString() + ""
        binding.tvAddress.text = stationInfoData.station_name + " " + stationInfoData.station_address
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
                when (binding.btnSubmit.text){
                    getString(R.string.startCharging) -> {
                        startCharging()
                    }
                    getString(R.string.stopCharging) -> {
                        startChargingUiSetup()
                        binding.tvTitle.text = getString(R.string.charging_done)
                        binding.ivChargeDone.visibility = View.VISIBLE
                        binding.btnSubmit.text = getString(R.string.pay)
                    }
                    getString(R.string.pay) -> {
                        requireActivity().supportFragmentManager.popBackStack()
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()
                        transaction.add(R.id.container, PayConfirmationFragment(), "Station")
                        transaction.addToBackStack(null)
                        transaction.commitAllowingStateLoss()
                    }
                }

            }
            binding.ivIssuerInfo -> {
                IssuerInfoDialog().show(childFragmentManager, "IssuerDialog")
            }
        }
    }

    private fun startCharging() {
        val startChargingRequest = StartChargingRequest("1", energy.toInt(), stationInfoData.id)
        viewModel.getStationInfo(startChargingRequest)

        observeChargingData()
    }

    private fun observeChargingData() {
        viewModel.chargingStatusData.observe(this){
            println(it.message)
            startChargingUiSetup()
        }
    }

    private fun startChargingUiSetup() {
        binding.llEnergyInfoEdit.visibility = View.GONE
        binding.llCostInfoEdit.visibility = View.GONE
        binding.llEnergyInfo.visibility = View.VISIBLE
        binding.llTimeInfo.visibility = View.VISIBLE
        binding.llCostInfo.visibility = View.VISIBLE
        binding.ivChargeDone.visibility = View.GONE
        binding.tvTitleValue.visibility = View.GONE
        binding.tvTitle.text = getString(R.string.charging_continued)
        binding.tvPercent.text = "75%"
        binding.progressCircle.progress = 75
        binding.btnSubmit.text = getString(R.string.stopCharging)
    }

}