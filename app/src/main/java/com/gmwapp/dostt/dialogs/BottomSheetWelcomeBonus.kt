package com.gmwapp.dostt.dialogs

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.dostt.databinding.BottomSheetWelcomeBonusBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationBarView


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