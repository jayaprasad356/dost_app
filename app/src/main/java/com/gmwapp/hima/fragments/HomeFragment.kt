package com.gmwapp.hima.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.text.Html
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
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
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.adapters.FemaleUserAdapter
import com.gmwapp.hima.adapters.TransactionAdapter
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
class HomeFragment : BaseFragment() {
    private var isAllFabVisible: Boolean = false
    lateinit var binding: FragmentHomeBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }


    private fun initUI(){
        binding.clCoins.setOnClickListener({
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        userData?.id?.let {
            femaleUsersViewModel.getFemaleUsers(
                it
            )
        }

        userData?.let { it1 -> setupZegoUIKit(it1.id,userData.name) }
        binding.fabAudio.setOnClickListener({
            val intent = Intent(context, CallActivity::class.java)
            startActivity(intent)
        })

        femaleUsersViewModel.femaleUsersResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it?.data != null) {
                binding.rvProfiles.setLayoutManager(
                    LinearLayoutManager(
                        activity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                )

                var transactionAdapter = activity?.let { it1 -> FemaleUserAdapter(it1, it.data) }
                binding.rvProfiles.setAdapter(transactionAdapter)
            }
        })

        initFab()
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
                    override fun onUserIDUpdated(parent: ViewGroup, uiKitUser: ZegoUIKitUser): View {
                        val imageView = ImageView(parent.context)
                        // Set different avatars for different users based on the user parameter in the callback.
                        val avatarUrl = BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image
                        if (!avatarUrl.isNullOrEmpty()) {
                            val requestOptions = RequestOptions().circleCrop()
                            Glide.with(parent.context).load(avatarUrl).apply(requestOptions).into(imageView)
                        }
                        return imageView
                    }
                }

                callInvitationConfig.provider = object : ZegoUIKitPrebuiltCallConfigProvider {
                    override fun requireConfig(invitationData: ZegoCallInvitationData): ZegoUIKitPrebuiltCallConfig {
                        val config = ZegoUIKitPrebuiltCallInvitationConfig.generateDefaultConfig(invitationData)
                        // Modify the config settings here according to your business needs
                        return config
                    }
                }



                return config
            }
        }
// Initialize the Zego call service with the provided details



        ZegoUIKitPrebuiltCallService.init(BaseApplication.getInstance(), appID, appSign, userID, userName, callInvitationConfig)

    }

    fun initFab(){


        val htmlText = " Video <img src='coin_d'/> 60/min"
        val htmlText1 = " Audio <img src='coin_d'/> 60/min"

        binding.tvVideo.text = Html.fromHtml(htmlText, Html.ImageGetter { source ->
            ContextCompat.getDrawable(requireActivity(), R.drawable.coin_d)?.apply {
                setBounds(0, 0, 45, 45)
            }
        }, null)

        binding.tvAudio.text = Html.fromHtml(htmlText1, Html.ImageGetter { source ->
            ContextCompat.getDrawable(requireActivity(), R.drawable.coin_d)?.apply {
                setBounds(0, 0, 45, 45)
            }
        }, null)


        binding.fabRandom.extend()
        binding.fabAudio.hide()
        binding.fabVideo.hide()
        binding.fabRandom.setOnClickListener {
            if (!isAllFabVisible) {
                binding.fabAudio.show()
                binding.fabVideo.show()
                binding.tvAudio.setVisibility(View.VISIBLE)
                binding.tvVideo.setVisibility(View.VISIBLE)

                // change the bg color
                binding.fabRandom.setBackgroundTintList(resources.getColorStateList(R.color.white))

                // Change the icon tint
                binding.fabRandom.setIconTintResource(R.color.black)


                // Change the icon and extend the parent FAB
                binding.fabRandom.setIconResource(R.drawable.ic_close) // Replace with your icon for shrinked state

                binding.fabRandom.shrink()
                isAllFabVisible = true
            } else {
                binding.fabAudio.hide()
                binding.fabVideo.hide()
                binding.tvAudio.setVisibility(View.GONE)
                binding.tvVideo.setVisibility(View.GONE)


                binding.fabRandom.setBackgroundTintList(resources.getColorStateList(R.color.blue))


                binding.fabRandom.setIconTintResource(R.color.white)



                // Change the icon and extend the parent FAB
                binding.fabRandom.setIconResource(R.drawable.random) // Replace with your icon for shrinked state
                binding.fabRandom.extend()

                isAllFabVisible = false
            }
        }
    }
}