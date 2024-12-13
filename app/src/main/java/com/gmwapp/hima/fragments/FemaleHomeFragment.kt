package com.gmwapp.hima.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.activities.CallActivity
import com.gmwapp.hima.activities.DeleteAccountActivity
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.adapters.FemaleUserAdapter
import com.gmwapp.hima.adapters.TransactionAdapter
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.FragmentFemaleHomeBinding
import com.gmwapp.hima.databinding.FragmentHomeBinding
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponseData
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.viewmodels.LoginViewModel
import com.zegocloud.uikit.components.audiovideo.ZegoAvatarViewProvider
import com.zegocloud.uikit.plugin.invitation.ZegoInvitationType
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallConfig
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.config.DurationUpdateListener
import com.zegocloud.uikit.prebuilt.call.config.ZegoCallDurationConfig
import com.zegocloud.uikit.prebuilt.call.core.invite.ZegoCallInvitationData
import com.zegocloud.uikit.prebuilt.call.core.invite.advanced.ZegoCallInvitationInCallingConfig
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoUIKitPrebuiltCallConfigProvider
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class FemaleHomeFragment : BaseFragment() {
    lateinit var binding: FragmentFemaleHomeBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFemaleHomeBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.clCoins.setOnClickListener({
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()
        if (userData != null) {
            binding.sAudio.isChecked = userData.audio_status == 1
            binding.sVideo.isChecked = userData.video_status == 1
        }
        femaleUsersViewModel.updateCallStatusResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it.success) {
                prefs?.setUserData(it.data)
            } else {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })
        femaleUsersViewModel.updateCallStatusErrorLiveData.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it)
            binding.sAudio.isChecked = prefs?.getUserData()?.audio_status == 1;
            binding.sVideo.isChecked = prefs?.getUserData()?.video_status == 1;
        })
        binding.sAudio.setOnCheckedChangeListener({ buttonView, isChecked ->
            userData?.id?.let {
                femaleUsersViewModel.updateCallStatus(
                    it, DConstants.AUDIO, if (isChecked) 1 else 0
                )
            }
        })
        binding.sVideo.setOnCheckedChangeListener({ buttonView, isChecked ->
            userData?.id?.let {
                femaleUsersViewModel.updateCallStatus(
                    it, DConstants.VIDEO, if (isChecked) 1 else 0
                )
            }
        })
    }

    private fun setupZegoUIKit(Userid: Any, userName: String) {

        // Android's application context
        val appID: Long = 364167780
        val appSign: String = "3dd4f50fa22240d5943b75a843ef9711c7fa0424e80f8eb67c2bc0552cd1c2f3"
        val userID: String = Userid.toString()
        val userName: String = userName.toString()


        val callInvitationConfig = ZegoUIKitPrebuiltCallInvitationConfig()


        //  callInvitationConfig.incomingCallRingtone = "outgoingcallringtone" // No file extension
        //  callInvitationConfig.outgoingCallRingtone = "outgoingcallringtone" // No file extension

        callInvitationConfig.callingConfig = ZegoCallInvitationInCallingConfig()

        // Whether to enable the feature of inviting users to an ongoing call
        callInvitationConfig.callingConfig.onlyInitiatorCanInvite = false


        // Set the custom call configuration provider
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
                        ZegoUIKitPrebuiltCallConfig.oneOnOneVideoCall()
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



                return config
            }
        }
// Initialize the Zego call service with the provided details


        ZegoUIKitPrebuiltCallService.init(
            BaseApplication.getInstance(), appID, appSign, userID, userName, callInvitationConfig
        )

    }

}