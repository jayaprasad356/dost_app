package com.gmwapp.hima.fragments

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.activities.EarningsActivity
import com.gmwapp.hima.activities.GrantPermissionsActivity
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.FragmentFemaleHomeBinding
import com.gmwapp.hima.retrofit.callbacks.NetworkCallback
import com.gmwapp.hima.retrofit.responses.FemaleCallAttendResponse
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.workers.CallUpdateWorker
import com.judemanutd.autostarter.AutoStartPermissionHelper
import com.permissionx.guolindev.PermissionX
import com.permissionx.guolindev.callback.ExplainReasonCallback
import com.permissionx.guolindev.callback.RequestCallback
import com.zegocloud.uikit.ZegoUIKit
import dagger.hilt.android.AndroidEntryPoint
import im.zego.zegoexpress.constants.ZegoRoomStateChangedReason
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import xyz.kumaraswamy.autostart.Autostart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone


@AndroidEntryPoint
class FemaleHomeFragment : BaseFragment() {
    private var checkingOverlayPermission: Boolean = false
    private val CALL_PERMISSIONS_REQUEST_CODE = 1
    lateinit var binding: FragmentFemaleHomeBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()
    private val dateFormat = SimpleDateFormat("HH:mm:ss").apply {
        timeZone = TimeZone.getTimeZone("Asia/Kolkata") // Set to IST time zone
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            initializeCall()
        } else {
            askNotificationPermission(); }
    }
    private var startTime: String = ""
    private var endTime: String = ""
    private var roomID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFemaleHomeBinding.inflate(layoutInflater)

        initUI()
        askPermissions()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (!checkingOverlayPermission) {
            checkAutoStartPermission()
        }
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
        } else {
            checkOverlayPermission()
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                initializeCall()
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                val intent = Intent(context, GrantPermissionsActivity::class.java)
                startActivity(intent)
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            initializeCall()
        }
    }


    private fun checkAutoStartPermission() {
        if (AutoStartPermissionHelper.getInstance()
                .isAutoStartPermissionAvailable(requireActivity())
        ) {
            when (Autostart.getAutoStartState(requireActivity())) {
                Autostart.State.ENABLED -> {
                    askNotificationPermission()
                }

                Autostart.State.DISABLED -> {
                    AutoStartPermissionHelper.getInstance()
                        .getAutoStartPermission(requireActivity())
                }

                Autostart.State.UNEXPECTED_RESULT, Autostart.State.NO_INFO -> {
                    askNotificationPermission()
                }
            }
        } else {
            askNotificationPermission()
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
                    checkingOverlayPermission = false
                    checkAutoStartPermission()
                } else {
                    checkingOverlayPermission = true
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

        binding.tvCoins.text = "₹" + userData?.balance.toString()


        femaleUsersViewModel.getReports(userData?.id!!)


        femaleUsersViewModel.reportResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it.success) {

                binding.tvApproxEarnings.text = it.data[0].today_earnings.toString()
                binding.tvTotalCalls.text = it.data[0].today_calls.toString()

            } else {
              //  Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
        })

        femaleUsersViewModel.updateCallStatusResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it!=null && it.success) {
                prefs?.setUserData(it.data)
            } else {
                Toast.makeText(context, it?.message, Toast.LENGTH_SHORT).show()
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

                    roomID = room
                    startTime = dateFormat.format(Date()) // Set call start time in IST
                    femaleUsersViewModel.femaleCallAttend(
                        receivedId,
                        callId,
                        startTime,
                        object : NetworkCallback<FemaleCallAttendResponse> {
                            override fun onResponse(
                                call: Call<FemaleCallAttendResponse>,
                                response: Response<FemaleCallAttendResponse>
                            ) {
                                balanceTime = response.body()?.data?.remaining_time
                            }

                            override fun onFailure(
                                call: Call<FemaleCallAttendResponse>,
                                t: Throwable
                            ) {
                            }

                            override fun onNoNetwork() {
                            }
                        })

                }

                ZegoRoomStateChangedReason.LOGOUT -> {
                    lifecycleScope.launch {
                        delay(500)
                        if (roomID != null) {
                            roomID = null
                            endTime = dateFormat.format(Date()) // Set call end time in IST

                            val constraints =
                                Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                            val data: Data = Data.Builder()
                                .putInt(DConstants.USER_ID, receivedId)
                                .putInt(DConstants.CALL_ID, callId)
                                .putString(DConstants.STARTED_TIME, startTime)
                                .putString(DConstants.ENDED_TIME, endTime).build()
                            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
                                CallUpdateWorker::class.java
                            ).setInputData(data).setConstraints(constraints).build()
                            WorkManager.getInstance(requireActivity())
                                .enqueue(oneTimeWorkRequest)
                        }}
                }

                else -> {
                }
            }
        }
    }

}