package com.gmwapp.hima.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.callbacks.OnButtonClickListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityRandomUserBinding
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.viewmodels.ProfileViewModel
import com.gmwapp.hima.workers.CallUpdateWorker
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.internal.OutgoingCallButtonListener
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class RandomUserActivity : BaseActivity(), OnButtonClickListener {
    private var mediaPlayer: MediaPlayer? = null
    private var isReceiverDetailsAvailable: Boolean = false
    private val CALL_PERMISSIONS_REQUEST_CODE = 1
    lateinit var binding: ActivityRandomUserBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()
    private var usersCount: Int = 0
    private val profileViewModel: ProfileViewModel by viewModels()

    private var userId: String = ""
    private var callUserId: String = ""
    private var callUserName: String = ""
    private var startTime: String = ""
    private var endTime: String = ""
    var targetUserId: String? = null

    private val dateFormat = SimpleDateFormat("HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Set to IST time zone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        askPermissions()
        onBackPressedDispatcher.addCallback(this ) {
        }
    }

    private fun checkOverlayPermission() {
        try {
            PermissionX.init(this).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
                .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                    try {
                        val message =
                            "We need your consent for the following permissions in order to use the offline call function properly"
                        scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
                    } catch (e: Exception) {
                    }
                }).request(RequestCallback { allGranted, grantedList, deniedList ->
                    try {
                        if (allGranted) {
                            initializeCall(false)
                        } else {
                            checkOverlayPermission()
                        }
                    } catch (e: Exception) {
                    }
                })
        } catch (e: Exception) {
        }

    }

    fun askPermissions() {
        val permissionNeeded =
            arrayOf("android.permission.RECORD_AUDIO", "android.permission.CAMERA")

        if (ContextCompat.checkSelfPermission(
                this, "android.permission.CAMERA"
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this, "android.permission.RECORD_AUDIO"
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissionNeeded, CALL_PERMISSIONS_REQUEST_CODE)
        } else {
            checkOverlayPermission()
        }
    }

    private fun initializeCall(cancelled: Boolean) {
        if (mediaPlayer == null) {
            val resID = resources.getIdentifier("rhythm", "raw", packageName)
            mediaPlayer = MediaPlayer.create(this, resID)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        }

        if (isReceiverDetailsAvailable) {
            if (cancelled) {
                mediaPlayer?.pause()
                mediaPlayer?.stop()
                finish()
            } else {
                val receiverId = intent.getIntExtra(DConstants.RECEIVER_ID, 0)
                val receiverName = intent.getStringExtra(DConstants.RECEIVER_NAME)
                callType = intent.getStringExtra(DConstants.CALL_TYPE)
                val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
                userData?.id?.let {
                    femaleUsersViewModel.callFemaleUser(
                        it, receiverId, callType.toString()
                    )
                }

                femaleUsersViewModel.callFemaleUserResponseLiveData.observe(this, Observer {
                    if (it != null && it.success) {
                        val callId = it.data?.call_id
                        val balanceTime = it.data?.balance_time
                        if (callId != null) {
                            setupCall(
                                receiverId.toString(),
                                receiverName.toString(),
                                callType.toString(),
                                balanceTime,
                                callId
                            )
                            addRoomStateChangedListener(callId)
                        }
                    }else{
                        Toast.makeText(
                            this@RandomUserActivity, it?.message, Toast.LENGTH_LONG
                        ).show()
                        stopCall()
                        finish()
                    }
                })

            }
        } else {
            if(usersCount<4) {
                usersCount++
                val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
                val callType = intent.getStringExtra(DConstants.CALL_TYPE)
                userData?.let {
                    callType?.let { it1 -> femaleUsersViewModel.getRandomUser(it.id, it1) }
                }
            }else{
                stopCall()
                finish()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMISSIONS_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToRecord = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToCamera && permissionToRecord) {
                    checkOverlayPermission()
                } else {
                    finish()
                    val intent = Intent(this, GrantPermissionsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override public fun onButtonClick() {
        getRemainingTime()
    }

    private fun initUI() {
        val callType = intent.getStringExtra(DConstants.CALL_TYPE)
        val image = intent.getStringExtra(DConstants.IMAGE)
        val text = intent.getStringExtra(DConstants.TEXT)
        if (image != null) {
            val requestOptions = RequestOptions().circleCrop()
            Glide.with(this).load(image).apply(requestOptions).into(binding.ivLogo)
        }
        if (text != null) {
            binding.tvWaitHint.text = text
        }
        binding.btnCancel.setOnClickListener({
            stopCall()
            finish()
        })
        isReceiverDetailsAvailable =
            intent.getBooleanExtra(DConstants.IS_RECEIVER_DETAILS_AVAILABLE, false)
        femaleUsersViewModel.randomUsersResponseLiveData.observe(this, Observer {
            if (it!=null && it.success) {
                val data = it.data
                data?.call_id?.let { it1 ->

                    setupCall(
                        data.call_user_id.toString(),
                        data.call_user_name.toString(),
                        callType.toString(),
                        data.balance_time,
                        it1,
                    )
                }
                data?.call_id?.let { it1 -> addRoomStateChangedListener(it1) }
            } else {
                Toast.makeText(
                    this@RandomUserActivity, it?.message, Toast.LENGTH_LONG
                ).show()
                mediaPlayer?.pause()
                mediaPlayer?.stop()
                finish()
            }
        })
        femaleUsersViewModel.randomUsersErrorLiveData.observe(this, Observer {
            showErrorMessage(it)
            mediaPlayer?.pause()
            mediaPlayer?.stop()
            finish()
        })
    }

    private fun addRoomStateChangedListener(callId: Int) {
        ZegoUIKitPrebuiltCallService.events.invitationEvents.outgoingCallButtonListener =
            object : OutgoingCallButtonListener {
                override fun onOutgoingCallCancelButtonPressed() {
                    stopCall()
                    finish()
                }
            }
        ZegoUIKitPrebuiltCallService.events.invitationEvents.invitationListener =
            object : ZegoInvitationCallListener {
                override fun onIncomingCallReceived(
                    callID: String?,
                    caller: ZegoCallUser?,
                    callType: ZegoCallType?,
                    callees: MutableList<ZegoCallUser>?
                ) {
                }

                override fun onIncomingCallCanceled(callID: String?, caller: ZegoCallUser?) {
                    stopCall()
                    finish()
                }

                override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                    stopCall()
                    finish()
                }

                override fun onOutgoingCallAccepted(callID: String?, callee: ZegoCallUser?) {
                }


                override fun onOutgoingCallRejectedCauseBusy(
                    callID: String?, callee: ZegoCallUser?
                ) {
                    ZegoUIKitPrebuiltCallService.endCall()
                    initializeCall(true)
                }

                override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                    ZegoUIKitPrebuiltCallService.endCall()
                    initializeCall(true)
                }

                override fun onOutgoingCallTimeout(
                    callID: String?, callees: MutableList<ZegoCallUser>?
                ) {
                    ZegoUIKitPrebuiltCallService.endCall()
                    initializeCall(true)
                }
            }


        ZegoUIKit.addRoomStateChangedListener { room, reason, _, _ ->
            when (reason) {
                ZegoRoomStateChangedReason.LOGINED -> {
                    lastActiveTime = System.currentTimeMillis();
                    mediaPlayer?.pause()
                    mediaPlayer?.stop()
                    roomID = room
                    userId = BaseApplication.getInstance()?.getPrefs()
                        ?.getUserData()?.id.toString() // Set user_id
                    callUserId = targetUserId.toString() // Set call_user_id
                    startTime = dateFormat.format(Date()) // Set call start time in IST

                }

                ZegoRoomStateChangedReason.LOGOUT -> {
                    lifecycleScope.launch {
                        lastActiveTime = null
                        delay(500)
                        if (roomID != null) {
                            roomID = null
                            endTime = dateFormat.format(Date()) // Set call end time in IST

                            val constraints =
                                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                            val data: Data = Data.Builder()
                                .putInt(DConstants.USER_ID, BaseApplication.getInstance()?.getPrefs()
                                    ?.getUserData()?.id?:0)
                                .putInt(DConstants.CALL_ID, callId)
                                .putString(DConstants.STARTED_TIME, startTime)
                                .putString(DConstants.ENDED_TIME, endTime).build()
                            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                                CallUpdateWorker::class.java
                            ).setInputData(data).setConstraints(constraints).build()
                            WorkManager.getInstance(this@RandomUserActivity)
                                .enqueue(oneTimeWorkRequest)
                            mediaPlayer?.pause()
                            mediaPlayer?.stop()
                            finish()
                            val receiverId = intent.getIntExtra(DConstants.RECEIVER_ID, 0)
                            val intent = Intent(this@RandomUserActivity, ReviewActivity::class.java)
                            intent.putExtra(DConstants.RECEIVER_NAME, callUserName)
                            intent.putExtra(DConstants.RECEIVER_ID, receiverId)
                            startActivity(intent)
                        }
                    }
                }

                else -> {
                }
            }
        }
    }

    private fun stopCall(){
        ZegoUIKitPrebuiltCallService.endCall()
        mediaPlayer?.pause()
        mediaPlayer?.stop()
    }

    private fun setupCall(
        receiverId: String?, receiverName: String, type: String, balanceTime: String?, callId: Int
    ) {
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()
        if (userData != null) {
            this.balanceTime = balanceTime;
            setupZegoUIKit(userData.id, userData.name)
        }
        when (type) {
            "audio" -> receiverId?.let { StartVoiceCall(it, receiverName, callId) }
            "video" -> receiverId?.let { StartVideoCall(it, receiverName, callId) }
            else -> Toast.makeText(this, "Invalid call type", Toast.LENGTH_SHORT).show()
        }

    }

    private fun StartVoiceCall(targetUserId: String, targetName: String, callId: Int) {
        binding.voiceCallButton.setIsVideoCall(false)
        binding.voiceCallButton.resourceID = "zego_call"
        val user = ZegoUIKitUser(targetUserId, targetName)
        val instance = BaseApplication.getInstance()
        user.avatar = instance?.getPrefs()?.getUserData()?.image
        binding.voiceCallButton.setCustomData(callId.toString())
        binding.voiceCallButton.setInvitees(listOf(user))
        binding.voiceCallButton.setTimeout(7)
        callUserName = targetName
        callUserId = targetUserId
        lifecycleScope.launch {
            if (instance?.isCalled() == false || instance?.isCalled() == null) {
                delay(4000)
                instance?.setCalled(true)
            }
            binding.voiceCallButton.performClick()
        }

    }

    private fun StartVideoCall(targetUserId: String, targetName: String, callId: Int) {
        callUserName = targetName
        callUserId = targetUserId
        binding.voiceCallButton.setIsVideoCall(true)
        binding.voiceCallButton.resourceID = "zego_call"
        val user = ZegoUIKitUser(targetUserId, targetName)
        val instance = BaseApplication.getInstance()
        user.avatar = instance?.getPrefs()?.getUserData()?.image
        binding.voiceCallButton.setCustomData(callId.toString())
        binding.voiceCallButton.setInvitees(listOf(user))
        binding.voiceCallButton.setTimeout(7)
        lifecycleScope.launch {
            if (instance?.isCalled() == false || instance?.isCalled() == null) {
                delay(4000)
                instance?.setCalled(true)
            }
            binding.voiceCallButton.performClick()
        }
    }
}
