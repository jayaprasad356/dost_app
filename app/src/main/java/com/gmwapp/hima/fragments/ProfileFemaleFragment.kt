package com.gmwapp.hima.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.bitmap.TransformationUtils.circleCrop
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.activities.AccountPrivacyActivity
import com.gmwapp.hima.activities.EditProfileActivity
import com.gmwapp.hima.activities.TransactionsActivity
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.databinding.FragmentProfileBinding
import com.gmwapp.hima.databinding.FragmentProfileFemaleBinding
import com.gmwapp.hima.dialogs.BottomSheetLogout

class ProfileFemaleFragment : BaseFragment() {
    lateinit var binding: FragmentProfileFemaleBinding
    private val EDIT_PROFILE_REQUEST_CODE = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileFemaleBinding.inflate(layoutInflater)
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
        Glide.with(this).load(userData?.image).
        apply(RequestOptions.circleCropTransform()).into(binding.ivProfile)
    }

    private fun initUI(){
        updateValues()

        binding.clEarnings.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        binding.ivEditProfile.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, EditProfileActivity::class.java)
            startActivityForResult(intent, EDIT_PROFILE_REQUEST_CODE)
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
    }
}