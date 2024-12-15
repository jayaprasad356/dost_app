package com.gmwapp.hima.fragments

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.zegocloud.uikit.components.audiovideo.ZegoAvatarViewProvider
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
import java.util.Arrays


open class BaseFragment : Fragment() {
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
                        );
                        oneOnOneVideoCall
                    }
                }

                // Set up call duration configuration with a listener
                config.durationConfig = ZegoCallDurationConfig().apply {
                    isVisible = true
                    durationUpdateListener = object : DurationUpdateListener {
                        override fun onDurationUpdate(seconds: Long) {
                            Log.d("TAG", "onDurationUpdate() called with: seconds = [$seconds]")
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
                        val imageView = ImageView(parent.context)
                        // Set different avatars for different users based on the user parameter in the callback.
                        val avatarUrl =
                            BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image
                        if (!avatarUrl.isNullOrEmpty()) {
                            val requestOptions = RequestOptions().circleCrop()
                            Glide.with(parent.context).load(avatarUrl).apply(requestOptions)
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
                config.hangUpConfirmDialogInfo.title = getString(R.string.warning)
                config.hangUpConfirmDialogInfo.message = getString(R.string.warning_end_call)
                config.hangUpConfirmDialogInfo.cancelButtonName = getString(R.string.cancel)
                config.hangUpConfirmDialogInfo.confirmButtonName = getString(R.string.confirm)
                return config
            }
        }

        ZegoUIKitPrebuiltCallService.init(
            BaseApplication.getInstance(), appID, appSign, userID, userName, callInvitationConfig
        )

    }
}