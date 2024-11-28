package com.gmwapp.dostt.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.adapters.AvatarsListAdapter
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding
import com.gmwapp.dostt.databinding.ActivitySelectGenderBinding
import com.gmwapp.dostt.viewmodels.LoginViewModel
import com.gmwapp.dostt.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectGenderActivity : BaseActivity() {
    lateinit var binding: ActivitySelectGenderBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var selectedGender = "male"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectGenderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI(){
        binding.cvGender.setBackgroundResource(R.drawable.d_button_bg_gender_outline);
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvAvatars)
        setCenterLayoutManager(binding.rvAvatars)
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, SelectLanguageActivity::class.java)
            val layoutManager = binding.rvAvatars.layoutManager as CenterLayoutManager
            val avatarId = profileViewModel.avatarsListLiveData.value?.data?.get(layoutManager.findFirstCompletelyVisibleItemPosition())?.id
            intent.putExtra(DConstants.AVATAR_ID, avatarId)
            intent.putExtra(DConstants.MOBILE_NUMBER, getIntent().getStringExtra(DConstants.MOBILE_NUMBER))
            intent.putExtra(DConstants.GENDER, selectedGender)
            startActivity(intent)

        }
        binding.btnMale.setOnClickListener {
            selectedGender = "male"
            profileViewModel.getAvatarsList("male")
            binding.btnMale.setBackgroundResource(R.drawable.d_button_bg_gender_selected)
            binding.btnFemale.setBackgroundColor(getColor(android.R.color.transparent))
            binding.btnMale.isEnabled = false
            binding.btnFemale.isEnabled = true
        }
        binding.btnFemale.setOnClickListener {
            selectedGender = "female"
            profileViewModel.getAvatarsList("female")
            binding.btnMale.setBackgroundColor(getColor(android.R.color.transparent))
            binding.btnFemale.setBackgroundResource(R.drawable.d_button_bg_gender_selected)
            binding.btnMale.isEnabled = true
            binding.btnFemale.isEnabled = false
        }
        profileViewModel.getAvatarsList("male")
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if(it.data != null) {
                it.data.add(0, null);
                it.data.add(it.data.size, null);
                val avatarsListAdapter = AvatarsListAdapter(
                    this,
                    it.data
                )
                binding.rvAvatars.setAdapter(avatarsListAdapter)
                binding.rvAvatars.smoothScrollToPosition(1);
            }
        })

    }

}