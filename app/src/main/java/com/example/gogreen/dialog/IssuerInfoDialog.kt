package com.example.gogreen.dialog

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.gogreen.R
import com.example.gogreen.data.VerifyData
import com.example.gogreen.databinding.DialogEvInfoBinding

class IssuerInfoDialog(data: VerifyData?) : DialogFragment() {
    private var verifyData = data
    private lateinit var binding: DialogEvInfoBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.round_corner);
        binding = DialogEvInfoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvCertificateValue.text = verifyData!!.certificateCID
        binding.ivClose.setOnClickListener {
            this.dismiss()
        }
        binding.tvLinkOne.setOnClickListener{
            val url = verifyData!!.txn_link
            val urlIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(urlIntent)
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }
}