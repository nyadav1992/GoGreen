package com.example.gogreen.ui.payconfirm

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.gogreen.MainActivity
import com.example.gogreen.databinding.FragmentPayConfirmationBinding

class PayConfirmationFragment : Fragment() {
    private var _binding: FragmentPayConfirmationBinding? = null

    private val binding get() = _binding!!

    companion object {
        fun newInstance() = PayConfirmationFragment()
    }

    private lateinit var viewModel: PayConfirmatationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[PayConfirmatationViewModel::class.java]

        _binding = FragmentPayConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHome.setOnClickListener {
            startActivity(Intent(requireActivity(), MainActivity::class.java))
            requireActivity().finish()
        }
    }

}