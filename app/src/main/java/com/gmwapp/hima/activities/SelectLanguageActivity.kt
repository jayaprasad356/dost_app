package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.LanguageAdapter
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivitySelectLanguageBinding
import com.gmwapp.hima.retrofit.responses.Language
import com.gmwapp.hima.viewmodels.ProfileViewModel
import com.gmwapp.hima.widgets.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SelectLanguageActivity : BaseActivity() {
    lateinit var binding: ActivitySelectLanguageBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var selectedLanguage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val layoutManager = GridLayoutManager(this, 2)
        binding.rvLanguages.addItemDecoration(SpacesItemDecoration(20))
        binding.ivBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        profileViewModel.registerErrorLiveData.observe(this, Observer {
            Toast.makeText(this@SelectLanguageActivity, it, Toast.LENGTH_LONG).show()
        })
        profileViewModel.registerLiveData.observe(this, Observer {
            if (it.success) {
                BaseApplication.getInstance()?.getPrefs()?.setUserData(it.data)
                if (it.data?.gender == DConstants.MALE) {
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(
                        DConstants.AVATAR_ID, getIntent().getIntExtra(DConstants.AVATAR_ID, 0)
                    )
                    intent.putExtra(DConstants.LANGUAGE, selectedLanguage)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    if(it.data?.status == 2){
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra(
                            DConstants.AVATAR_ID, getIntent().getIntExtra(DConstants.AVATAR_ID, 0)
                        )
                        intent.putExtra(DConstants.LANGUAGE, selectedLanguage)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else if(it.data?.status == 1){
                        val intent = Intent(this, AlmostDoneActivity::class.java)
                        startActivity(intent)
                    } else{
                        val intent = Intent(this, VoiceIdentificationActivity::class.java)
                        intent.putExtra(DConstants.LANGUAGE, selectedLanguage)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    }
                }
            } else {
                Toast.makeText(this@SelectLanguageActivity, it.message, Toast.LENGTH_LONG).show()
            }
        })
        binding.btnContinue.setOnClickListener {
            val gender = intent.getStringExtra(DConstants.GENDER).toString()
            if (gender == DConstants.MALE) {
                profileViewModel.register(
                    intent.getStringExtra(DConstants.MOBILE_NUMBER).toString(),
                    selectedLanguage.toString(),
                    intent.getIntExtra(DConstants.AVATAR_ID, 0),
                    gender,

                    )
            } else {
                profileViewModel.registerFemale(
                    intent.getStringExtra(DConstants.MOBILE_NUMBER).toString(),
                    selectedLanguage.toString(),
                    intent.getIntExtra(DConstants.AVATAR_ID, 0),
                    gender,
                    intent.getStringExtra(DConstants.AGE).toString(),
                    intent.getStringExtra(DConstants.INTERESTS).toString(),
                    intent.getStringExtra(DConstants.SUMMARY).toString()
                )
            }
        }
        binding.rvLanguages.setLayoutManager(layoutManager)
        val interestsListAdapter = LanguageAdapter(this, arrayListOf(
            Language(getString(R.string.hindi), R.drawable.hindi, false),
            Language(getString(R.string.telugu), R.drawable.telugu, false),
            Language(getString(R.string.malayalam), R.drawable.malayalam, false),
            Language(getString(R.string.kannada), R.drawable.kannada, false),
            Language(getString(R.string.punjabi), R.drawable.punjabi, false),
            Language(getString(R.string.tamil), R.drawable.tamil, false),
        ), object : OnItemSelectionListener<Language> {
            override fun onItemSelected(language: Language) {
                binding.btnContinue.isEnabled = true
                selectedLanguage = language.name
                binding.btnContinue.setBackgroundResource(R.drawable.d_button_bg_white)
            }

        }

        )
        binding.rvLanguages.setAdapter(interestsListAdapter)
    }

}