package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.InterestsListAdapter
import com.gmwapp.hima.adapters.LanguageAdapter
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityFemaleAboutBinding
import com.gmwapp.hima.databinding.ActivitySelectLanguageBinding
import com.gmwapp.hima.retrofit.responses.Interests
import com.gmwapp.hima.retrofit.responses.Language
import com.gmwapp.hima.viewmodels.ProfileViewModel
import com.gmwapp.hima.widgets.SpacesItemDecoration
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FemaleAboutActivity : BaseActivity() {
    lateinit var binding: ActivityFemaleAboutBinding
    private var interestsListAdapter: InterestsListAdapter? = null
    private var selectedInterests: ArrayList<String> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFemaleAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.etEnterYourAge.setOnTouchListener { v, _ ->
            binding.cvEnterYourAge.setBackgroundResource(R.drawable.card_view_border_country_selected)
            false
        }
        binding.etEnterYourAge.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateButton()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        binding.etSummary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                updateButton()
                if (!TextUtils.isEmpty(s)) {
                    binding.tvRemainingText.text = getString(
                        R.string.description_remaining_text, s.length
                    )
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        val staggeredGridLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.FLEX_START
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        val itemDecoration = FlexboxItemDecoration(this).apply {
            setDrawable(ContextCompat.getDrawable(this@FemaleAboutActivity, R.drawable.bg_divider))
            setOrientation(FlexboxItemDecoration.VERTICAL)
        }
        binding.rvInterests.addItemDecoration(itemDecoration)
        binding.rvInterests.setLayoutManager(staggeredGridLayoutManager)
        interestsListAdapter = InterestsListAdapter(this, arrayListOf(
            Interests(
                getString(R.string.politics), R.drawable.politics, false
            ),
            Interests(
                getString(R.string.art), R.drawable.art, false
            ),
            Interests(
                getString(R.string.sports), R.drawable.sports, false
            ),
            Interests(
                getString(R.string.movies), R.drawable.movie, false
            ),
            Interests(
                getString(R.string.music), R.drawable.music, false
            ),
            Interests(
                getString(R.string.foodie), R.drawable.foodie, false
            ),
            Interests(
                getString(R.string.travel), R.drawable.travel, false
            ),
            Interests(
                getString(R.string.photography), R.drawable.photography, false
            ),
            Interests(
                getString(R.string.love), R.drawable.love, false
            ),
            Interests(
                getString(R.string.cooking), R.drawable.cooking, false
            ),
        ), false, object : OnItemSelectionListener<Interests> {
            override fun onItemSelected(interest: Interests) {
                if (interest.isSelected == true) {
                    selectedInterests.remove(interest.name)
                } else {
                    selectedInterests.add(interest.name)
                }
                interestsListAdapter?.updateLimitReached(selectedInterests.size == 4)
                updateButton()
            }
        })

    }

    private fun updateButton() {
        if (binding.etEnterYourAge.text.isNotEmpty() && selectedInterests.size > 0 && binding.etSummary.text.isNotEmpty()) {
            binding.btnContinue.isEnabled = true
            binding.btnContinue.setBackgroundResource(R.drawable.d_button_bg_white)
        } else {
            binding.btnContinue.isEnabled = false
            binding.btnContinue.setBackgroundResource(R.drawable.d_button_bg_disabled)
        }
    }

}