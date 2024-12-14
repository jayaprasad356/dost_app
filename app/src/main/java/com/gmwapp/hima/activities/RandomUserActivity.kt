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
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityCallBinding
import com.gmwapp.hima.databinding.ActivityMainBinding
import com.gmwapp.hima.databinding.ActivityRandomUserBinding
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
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
                val intent = Intent(this, CallActivity::class.java)
                intent.putExtra(DConstants.CALL_ID, it.data?.call_id)
                intent.putExtra(DConstants.CALL_TYPE, callType)
                intent.putExtra(DConstants.RECEIVER_ID, it.data?.call_user_id)
                intent.putExtra(DConstants.RECEIVER_NAME, it.data?.call_user_name)
                startActivity(intent)
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


}
