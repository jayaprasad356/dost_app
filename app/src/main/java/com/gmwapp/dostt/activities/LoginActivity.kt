package com.gmwapp.dostt.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.gmwapp.dostt.R
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cvLogin.setBackgroundResource(R.drawable.card_view_border);

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VerifyOTPActivity::class.java)
            intent.putExtra(DConstants.MOBILE_NUMBER, binding.etMobileNumber.text.toString())
            startActivity(intent)

        })
    }
}