package com.gmwapp.dostt.fragments

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.activities.AccountPrivacyActivity
import com.gmwapp.dostt.activities.EditProfileActivity
import com.gmwapp.dostt.activities.TransactionsActivity
import com.gmwapp.dostt.activities.VerifyOTPActivity
import com.gmwapp.dostt.activities.WalletActivity
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.databinding.FragmentHomeBinding
import com.gmwapp.dostt.databinding.FragmentProfileBinding
import com.gmwapp.dostt.dialogs.BottomSheetLogout
import com.gmwapp.dostt.dialogs.BottomSheetWelcomeBonus


class ProfileFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)

        binding.tvName.setText(BaseApplication.getInstance()?.getPrefs()?.getUserData()?.name)
        binding.clWallet.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        binding.ivEditProfile.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivity(intent)
        })
        binding.clTransactions.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, TransactionsActivity::class.java)
            startActivity(intent)
        })
        binding.clAccountPrivacy.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, AccountPrivacyActivity::class.java)
            startActivity(intent)
        })
        binding.cvLogout.setOnClickListener(View.OnClickListener {
            val bottomSheet: BottomSheetLogout =
                BottomSheetLogout()
            fragmentManager?.let { it1 ->
                bottomSheet.show(
                    it1,
                    "BottomSheetLogout"
                )
            }
        })

        return binding.root
    }
}