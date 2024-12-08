package com.gmwapp.hima.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.BuildConfig
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.AccountPrivacyActivity
import com.gmwapp.hima.activities.EditProfileActivity
import com.gmwapp.hima.activities.TransactionsActivity
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.databinding.FragmentProfileBinding
import com.gmwapp.hima.dialogs.BottomSheetLogout
import com.gmwapp.hima.viewmodels.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {
    lateinit var binding: FragmentProfileBinding
    private val EDIT_PROFILE_REQUEST_CODE = 1
    private val accountViewModel: AccountViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        initUI()
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == EDIT_PROFILE_REQUEST_CODE){
            updateValues()
        }
    }

    private fun updateValues(){
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        binding.tvName.text = userData?.name
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val supportMail = prefs?.getSettingsData()?.support_mail
        val subject = getString(R.string.delete_account_mail_subject, userData?.mobile, userData?.language)

        val body = ""
        binding.tvSupportMail.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)

            val data = Uri.parse(("mailto:$supportMail?subject=$subject").toString() + "&body=$body")
            intent.setData(data)

            startActivity(intent)
        }
        binding.tvSupportMail.paintFlags =
            binding.tvSupportMail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.tvSupportMail.text =
            supportMail
        Glide.with(this).load(userData?.image).
        apply(RequestOptions.circleCropTransform()).into(binding.ivProfile)
    }

    private fun initUI(){
        updateValues()

        val prefs = BaseApplication.getInstance()?.getPrefs()
        binding.clWallet.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        binding.ivEditProfile.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
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
                    "ProfileFragment"
                )
            }
        })
        accountViewModel.getSettings()
        accountViewModel.settingsLiveData.observe(viewLifecycleOwner, Observer {
            if (it.success) {
                if (it.data != null) {
                    if (it.data.size > 0) {
                        prefs?.setSettingsData(it.data.get(0))
                        val supportMail = prefs?.getSettingsData()?.support_mail
                        binding.tvSupportMail.text =
                            supportMail
                        val userData = prefs?.getUserData()
                        val subject = getString(R.string.delete_account_mail_subject, userData?.mobile,  userData?.language)

                        val body = ""
                        binding.tvSupportMail.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)

                            val data = Uri.parse(("mailto:$supportMail?subject=$subject").toString() + "&body=$body")
                            intent.setData(data)

                            startActivity(intent)
                        }
                        binding.tvSupportMail.paintFlags =
                            binding.tvSupportMail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    }
                }
            }
        })
    }
}