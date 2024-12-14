package com.gmwapp.hima.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityCallBinding
import com.gmwapp.hima.databinding.ActivityMainBinding
import com.gmwapp.hima.databinding.ActivityRandomUserBinding
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.workers.CallUpdateWorker
import com.zego.ve.Log
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone
import java.util.Timer
import java.util.TimerTask


@AndroidEntryPoint
class RandomUserActivity : BaseActivity() {
    lateinit var binding: ActivityRandomUserBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()
    lateinit var activity: Activity

    private var roomID: String? = null
    private var duration = 0
    private var timer: Timer? = null
    private val handler = Handler(Looper.getMainLooper())

    private var userId: String = ""
    private var callUserId: String = ""
    private var startTime: String = ""
    private var endTime: String = ""
    var targetUserId: String? = null

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Set to IST time zone
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        val callType = intent.getStringExtra(DConstants.CALL_TYPE)
        binding.btnCancel.setOnClickListener({
            finish()
        })
        femaleUsersViewModel.randomUsersResponseLiveData.observe(this, Observer {
            if (it.success) {
                it.data?.call_id?.let { it1 -> addRoomStateChangedListener(it1) }
                setupCall("48", "Anushka854", callType.toString())
            } else {
                Toast.makeText(
                    this@RandomUserActivity, it.message, Toast.LENGTH_LONG
                ).show()
                finish()
            }
        })
        femaleUsersViewModel.randomUsersErrorLiveData.observe(this, Observer {
            showErrorMessage(it)
            finish()
        })
        userData?.let {
            callType?.let { it1 -> femaleUsersViewModel.getRandomUser(it.id, it1) }
        }
    }

    private fun addRoomStateChangedListener(callId:Int) {
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
                    Toast.makeText(this@RandomUserActivity, "Call Cancelled", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                    Toast.makeText(this@RandomUserActivity, "Call Timeout", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onOutgoingCallAccepted(callID: String?, callee: ZegoCallUser?) {
                }


                override fun onOutgoingCallRejectedCauseBusy(
                    callID: String?, callee: ZegoCallUser?
                ) {
                    Toast.makeText(this@RandomUserActivity, "Call Rejected", Toast.LENGTH_SHORT).show()
                }

                override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                    // addObsereves()
                    Toast.makeText(this@RandomUserActivity, "Call Rejected", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onOutgoingCallTimeout(
                    callID: String?, callees: MutableList<ZegoCallUser>?
                ) {
                    Toast.makeText(this@RandomUserActivity, "Call Timeout", Toast.LENGTH_SHORT).show()
                    TODO("Not yet implemented")
                }
            }


        ZegoUIKit.addRoomStateChangedListener { room, reason, _, _ ->
            when (reason) {
                ZegoRoomStateChangedReason.LOGINED -> {
                    roomID = room
                    userId = BaseApplication.getInstance()?.getPrefs()
                        ?.getUserData()?.id.toString() // Set user_id
                    callUserId = targetUserId.toString() // Set call_user_id
                    startTime = dateFormat.format(Date()) // Set call start time in IST
                    startTimer()    // Start the timer when call start
                }

                ZegoRoomStateChangedReason.LOGOUT -> {
                    stopTimer()
                    endTime = dateFormat.format(Date()) // Set call end time in IST
                    val constraints =
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    val data: Data = Data.Builder()
                        .putInt(DConstants.CALL_ID, callId)
                        .putString(DConstants.STARTED_TIME, startTime)
                        .putString(DConstants.ENDED_TIME, endTime).build()
                    val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                        CallUpdateWorker::class.java
                    ).setInputData(data).setConstraints(constraints).build()
                    WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

                }

                else -> { /* Handle other cases if necessary */
                }
            }
        }
    }


    private fun startTimer() {
        duration = 0
        timer = Timer()
        timer?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                duration++
            }
        }, 0, 1000)
    }

    private fun stopTimer() {
        timer?.cancel()
        timer = null
    }

    private fun transToHourMinSec(seconds: Int): String {
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60
        val secs = seconds % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    private fun setupCall(receiverId: String, receiverName: String, type: String) {
        activity = this

        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()
        if (userData != null) {
            setupZegoUIKit(userData.id, userData.name)
        }
        when (type) {
            "audio" -> StartVoiceCall(receiverId, receiverName)
            "video" -> StartVideoCall(receiverId, receiverName)
            else -> Toast.makeText(this, "Invalid call type", Toast.LENGTH_SHORT).show()
        }

    }

    private fun StartVoiceCall(targetUserId: String, targetName: String) {
        binding.voiceCallButton.setIsVideoCall(false)
        binding.voiceCallButton.resourceID = "zego_uikit_call"
        binding.voiceCallButton.setInvitees(listOf(ZegoUIKitUser(targetUserId, targetName)))


        lifecycleScope.launch {
            delay(4000)  // 4-second delay before initiating the call
            binding.voiceCallButton.performClick() // Programmatically click to start the call

            // Additional delay if needed to allow time for the call to start
            delay(4000)  // Additional delay to allow the call setup
        }

    }

    private fun StartVideoCall(targetUserId: String, targetName: String) {
        binding.voiceCallButton.setIsVideoCall(true)
        binding.voiceCallButton.resourceID = "zego_uikit_call"
        binding.voiceCallButton.setInvitees(listOf(ZegoUIKitUser(targetUserId, targetName)))
        lifecycleScope.launch {
            delay(4000)  // 4-second delay before initiating the call
            binding.voiceCallButton.performClick() // Programmatically click to start the call

            // Additional delay if needed to allow time for the call to start
            delay(4000)  // Additional delay to allow the call setup
            // Finish the activity after call initiation
        }
    }


}
