package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.AvatarsListAdapter
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivitySelectGenderBinding
import com.gmwapp.hima.viewmodels.ProfileViewModel
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

    private fun initUI() {
        binding.cvGender.setBackgroundResource(R.drawable.d_button_bg_gender_outline)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvAvatars)
        setCenterLayoutManager(binding.rvAvatars)
        binding.ivBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        binding.btnContinue.setOnClickListener {
            var intent:Intent? = null
            if (selectedGender == DConstants.MALE) {
                intent = Intent(this, SelectLanguageActivity::class.java)
            } else {
                intent = Intent(this, FemaleAboutActivity::class.java)
            }
            val layoutManager = binding.rvAvatars.layoutManager as CenterLayoutManager
            val avatarId =
                profileViewModel.avatarsListLiveData.value?.data?.get(layoutManager.findFirstCompletelyVisibleItemPosition())?.id
            intent.putExtra(DConstants.AVATAR_ID, avatarId)
            intent.putExtra(
                DConstants.MOBILE_NUMBER, getIntent().getStringExtra(DConstants.MOBILE_NUMBER)
            )
            intent.putExtra(DConstants.GENDER, selectedGender)
            startActivity(intent)

        }
        binding.btnMale.setOnClickListener {
            selectedGender = DConstants.MALE
            profileViewModel.getAvatarsList(selectedGender)
            binding.btnMale.setBackgroundResource(R.drawable.d_button_bg_gender_selected)
            binding.btnFemale.setBackgroundColor(getColor(android.R.color.transparent))
            binding.btnMale.isEnabled = false
            binding.btnFemale.isEnabled = true
        }
        binding.btnFemale.setOnClickListener {
            selectedGender = DConstants.FEMALE
            profileViewModel.getAvatarsList(selectedGender)
            binding.btnMale.setBackgroundColor(getColor(android.R.color.transparent))
            binding.btnFemale.setBackgroundResource(R.drawable.d_button_bg_gender_selected)
            binding.btnMale.isEnabled = true
            binding.btnFemale.isEnabled = false
        }
        profileViewModel.getAvatarsList("male")
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if (it.data != null) {
                val avatarsListAdapter = AvatarsListAdapter(
                    this, it.data
                )
                binding.rvAvatars.setAdapter(avatarsListAdapter)
                binding.rvAvatars.smoothScrollToPosition(0)
            }
        })

    }

}