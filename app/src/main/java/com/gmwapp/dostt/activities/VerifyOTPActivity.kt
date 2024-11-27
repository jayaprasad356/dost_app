package com.gmwapp.dostt.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding
import com.gmwapp.dostt.databinding.ActivityVerifyOtpBinding


class VerifyOTPActivity : BaseActivity() {
    lateinit var binding: ActivityVerifyOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val mobileNumber:String? = intent.getStringExtra(DConstants.MOBILE_NUMBER);
        if(mobileNumber!=null){
            binding.tvOtpMobileNumber.text = getString(R.string.please_enter_otp_sent_to, mobileNumber)
        }
        binding.btnVerifyOtp.setOnClickListener {
            if (BaseApplication.getInstance()?.getPrefs()?.isRegistered == true) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, SelectGenderActivity::class.java)
                intent.putExtra(
                    DConstants.MOBILE_NUMBER,
                    getIntent().getStringExtra(DConstants.MOBILE_NUMBER)
                )
                startActivity(intent)
            }
        }
    }
}