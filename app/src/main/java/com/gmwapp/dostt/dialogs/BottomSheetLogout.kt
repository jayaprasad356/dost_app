package com.gmwapp.dostt.dialogs

import android.annotation.SuppressLint
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.dostt.databinding.BottomSheetLogoutBinding
import com.gmwapp.dostt.databinding.BottomSheetWelcomeBonusBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationBarView


class BottomSheetLogout : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetLogoutBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLogoutBinding.inflate(layoutInflater)

        return binding.root
    }

}