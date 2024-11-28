package com.gmwapp.dostt.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding
import com.gmwapp.dostt.databinding.ActivityVerifyOtpBinding
import com.gmwapp.dostt.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOTPActivity : BaseActivity() {
    lateinit var binding: ActivityVerifyOtpBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val mobileNumber: String = intent.getStringExtra(DConstants.MOBILE_NUMBER).toString()
        if (mobileNumber != null) {
            binding.tvOtpMobileNumber.text =
                getString(R.string.please_enter_otp_sent_to, mobileNumber)
        }
        loginViewModel.loginResponseLiveData.observe(this, Observer {
            if (it.success) {
                if (it.registered) {
                    it.data?.let { it1 ->
                        BaseApplication.getInstance()?.getPrefs()?.setUserData(it1)
                    }
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this, SelectGenderActivity::class.java)
                    startActivity(intent)
                }
            }
        })
        binding.btnVerifyOtp.setOnClickListener {
            val enteredOTP = binding.pvOtp.text.toString().toInt()
            val otp = intent.getIntExtra(DConstants.MOBILE_NUMBER, 0)
            if (enteredOTP == otp) {
                login(mobileNumber)
            }

            if (BaseApplication.getInstance()?.getPrefs()?.isRegistered == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, SelectGenderActivity::class.java)
                intent.putExtra(
                    DConstants.MOBILE_NUMBER, getIntent().getStringExtra(DConstants.MOBILE_NUMBER)
                )
                startActivity(intent)
            }
        }
    }

    private fun login(mobile: String) {
        loginViewModel.login(mobile)
    }
}