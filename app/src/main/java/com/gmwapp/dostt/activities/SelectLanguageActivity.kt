package com.gmwapp.dostt.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.adapters.LanguageAdapter
import com.gmwapp.dostt.callbacks.OnItemSelectionListener
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivitySelectLanguageBinding
import com.gmwapp.dostt.retrofit.responses.Language
import com.gmwapp.dostt.viewmodels.ProfileViewModel
import com.gmwapp.dostt.widgets.SpacesItemDecoration
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
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
        val layoutManager = GridLayoutManager(this,2);
        binding.rvLanguages.addItemDecoration(SpacesItemDecoration(20));

        binding.btnContinue.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(DConstants.AVATAR_ID, getIntent().getStringExtra(DConstants.AVATAR_ID))
            intent.putExtra(DConstants.LANGUAGE, selectedLanguage)
            startActivity(intent)
        }
        binding.rvLanguages.setLayoutManager(layoutManager)
        val interestsListAdapter = LanguageAdapter(
            this,
            arrayListOf(
                Language(getString(R.string.hindi), R.drawable.hindi, false),
                Language(getString(R.string.telugu), R.drawable.telugu, false),
                Language(getString(R.string.malayalam), R.drawable.malayalam, false),
                Language(getString(R.string.kannada), R.drawable.kannada, false),
                Language(getString(R.string.punjabi), R.drawable.punjabi, false),
                Language(getString(R.string.tamil), R.drawable.tamil, false),
            ),
            object : OnItemSelectionListener<Language> {
                override fun onItemSelected(language: Language){
                    binding.btnContinue.isEnabled = true
                    selectedLanguage = language.name
                }
            }

            )
        binding.rvLanguages.setAdapter(interestsListAdapter)
    }

}