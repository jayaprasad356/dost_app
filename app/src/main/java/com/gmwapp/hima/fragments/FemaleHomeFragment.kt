package com.gmwapp.hima.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.activities.EarningsActivity
import com.gmwapp.hima.activities.GrantPermissionsActivity
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.FragmentFemaleHomeBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.zegocloud.uikit.ZegoUIKit
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class FemaleHomeFragment : BaseFragment() {
    private val CALL_PERMISSIONS_REQUEST_CODE = 1
    lateinit var binding: FragmentFemaleHomeBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Set to IST time zone
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFemaleHomeBinding.inflate(layoutInflater)

        initUI()
        askPermissions();
        return binding.root
    }

    fun askPermissions() {
        val permissionNeeded =
            arrayOf("android.permission.RECORD_AUDIO", "android.permission.CAMERA")

        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it, "android.permission.CAMERA"
                )
            } != PackageManager.PERMISSION_GRANTED || context?.let {
                ContextCompat.checkSelfPermission(
                    it, "android.permission.RECORD_AUDIO"
                )
            } != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissionNeeded, CALL_PERMISSIONS_REQUEST_CODE)
        }else{
            checkOverlayPermission()
        }
    }

    private fun checkOverlayPermission() {
        PermissionX.init(requireActivity()).permissions(Manifest.permission.SYSTEM_ALERT_WINDOW)
            .onExplainRequestReason(ExplainReasonCallback { scope, deniedList ->
                val message =
                    "We need your consent for the following permissions in order to use the offline call function properly"
                scope.showRequestReasonDialog(deniedList, message, "Allow", "Deny")
            }).request(RequestCallback { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    initializeCall()
                } else {
                    checkOverlayPermission()
                }
            })

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CALL_PERMISSIONS_REQUEST_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToCamera = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToRecord = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (!(permissionToCamera && permissionToRecord)) {
                    val intent = Intent(context, GrantPermissionsActivity::class.java)
                    startActivity(intent)
                } else {
                    checkOverlayPermission()
                }
            }
        }
    }

    private fun initializeCall() {
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()
        if (userData != null) {
            setupZegoUIKit(userData.id, userData.name)
            addRoomStateChangedListener()
        }
    }

    private fun initUI() {
        binding.clCoins.setOnSingleClickListener({
            val intent = Intent(context, EarningsActivity::class.java)
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
                binding.sAudio.isChecked = prefs?.getUserData()?.audio_status == 1
                binding.sVideo.isChecked = prefs?.getUserData()?.video_status == 1
            }
        })
        femaleUsersViewModel.updateCallStatusErrorLiveData.observe(viewLifecycleOwner, Observer {
            showErrorMessage(it)
            binding.sAudio.isChecked = prefs?.getUserData()?.audio_status == 1
            binding.sVideo.isChecked = prefs?.getUserData()?.video_status == 1
        })
        binding.sAudio.setOnCheckedChangeListener({ buttonView, isChecked ->
            userData?.id?.let {
                femaleUsersViewModel.updateCallStatus(
                    it, DConstants.AUDIO, if (isChecked) 1 else 0
                )
            }
            if (isChecked) {

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

    private fun addRoomStateChangedListener() {

        ZegoUIKit.addRoomStateChangedListener { room, reason, _, _ ->
            when (reason) {
                ZegoRoomStateChangedReason.LOGINED -> {
                    val prefs = BaseApplication.getInstance()?.getPrefs()
                    val userData = prefs?.getUserData()

                    femaleUsersViewModel.femaleCallAttendResponseLiveData.observe(viewLifecycleOwner, Observer {
                        if(it!=null && it.success){
                            balanceTime = it.data?.remaining_time;
                        }
                    })
                    var startTime = dateFormat.format(Date()) // Set call start time in IST
                    femaleUsersViewModel.femaleCallAttend(receivedId,callId, startTime)

                }

                ZegoRoomStateChangedReason.LOGOUT -> {
                }

                else -> {
                }
            }
        }
    }

}