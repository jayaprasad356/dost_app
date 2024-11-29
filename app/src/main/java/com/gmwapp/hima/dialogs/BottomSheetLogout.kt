package com.gmwapp.hima.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.databinding.BottomSheetLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


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