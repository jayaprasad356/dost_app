package com.gmwapp.dostt.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.gmwapp.dostt.R
import com.gmwapp.dostt.adapters.AvatarsListAdapter
import com.gmwapp.dostt.adapters.InterestsListAdapter
import com.gmwapp.dostt.databinding.ActivityEditProfileBinding
import com.gmwapp.dostt.retrofit.responses.Interests
import com.gmwapp.dostt.viewmodels.ProfileViewModel
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxItemDecoration
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditProfileActivity : BaseActivity(){
    private var avatarsListAdapter: AvatarsListAdapter?=null
    lateinit var binding: ActivityEditProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val MILLISECONDS_PER_INCH: Float = 50f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }
    private fun initUI() {
        window.navigationBarColor = getColor(R.color.black_background)

        val staggeredGridLayoutManager =FlexboxLayoutManager(this).apply {
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

        val interestsListAdapter = InterestsListAdapter(
            this,
            arrayListOf(
                Interests(getString(R.string.politics), R.drawable.politics),
                Interests(getString(R.string.art), R.drawable.art),
                Interests(getString(R.string.sports), R.drawable.sports),
                Interests(getString(R.string.movies), R.drawable.movie),
                Interests(getString(R.string.music), R.drawable.music),
                Interests(getString(R.string.foodie), R.drawable.foodie),
                Interests(getString(R.string.travel), R.drawable.travel),
                Interests(getString(R.string.photography), R.drawable.photography),
                Interests(getString(R.string.love), R.drawable.love),
                Interests(getString(R.string.cooking), R.drawable.cooking),
                ),
            1
        )
        binding.rvInterests.setAdapter(interestsListAdapter)
        binding.cvUserName.setBackgroundResource(R.drawable.d_button_bg_user_name);
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvAvatars)
        setCenterLayoutManager(binding.rvAvatars)
        profileViewModel.getAvatarsList("male")
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if(it.data != null) {
                it.data.add(it.data.size, null);
                it.data.add(0, null);

                 avatarsListAdapter = AvatarsListAdapter(
                    this,
                    it.data
                )
                binding.rvAvatars.setAdapter(avatarsListAdapter)
            }
            binding.rvAvatars.smoothScrollToPosition(1);
        })
    }
}


