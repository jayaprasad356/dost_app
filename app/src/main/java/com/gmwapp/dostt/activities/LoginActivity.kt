package com.gmwapp.dostt.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import com.gmwapp.dostt.R
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding


class LoginActivity : BaseActivity() {
    lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initUI(){
        binding.cvLogin.setBackgroundResource(R.drawable.card_view_border);

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VerifyOTPActivity::class.java)
            intent.putExtra(DConstants.MOBILE_NUMBER, binding.etMobileNumber.text.toString())
            startActivity(intent)

        })
        binding.etMobileNumber.setOnTouchListener(OnTouchListener { v, event ->
            binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_active)
            false
        })
        binding.etMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                window.statusBarColor = resources.getColor(R.color.dark_blue);
                if(TextUtils.isEmpty(s)){
                    binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg)
                }else{
                    binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg_white)
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}