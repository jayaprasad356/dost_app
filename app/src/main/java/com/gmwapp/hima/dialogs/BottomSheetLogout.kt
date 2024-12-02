package com.gmwapp.hima.dialogs

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.activities.LoginActivity
import com.gmwapp.hima.databinding.BottomSheetLogoutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class BottomSheetLogout : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetLogoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLogoutBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }

    private fun initUI() {
        val prefs = BaseApplication.getInstance()?.getPrefs()
        prefs?.clearUserData()
        val intent = Intent(context, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)

    }
}