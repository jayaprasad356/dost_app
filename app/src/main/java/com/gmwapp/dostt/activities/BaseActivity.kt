package com.gmwapp.dostt.activities

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PointF
import android.os.Bundle
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import kotlin.math.abs

open class BaseActivity : AppCompatActivity() {
    private val mShrinkAmount = 0.20f
    private val mShrinkDistance = 0.9f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onStart() {
        super.onStart()
    }

    fun setCenterLayoutManager(recyclerView: RecyclerView){
        recyclerView.setLayoutManager(object : CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false) {
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
