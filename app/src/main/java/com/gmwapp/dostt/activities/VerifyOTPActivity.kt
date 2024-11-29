package com.gmwapp.dostt.activities

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityVerifyOtpBinding
import com.gmwapp.dostt.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VerifyOTPActivity : BaseActivity() {
    private var timer: CountDownTimer?=null
    lateinit var binding: ActivityVerifyOtpBinding
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerifyOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
        startTimer();
    }

    private fun initUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        val mobileNumber: String = intent.getStringExtra(DConstants.MOBILE_NUMBER).toString()
        val otp = intent.getIntExtra(DConstants.OTP, 0)
        val countryCode = intent.getIntExtra(DConstants.COUNTRY_CODE, 0)
        binding.tvOtpMobileNumber.text =
            getString(R.string.please_enter_otp_sent_to, mobileNumber)
        binding.ivEdit.setOnClickListener(View.OnClickListener {
            finish()
        })
        loginViewModel.sendOTPResponseLiveData.observe(this, Observer {
            binding.pbLoader.visibility = View.GONE
            binding.btnResendOtp.setText(getString(R.string.resend_otp))
            binding.btnResendOtp.visibility = View.GONE
            binding.tvDidntReceivedOtpTimer.visibility = View.VISIBLE
            startTimer()
        })

        loginViewModel.loginErrorLiveData.observe(this, Observer {
            binding.pbVerifyOtpLoader.visibility = View.GONE
            binding.btnVerifyOtp.setText(getString(R.string.verify_otp))
            binding.btnVerifyOtp.isEnabled = true
        })
        loginViewModel.loginResponseLiveData.observe(this, Observer {
            binding.pbVerifyOtpLoader.visibility = View.GONE
            binding.btnVerifyOtp.setText(getString(R.string.verify_otp))
            binding.btnVerifyOtp.isEnabled = true
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

        binding.btnResendOtp.setOnClickListener({
            binding.btnResendOtp.setText("")
            binding.pbLoader.visibility = View.VISIBLE
            loginViewModel.sendOTP(mobileNumber, countryCode, otp)
        })
        binding.pvOtp.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                if(s.toString().length==6){
                    binding.btnVerifyOtp.setBackgroundResource(R.drawable.d_button_bg_white)
                    binding.btnVerifyOtp.isEnabled = true
                }else{
                    binding.btnVerifyOtp.setBackgroundResource(R.drawable.d_button_bg)
                    binding.btnVerifyOtp.isEnabled = false
                }
            }
        }
        )
        binding.btnVerifyOtp.setOnClickListener {
            val enteredOTP = binding.pvOtp.text.toString().toInt()
            if (enteredOTP == otp) {
                binding.pbVerifyOtpLoader.visibility = View.VISIBLE
                binding.btnVerifyOtp.setText("")
                login(mobileNumber)
            }
        }
    }

    private fun startTimer(){
        timer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val time = millisUntilFinished / 1000
                binding.tvDidntReceivedOtpTimer.setText(getString(R.string.retry_in, if(time<10) "0$time" else time.toString()))
            }

            override fun onFinish() {
                binding.btnResendOtp.visibility = View.VISIBLE
                binding.tvDidntReceivedOtpTimer.visibility = View.GONE
            }
        }.start()
    }

    private fun login(mobile: String) {
        loginViewModel.login(mobile)
    }
}