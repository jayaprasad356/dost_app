package com.gmwapp.hima.dialogs

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.databinding.BottomSheetWelcomeBonusBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetWelcomeBonus : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetWelcomeBonusBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetWelcomeBonusBinding.inflate(layoutInflater)
        binding.tvBonusOriginal.paintFlags = binding.tvBonusOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        return binding.root
    }

}