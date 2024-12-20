package com.gmwapp.hima.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.UpdateConnectedCallResponse
import com.gmwapp.hima.utils.UsersImage
import com.gmwapp.hima.viewmodels.ProfileViewModel
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
import com.zegocloud.uikit.prebuilt.call.event.BackPressEvent
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import java.util.Arrays


@AndroidEntryPoint
open class BaseFragment : Fragment() {
    var receivedId: Int = 0
    var callId: Int = 0
    var balanceTime: String? = null
    private var foregroundView: CustomCallView? = null
    private val profileViewModel: ProfileViewModel by viewModels()
    fun showErrorMessage(message: String) {
        if (message == DConstants.NO_NETWORK) {
            Toast.makeText(
                context, getString(R.string.please_try_again_later), Toast.LENGTH_LONG
            ).show()
        } else {
            Toast.makeText(
                context, message, Toast.LENGTH_LONG
            ).show()
        }
    }

    fun setupZegoUIKit(Userid: Any, userName: String) {
        val appID: Long = 364167780
        val appSign = "3dd4f50fa22240d5943b75a843ef9711c7fa0424e80f8eb67c2bc0552cd1c2f3"
        val userID: String = Userid.toString()

        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()

        callInvitationConfig.callingConfig = ZegoCallInvitationInCallingConfig()
        callInvitationConfig.callingConfig.canInvitingInCalling = false
        callInvitationConfig.callingConfig.onlyInitiatorCanInvite = true
        callInvitationConfig.incomingCallRingtone = "rhythm"
        callInvitationConfig.outgoingCallRingtone = "rhythm"

        callInvitationConfig.provider = object : ZegoUIKitPrebuiltCallConfigProvider {
            override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                callId = invitationData.customData.toInt()
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
                // Set up call duration configuration with a listener
                config.durationConfig = ZegoCallDurationConfig().apply {
                    isVisible = false
                    durationUpdateListener = object : DurationUpdateListener {
                        override fun onDurationUpdate(seconds: Long) {
                            Log.d("TAG", "onDurationUpdate() called with: seconds = [$seconds]")
                            var balanceTimeInsecs: Int = 0
                            try {
                                if (balanceTime != null) {
                                    val split = balanceTime!!.split(":")
                                    balanceTimeInsecs += split[0].toInt() * 60 + split[1].toInt()

                                }
                            } catch (e: Exception) {
                            }
                            var remainingTime: Int = balanceTimeInsecs - seconds.toInt()
                            if (remainingTime > 0) {
                                foregroundView?.updateTime(remainingTime)
                            }
                            if (balanceTime != null && remainingTime <= 0) {
                                ZegoUIKitPrebuiltCallService.endCall()
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

                        Glide.with(parent.context).load(uiKitUser.avatar?.ifEmpty {
                            BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image
                        }).apply(requestOptions).into(imageView)
                        // Set different avatars for different users based on the user parameter in the callback.
                        if (uiKitUser.userID == userID) {
                            val avatarUrl =
                                BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image
                            if (!avatarUrl.isNullOrEmpty()) {
                                val requestOptions = RequestOptions().circleCrop()
                                Glide.with(parent.context).load(avatarUrl).apply(requestOptions)
                                    .into(imageView)
                            }
                        } else {
                            receivedId = uiKitUser.userID.toInt()
                            val requestOptions = RequestOptions().circleCrop()
                            Glide.with(parent.context).load(
                                UsersImage(
                                    profileViewModel,
                                    uiKitUser.userID.toInt()
                                ).execute().get()
                            ).apply(requestOptions).into(imageView)

                        }
                        return imageView
                    }
                }

                config.hangUpConfirmDialogInfo = ZegoHangUpConfirmDialogInfo()
                config.audioVideoViewConfig.videoViewForegroundViewProvider =
                    ZegoForegroundViewProvider { parent, uiKitUser ->
                        if (uiKitUser.userID != userID) {
                            foregroundView = CustomCallView(parent.context, uiKitUser.userID)
                            foregroundView
                        } else {

                            CustomCallEmptyView(
                                parent.context, uiKitUser.userID
                            )
                        }

                    }
                config.topMenuBarConfig.buttons.add(ZegoMenuBarButtonName.MINIMIZING_BUTTON)
                config.topMenuBarConfig.hideByClick = false
                config.topMenuBarConfig.hideAutomatically = true
                config.bottomMenuBarConfig.hideByClick = false
                config.bottomMenuBarConfig.hideAutomatically = false
                return config
            }
        }

        ZegoUIKitPrebuiltCallService.init(
            BaseApplication.getInstance(), appID, appSign, userID, userName, callInvitationConfig
        )

    }
}