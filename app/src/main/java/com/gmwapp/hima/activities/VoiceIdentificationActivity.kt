package com.gmwapp.hima.activities

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.callbacks.OnButtonClickListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityFemaleAboutBinding
import com.gmwapp.hima.dialogs.BottomSheetVoiceIdentification
import com.gmwapp.hima.viewmodels.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


@AndroidEntryPoint
class VoiceIdentificationActivity : BaseActivity(), OnButtonClickListener {
    lateinit var binding: ActivityFemaleAboutBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val REQUEST_AUDIO_PERMISSION_CODE: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFemaleAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.size > 0) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                    openVoiceIdentificationPopup()
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                    requestPermissions()
                }
            }
        }
    }

    private fun openVoiceIdentificationPopup() {
        BaseApplication.getInstance()?.getPrefs()?.getUserData()?.id?.let {
            intent.getStringExtra(DConstants.LANGUAGE)?.let { it1 ->
                profileViewModel.getSpeechText(
                    it, it1
                )
            }
        }
        profileViewModel.speechTextLiveData.observe(this, Observer {
            val data = it.data
            if (it.success && data != null && data.size > 0) {
                val bottomSheet = BottomSheetVoiceIdentification()
                val bundle = Bundle()
                bundle.putString(DConstants.TEXT, data[0].text)
                bottomSheet.arguments = bundle
                bottomSheet.show(
                    supportFragmentManager, "BottomSheetVoiceIdentification"
                )
            } else {
                Toast.makeText(
                    this@VoiceIdentificationActivity, it.message, Toast.LENGTH_LONG
                ).show()
            }
        })
        profileViewModel.speechTextErrorLiveData.observe(this, Observer {
            Toast.makeText(
                this@VoiceIdentificationActivity,
                getString(R.string.please_try_again_later),
                Toast.LENGTH_LONG
            ).show()
        })
    }

    private fun initUI() {
        if (checkPermissions()) {
            openVoiceIdentificationPopup()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(this, WRITE_EXTERNAL_STORAGE)
        val recordPermission = ContextCompat.checkSelfPermission(this, RECORD_AUDIO)
        return writePermission == PackageManager.PERMISSION_GRANTED && recordPermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf<String>(RECORD_AUDIO, WRITE_EXTERNAL_STORAGE),
            REQUEST_AUDIO_PERMISSION_CODE
        )
    }

    override fun onButtonClick() {
        val file: File =
            File(Environment.getExternalStorageDirectory().absolutePath + "/AudioRecording.3gp")
        BaseApplication.getInstance()?.getPrefs()?.getUserData()?.id?.let {
            profileViewModel.updateVoice(
                it, MultipartBody.Part.createFormData(
                        "voice", file.name, file.asRequestBody()
                    )
            )
        }
    }
}