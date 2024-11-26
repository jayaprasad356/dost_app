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
        profileViewModel.getAvatarsList("male")
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if(it.data != null) {
                it.data.add(it.data.size, null);
                it.data.add(0, null);

                var avatarsListAdapter = AvatarsListAdapter(
                    this,
                    it.data,
                    1
                )
                binding.rvAvatars.setAdapter(avatarsListAdapter)
            }
            binding.rvAvatars.smoothScrollToPosition(1);
        })

    }

}