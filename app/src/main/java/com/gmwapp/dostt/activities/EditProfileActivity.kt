package com.gmwapp.dostt.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.adapters.AvatarsListAdapter
import com.gmwapp.dostt.adapters.InterestsListAdapter
import com.gmwapp.dostt.callbacks.OnItemSelectionListener
import com.gmwapp.dostt.databinding.ActivityEditProfileBinding
import com.gmwapp.dostt.retrofit.responses.Interests
import com.gmwapp.dostt.retrofit.responses.Language
import com.gmwapp.dostt.viewmodels.ProfileViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileActivity : BaseActivity() {
    private var interestsListAdapter: InterestsListAdapter? = null
    private var avatarsListAdapter: AvatarsListAdapter? = null
    lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val selectedInterests: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        binding.tvGender.setText(userData?.gender)
        binding.tvPreferredLanguage.setText(userData?.language)
        binding.btnUpdate.setBackgroundResource(R.drawable.d_button_bg_disabled)
        binding.ivBack.setOnClickListener(View.OnClickListener {
            finish()
        })
        window.navigationBarColor = getColor(R.color.black_background)

        val staggeredGridLayoutManager = FlexboxLayoutManager(this).apply {
            flexWrap = FlexWrap.WRAP
            alignItems = AlignItems.FLEX_START
            flexDirection = FlexDirection.ROW
            justifyContent = JustifyContent.FLEX_START
        }
        val itemDecoration = FlexboxItemDecoration(this).apply {
            setDrawable(ContextCompat.getDrawable(this@EditProfileActivity, R.drawable.bg_divider))
            setOrientation(FlexboxItemDecoration.VERTICAL)
        }
        binding.rvInterests.addItemDecoration(itemDecoration)
        binding.rvInterests.setLayoutManager(staggeredGridLayoutManager)

        interestsListAdapter = InterestsListAdapter(this, arrayListOf(
            Interests(getString(R.string.politics), R.drawable.politics, false),
            Interests(getString(R.string.art), R.drawable.art, false),
            Interests(getString(R.string.sports), R.drawable.sports, false),
            Interests(getString(R.string.movies), R.drawable.movie, false),
            Interests(getString(R.string.music), R.drawable.music, false),
            Interests(getString(R.string.foodie), R.drawable.foodie, false),
            Interests(getString(R.string.travel), R.drawable.travel, false),
            Interests(getString(R.string.photography), R.drawable.photography, false),
            Interests(getString(R.string.love), R.drawable.love, false),
            Interests(getString(R.string.cooking), R.drawable.cooking, false),
        ), false, object : OnItemSelectionListener<Interests> {
            override fun onItemSelected(interest: Interests) {
                if (interest.isSelected == true) {
                    selectedInterests.remove(interest.name)
                } else {
                    selectedInterests.add(interest.name)
                }
                interestsListAdapter?.updateLimitReached(selectedInterests.size == 4)
                if (selectedInterests.size > 0) {
                    binding.btnUpdate.isEnabled = true
                    binding.btnUpdate.setBackgroundResource(R.drawable.d_button_bg_white)
                } else {
                    binding.btnUpdate.isEnabled = false
                    binding.btnUpdate.setBackgroundResource(R.drawable.d_button_bg_disabled)
                }
            }
        })
        binding.btnUpdate.setOnClickListener(View.OnClickListener {
            val layoutManager = binding.rvAvatars.layoutManager as CenterLayoutManager
            val avatarId = profileViewModel.avatarsListLiveData.value?.data?.get(layoutManager.findFirstCompletelyVisibleItemPosition())?.id
            userData?.let { it1 ->
                avatarId?.let { it2 ->
                    profileViewModel.updateProfile(
                        it1.id,
                        it2,
                        binding.etUserName.text.toString(),
                        selectedInterests
                    )
                }
            }

        })
        binding.rvInterests.setAdapter(interestsListAdapter)
        binding.cvUserName.setBackgroundResource(R.drawable.d_button_bg_user_name)
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvAvatars)
        setCenterLayoutManager(binding.rvAvatars)
        profileViewModel.getAvatarsList("male")
        profileViewModel.updateProfileLiveData.observe(this, Observer {
            if (it.data != null) {
                Toast.makeText(this@EditProfileActivity,getString(R.string.profile_updated), Toast.LENGTH_LONG).show()
                finish()
            }
        })
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if (it.data != null) {
                avatarsListAdapter = AvatarsListAdapter(
                    this, it.data
                )
                binding.rvAvatars.setAdapter(avatarsListAdapter)
            }
            binding.rvAvatars.smoothScrollToPosition(0)
        })
    }
}


