package com.gmwapp.dostt.activities

import android.R.drawable
import android.content.Context
import android.graphics.PointF
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
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
import kotlin.math.abs


@AndroidEntryPoint
class EditProfileActivity : BaseActivity(){
    private val mShrinkAmount = 0.20f
    private val mShrinkDistance = 0.9f
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
        binding.rvAvatars.setLayoutManager(object : CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
                lp.width = width / 2
                lp.height = width / 2
                return true
            }

            override fun smoothScrollToPosition(
                recyclerView: RecyclerView,
                state: RecyclerView.State,
                position: Int
            ) {
                val smoothScroller: SmoothScroller = CenterSmoothScroller(recyclerView.context)
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }

            override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                return super.computeScrollVectorForPosition(targetPosition)
            }

            override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                return 5000F / displayMetrics.densityDpi
            }

            private open inner class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
                override fun calculateDtToFit(
                    viewStart: Int,
                    viewEnd: Int,
                    boxStart: Int,
                    boxEnd: Int,
                    snapPreference: Int
                ): Int {
                    return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
                }
            }

            override fun canScrollHorizontally(): Boolean {
                return true
            }
            override fun scrollHorizontallyBy(
                dx: Int,
                recycler: RecyclerView.Recycler?,
                state: RecyclerView.State?
            ): Int {
                val orientation = orientation
                if (orientation == HORIZONTAL) {
                    val scrolled = super.scrollHorizontallyBy(dx, recycler, state)

                    val midpoint = width / 2f
                    val d0 = 0f
                    val d1 = mShrinkDistance * midpoint
                    val s0 = 1f
                    val s1 = 1f - mShrinkAmount
                    for (i in 0 until childCount) {
                        val child = getChildAt(i)
                        val childMidpoint = (getDecoratedRight(child!!) + getDecoratedLeft(child)) / 2f
                        val d = d1.coerceAtMost(abs(midpoint - childMidpoint))
                        val scale = s0 + (s1 - s0) * (d - d0) / (d1 - d0)
                        child.scaleX = scale
                        child.scaleY = scale
                    }
                    return scrolled
                } else {
                    return 0
                }

            }

        })


        profileViewModel.getAvatarsList("male")
        profileViewModel.avatarsListLiveData.observe(this, Observer {
            if(it.data != null) {
                it.data.add(it.data.size, null);
                it.data.add(0, null);

                 avatarsListAdapter = AvatarsListAdapter(
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

open class CenterLayoutManager : LinearLayoutManager {
    constructor(context: Context?) : super(context)

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int,
            viewEnd: Int,
            boxStart: Int,
            boxEnd: Int,
            snapPreference: Int
        ): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }
    }

    open fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
        TODO("Not yet implemented")
    }
}

