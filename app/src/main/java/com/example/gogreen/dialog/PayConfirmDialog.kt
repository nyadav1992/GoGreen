package com.example.gogreen.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gogreen.R
import com.example.gogreen.data.Invoice
import com.example.gogreen.data.StopChargingResponse
import com.example.gogreen.databinding.DialogPaidBinding
import com.example.gogreen.ui.payconfirm.PayConfirmationFragment

class PayConfirmDialog(stopChargingResponse: StopChargingResponse?) : DialogFragment() {
    private val invoiceData: Invoice? = stopChargingResponse?.invoice
    private lateinit var binding: DialogPaidBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        binding = DialogPaidBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val totalSecs = invoiceData!!.energyTransferTimeInSeconds/1000
//        val hours = totalSecs / 3600;
//        val minutes = (totalSecs % 3600) / 60;
        val minutes = (totalSecs) / 60;
        val seconds = totalSecs % 60;

        val timeString: String = String.format("%02d Min %02d Sec ", minutes, seconds)

        binding.tvOrderId.text = invoiceData!!.order_id
        binding.tvTimeValue.text = timeString
        binding.tvCostValue.text = "Rs " + invoiceData!!.total_cost
        binding.tvTxnId.text = invoiceData.txn_id

        binding.btnHome.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
            val transaction =
                requireActivity().supportFragmentManager.beginTransaction()
            transaction.add(R.id.container, PayConfirmationFragment.newInstance(invoiceData), "Station")
            transaction.addToBackStack(null)
            transaction.commitAllowingStateLoss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}