package com.gmwapp.hima.activities

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Rect
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.widgets.CustomCallEmptyView
import com.gmwapp.hima.widgets.CustomCallView
import com.zegocloud.uikit.components.audiovideo.ZegoAvatarViewProvider
import com.zegocloud.uikit.components.audiovideo.ZegoForegroundViewProvider
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.DurationUpdateListener
import com.zegocloud.uikit.prebuilt.call.config.ZegoCallDurationConfig
import com.zegocloud.uikit.prebuilt.call.config.ZegoHangUpConfirmDialogInfo
import com.zegocloud.uikit.prebuilt.call.config.ZegoMenuBarButtonName
import com.zegocloud.uikit.prebuilt.call.core.invite.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.core.invite.advanced.ZegoCallInvitationInCallingConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays
import kotlin.math.abs


@AndroidEntryPoint
open class BaseActivity : AppCompatActivity() {
    private var foregroundView: CustomCallView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

    }

    fun showErrorMessage(message: String) {
        if (message == DConstants.NO_NETWORK) {
            Toast.makeText(
                this@BaseActivity,
                getString(com.gmwapp.hima.R.string.please_try_again_later),
                Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                this@BaseActivity, message, Toast.LENGTH_LONG
            ).show()
        }
    }

    fun setupZegoUIKit(Userid: Any, userName: String) {
        val appID: Long = 364167780
        val appSign = "3dd4f50fa22240d5943b75a843ef9711c7fa0424e80f8eb67c2bc0552cd1c2f3"
        val userID: String = Userid.toString()

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()

        callInvitationConfig.callingConfig = ZegoCallInvitationInCallingConfig()
        callInvitationConfig.callingConfig.onlyInitiatorCanInvite = false

        callInvitationConfig.provider = object : ZegoUIKitPrebuiltCallConfigProvider {

            override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                val config: ZegoUIKitPrebuiltCallConfig = when {
                    invitationData.type == ZegoInvitationType.VIDEO_CALL.value && invitationData.invitees.size > 1 -> {
                        ZegoUIKitPrebuiltCallConfig.groupVideoCall()
                    }

                    invitationData.type != ZegoInvitationType.VIDEO_CALL.value && invitationData.invitees.size > 1 -> {
                        ZegoUIKitPrebuiltCallConfig.groupVoiceCall()
                    }

                    invitationData.type != ZegoInvitationType.VIDEO_CALL.value -> {
                        ZegoUIKitPrebuiltCallConfig.oneOnOneVoiceCall()
                    }

                    else -> {
                        val oneOnOneVideoCall = ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
                        oneOnOneVideoCall.bottomMenuBarConfig.buttons = ArrayList(
                            Arrays.asList(
                                ZegoMenuBarButtonName.SWITCH_CAMERA_BUTTON,
                                ZegoMenuBarButtonName.HANG_UP_BUTTON,
                                ZegoMenuBarButtonName.TOGGLE_MICROPHONE_BUTTON,
                                ZegoMenuBarButtonName.SWITCH_AUDIO_OUTPUT_BUTTON
                            )
                        )
                        oneOnOneVideoCall
                    }
                }

                // Set up call duration configuration with a listener
                config.durationConfig = ZegoCallDurationConfig().apply {
                    isVisible = true
                    durationUpdateListener = object : DurationUpdateListener {
                        override fun onDurationUpdate(seconds: Long) {
                            Log.d("TAG", "onDurationUpdate() called with: seconds = [$seconds]")
                            foregroundView?.updateTime(seconds)
                            if (seconds.toInt() == 60 * 5) {  // Ends call after 5 minutes
                                //     ZegoUIKitPrebuiltCallService.endCall()
                            }
                        }
                    }
                }



                config.avatarViewProvider = object : ZegoAvatarViewProvider {
                    override fun onUserIDUpdated(
                        parent: ViewGroup, uiKitUser: ZegoUIKitUser
                    ): View {
                        try {
                            (parent.context as AppCompatActivity).window.setFlags(
                                WindowManager.LayoutParams.FLAG_SECURE,
                                WindowManager.LayoutParams.FLAG_SECURE
                            )
                        } catch (e: Exception) {
                        }
                        val imageView = ImageView(parent.context)
                        val requestOptions = RequestOptions().circleCrop()

                        // Set different avatars for different users based on the user parameter in the callback.
                        if (uiKitUser.userID == userID) {
                            val avatarUrl =
                                BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image
                            if (!avatarUrl.isNullOrEmpty()) {
                                val requestOptions = RequestOptions().circleCrop()
                                Glide.with(parent.context).load(avatarUrl).apply(requestOptions)
                                    .into(imageView)
                            }
                        }else{
                            val requestOptions = RequestOptions().circleCrop()
                            Glide.with(parent.context).load(uiKitUser.avatar).apply(requestOptions)
                                .into(imageView)
                        }
                        return imageView
                    }
                }

                callInvitationConfig.provider = object : ZegoUIKitPrebuiltCallConfigProvider {
                    override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                        val config = ZegoUIKitPrebuiltCallInvitationConfig.generateDefaultConfig(
                            invitationData
                        )
                        // Modify the config settings here according to your business needs
                        return config
                    }
                }


                config.hangUpConfirmDialogInfo = ZegoHangUpConfirmDialogInfo()
                config.audioVideoViewConfig.videoViewForegroundViewProvider =
                    ZegoForegroundViewProvider { parent, uiKitUser ->
                        if (uiKitUser.userID != userID) {
                            foregroundView = CustomCallView(parent.context, uiKitUser.userID)
                            foregroundView
                        }else {
                            CustomCallEmptyView(parent.context, uiKitUser.userID
                        )}

                    }
                return config
            }
        }

        ZegoUIKitPrebuiltCallService.init(
            BaseApplication.getInstance(), appID, appSign, userID, userName, callInvitationConfig
        )

    }

    fun setCenterLayoutManager(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
            ) {
                super.getItemOffsets(outRect, view, parent, state)

                val itemPosition = parent.getChildAdapterPosition(view)

                val itemWidth = view.layoutParams.width
                val offset = (parent.width - itemWidth) / 2

                if (itemPosition == 0) {
                    outRect.left = offset
                } else if (itemPosition == state.itemCount - 1) {
                    outRect.right = offset
                }
            }
        })
        val centerLayoutManager = CenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.setLayoutManager(centerLayoutManager)
    }
}

open class CenterLayoutManager : LinearLayoutManager {
    private val mShrinkAmount = 0.20f
    private val mShrinkDistance = 0.9f

    constructor(context: Context?) : super(context)

    constructor(context: Context?, orientation: Int, reverseLayout: Boolean) : super(
        context, orientation, reverseLayout
    )

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun checkLayoutParams(lp: RecyclerView.LayoutParams): Boolean {
        lp.width = width / 2
        lp.height = width / 2
        return true
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView, state: RecyclerView.State, position: Int
    ) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(recyclerView.context)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    private class CenterSmoothScroller(context: Context?) : LinearSmoothScroller(context) {
        override fun calculateDtToFit(
            viewStart: Int, viewEnd: Int, boxStart: Int, boxEnd: Int, snapPreference: Int
        ): Int {
            return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
        }
    }

    override fun canScrollHorizontally(): Boolean {
        return true
    }

    override fun scrollHorizontallyBy(
        dx: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?
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
}
