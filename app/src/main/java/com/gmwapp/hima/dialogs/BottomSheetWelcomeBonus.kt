package com.gmwapp.hima.dialogs

import android.app.Dialog
import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.databinding.BottomSheetWelcomeBonusBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.OfferViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetWelcomeBonus(
    private val coins: Int,
    private val price: Int,
    private val save: Int
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetWelcomeBonusBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWelcomeBonusBinding.inflate(inflater, container, false)

        // Setting the strikethrough effect
        binding.tvBonusOriginal.paintFlags =
            binding.tvBonusOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        // Set text views with the provided data
        binding.tvBonusText.text = "$coins Coins"
        binding.tvBonusOriginal.text =  "$price"
        binding.tvBonusDiscount.text =  "$save"

        // Button click listeners
        binding.tvViewMorePlans.setOnSingleClickListener {
            startActivity(Intent(context, WalletActivity::class.java))
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }
}
