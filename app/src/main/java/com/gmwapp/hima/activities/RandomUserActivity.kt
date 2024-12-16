package com.gmwapp.hima.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityRandomUserBinding
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.workers.CallUpdateWorker
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.zegocloud.uikit.ZegoUIKit
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallType
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoCallUser
import com.zegocloud.uikit.prebuilt.call.invite.internal.ZegoInvitationCallListener
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class RandomUserActivity : BaseActivity() {
    private val CALL_PERMISSIONS_REQUEST_CODE = 1
    lateinit var binding: ActivityRandomUserBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()
    lateinit var activity: Activity
    private var isAlreadyCalled = false
    private var roomID: String? = null

    private var userId: String = ""
    private var callUserId: String = ""
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
            val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
            val callType = intent.getStringExtra(DConstants.CALL_TYPE)
            userData?.let {
                callType?.let { it1 -> femaleUsersViewModel.getRandomUser(it.id, it1) }
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
                    val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
                    val callType = intent.getStringExtra(DConstants.CALL_TYPE)
                    userData?.let {
                        callType?.let { it1 -> femaleUsersViewModel.getRandomUser(it.id, it1) }
                    }
                } else {
                    finish()
                    val intent = Intent(this, GrantPermissionsActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        askPermissions()
    }

    private fun initUI() {
        val callType = intent.getStringExtra(DConstants.CALL_TYPE)
        binding.btnCancel.setOnClickListener({
            finish()
        })
        femaleUsersViewModel.randomUsersResponseLiveData.observe(this, Observer {
            if (it.success) {
                setupCall(
                    it.data?.call_user_id.toString(),
                    it.data?.call_user_name.toString(),
                    callType.toString()
                )
                it.data?.call_id?.let { it1 -> addRoomStateChangedListener(it1) }
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
    }

    private fun addRoomStateChangedListener(callId: Int) {
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
                    finish()
                }

                override fun onIncomingCallTimeout(callID: String?, caller: ZegoCallUser?) {
                    finish()
                }

                override fun onOutgoingCallAccepted(callID: String?, callee: ZegoCallUser?) {
                }


                override fun onOutgoingCallRejectedCauseBusy(
                    callID: String?, callee: ZegoCallUser?
                ) {
                    Toast.makeText(
                        this@RandomUserActivity,
                        getString(R.string.call_rejected),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onOutgoingCallDeclined(callID: String?, callee: ZegoCallUser?) {
                    Toast.makeText(
                        this@RandomUserActivity,
                        getString(R.string.call_rejected),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }

                override fun onOutgoingCallTimeout(
                    callID: String?, callees: MutableList<ZegoCallUser>?
                ) {
                    Toast.makeText(
                        this@RandomUserActivity,
                        getString(R.string.call_timeout),
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()
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
                }

                ZegoRoomStateChangedReason.LOGOUT -> {
                    endTime = dateFormat.format(Date()) // Set call end time in IST
                    val constraints =
                        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                    val data: Data = Data.Builder().putInt(DConstants.CALL_ID, callId)
                        .putString(DConstants.STARTED_TIME, startTime)
                        .putString(DConstants.ENDED_TIME, endTime).build()
                    val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                        CallUpdateWorker::class.java
                    ).setInputData(data).setConstraints(constraints).build()
                    WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)
                    finish()
                }

                else -> {
                    finish()
                }
            }
        }
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
        binding.voiceCallButton.resourceID = "zego_call"
        val user = ZegoUIKitUser(targetUserId, targetName)
        user.avatar = BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image;
        binding.voiceCallButton.setInvitees(listOf(user))
        binding.voiceCallButton.performClick() // Programmatically click to start the call
    }

    private fun StartVideoCall(targetUserId: String, targetName: String) {
        binding.voiceCallButton.setIsVideoCall(true)
        binding.voiceCallButton.resourceID = "zego_call"
        val user = ZegoUIKitUser(targetUserId, targetName)
        user.avatar = BaseApplication.getInstance()?.getPrefs()?.getUserData()?.image;
        binding.voiceCallButton.setInvitees(listOf(user))
        binding.voiceCallButton.performClick()
    }
}
