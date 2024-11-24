package com.gmwapp.dostt.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
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

    private fun initUI(){
        binding.cvLogin.setBackgroundResource(R.drawable.card_view_border);

        binding.btnLogin.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, VerifyOTPActivity::class.java)
            intent.putExtra(DConstants.MOBILE_NUMBER, binding.etMobileNumber.text.toString())
            startActivity(intent)

        })
        binding.etMobileNumber.setOnTouchListener { v, _ ->
            binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_active)
            false
        }
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
        setMessageWithClickableLink();
    }

    private fun setMessageWithClickableLink() {
        val content = getString(R.string.terms_and_conditions_text)
        val url = "http://my-site.com/information" //TODO need tp add the terms & conditions link
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent);
            }
            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = getColor(R.color.yellow)
                textPaint.isUnderlineText = false;
            }
        }
        val startIndex = content.indexOf("terms & conditions")
        val endIndex = startIndex + "terms & conditions".length
        val spannableString = SpannableString(content)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.tvTermsAndConditions.text = spannableString
        binding.tvTermsAndConditions.movementMethod = LinkMovementMethod.getInstance()
    }
}