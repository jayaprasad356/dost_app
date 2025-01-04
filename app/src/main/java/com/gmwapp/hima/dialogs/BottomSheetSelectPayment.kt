package com.gmwapp.hima.dialogs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.WithdrawActivity
import com.gmwapp.hima.databinding.BottomSheetSelectPaymentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetSelectPayment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetSelectPaymentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetSelectPaymentBinding.inflate(inflater, container, false)

        setupListeners()
        setDefaultSelection() // Set default selection here
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    private fun setupListeners() {
        // Set up listeners for the radio buttons
        binding.rbUpi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnSelectPayment.isEnabled = true
                binding.rbBank.isChecked = false
            }
        }

        binding.rbBank.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.btnSelectPayment.isEnabled = true
                binding.rbUpi.isChecked = false
            }
        }

        // Set up the select button click listener
        binding.btnSelectPayment.setOnClickListener {
            val selectedOption = when {
                binding.rbUpi.isChecked -> "upi_transfer"
                binding.rbBank.isChecked -> "bank_transfer"
                else -> "No Option Selected"
            }

            val intent = Intent(requireContext(), WithdrawActivity::class.java)

            // Add optional extras if needed
            intent.putExtra("payment_method", selectedOption)

            startActivity(intent)

            // Close the bottom sheet
            dismiss()
        }
    }

    private fun setDefaultSelection() {
        // Set UPI as the default selected option
        binding.rbUpi.isChecked = true
        binding.btnSelectPayment.isEnabled = true // Enable the button since a default is selected
    }
}
