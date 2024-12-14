package com.gmwapp.hima.activities

import android.app.Activity
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
import com.gmwapp.hima.workers.CallUpdateWorker
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
class CallActivity : BaseActivity() {
    lateinit var binding: ActivityCallBinding
    lateinit var mContext: CallActivity
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


    private lateinit var voiceCallButton: ZegoSendCallInvitationButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        voiceCallButton = binding.voiceCallButton
        mContext = this
        initUI()


        addRoomStateChangedListener()

    }


    private fun addRoomStateChangedListener() {
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
                    Toast.makeText(mContext, "Call Cancelled", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                    Toast.makeText(mContext, "Call Timeout", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onOutgoingCallAccepted(callID: String?, callee: ZegoCallUser?) {
                }


                override fun onOutgoingCallRejectedCauseBusy(
                    callID: String?, callee: ZegoCallUser?
                ) {
                    Toast.makeText(mContext, "Call Rejected", Toast.LENGTH_SHORT).show()
                }

                override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                    // addObsereves()
                    Toast.makeText(mContext, "Call Rejected", Toast.LENGTH_SHORT).show()
                    finish()
                }

                override fun onOutgoingCallTimeout(
                    callID: String?, callees: MutableList<ZegoCallUser>?
                ) {
                    Toast.makeText(mContext, "Call Timeout", Toast.LENGTH_SHORT).show()
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
                        .putString(DConstants.CALL_ID, intent.getStringExtra(DConstants.CALL_ID))
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

    private fun initUI() {
        activity = this

        val receiverId = intent.getIntExtra(DConstants.RECEIVER_ID,0).toString()
        val receiverName = intent.getStringExtra(DConstants.RECEIVER_NAME)
        val type = intent.getStringExtra(DConstants.CALL_TYPE)

        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()
        if (userData != null) {
            setupZegoUIKit(userData.id,userData.name)
        }
        when (type) {
            "audio" -> receiverId?.let { receiverName?.let { it1 -> StartVoiceCall(it, it1) } }
            "video" -> receiverId?.let { receiverName?.let { it1 -> StartVideoCall(it, it1) } }
            else -> Toast.makeText(this, "Invalid call type", Toast.LENGTH_SHORT).show()
        }

    }

    private fun StartVoiceCall(targetUserId: String, targetName: String) {
        voiceCallButton.setIsVideoCall(false)
        voiceCallButton.resourceID = "zego_uikit_call"
        voiceCallButton.setInvitees(listOf(ZegoUIKitUser(targetUserId, targetName)))


        lifecycleScope.launch {
            delay(4000)  // 4-second delay before initiating the call
            voiceCallButton.performClick() // Programmatically click to start the call

            // Additional delay if needed to allow time for the call to start
            delay(4000)  // Additional delay to allow the call setup
        }


    }

    private fun StartVideoCall(targetUserId: String, targetName: String) {
        voiceCallButton.setIsVideoCall(true)
        voiceCallButton.resourceID = "zego_uikit_call"
        voiceCallButton.setInvitees(listOf(ZegoUIKitUser(targetUserId, targetName)))
        lifecycleScope.launch {
            delay(4000)  // 4-second delay before initiating the call
            voiceCallButton.performClick() // Programmatically click to start the call

            // Additional delay if needed to allow time for the call to start
            delay(4000)  // Additional delay to allow the call setup
            // Finish the activity after call initiation
        }
    }

}
