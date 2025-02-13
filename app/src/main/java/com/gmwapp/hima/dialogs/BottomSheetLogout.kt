package com.gmwapp.hima.dialogs

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.LoginActivity
import com.gmwapp.hima.activities.NewLoginActivity
import com.gmwapp.hima.databinding.BottomSheetLogoutBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.onesignal.OneSignal
import com.tencent.mmkv.MMKV
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService


class BottomSheetLogout : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetLogoutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetLogoutBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    private fun initUI() {
        binding.btnLogout.setOnSingleClickListener({
            MMKV.defaultMMKV().remove("user_id");
            MMKV.defaultMMKV().remove("user_name");
            OneSignal.User.removeTag("gender_language") // Clears the tag on logout
            OneSignal.User.removeTag("gender") // Clears the tag on logout
            OneSignal.User.removeTag("language") // Clears the tag on logout

            ZegoUIKitPrebuiltCallService.unInit()
            val prefs = BaseApplication.getInstance()?.getPrefs()
            prefs?.clearUserData()
            val intent = Intent(context, NewLoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        })
    }
}