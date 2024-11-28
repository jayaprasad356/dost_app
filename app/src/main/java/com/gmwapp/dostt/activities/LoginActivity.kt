package com.gmwapp.dostt.activities

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
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.callbacks.OnItemSelectionListener
import com.gmwapp.dostt.constants.DConstants
import com.gmwapp.dostt.databinding.ActivityLoginBinding
import com.gmwapp.dostt.dialogs.BottomSheetCountry
import com.gmwapp.dostt.retrofit.responses.Country
import com.gmwapp.dostt.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random


@AndroidEntryPoint
class LoginActivity : BaseActivity(), OnItemSelectionListener<Country> {
    lateinit var binding: ActivityLoginBinding
    var mobile: String? = null
    var otp:Int?= null;
    private val loginViewModel: LoginViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    override fun onItemSelected(country: Country) {
        binding.ivFlag.setImageResource(country.image)
        binding.tvCountryCode.text = country.code
    }

    private fun initUI() {
        binding.cvLogin.setBackgroundResource(R.drawable.card_view_border)

        binding.btnLogin.setOnClickListener {
            binding.btnLogin.isEnabled = false
            var mobile = binding.etMobileNumber.text.toString()
            if (TextUtils.isEmpty(mobile) || mobile.length < 10) {
                binding.tvOtpText.text = getString(R.string.invalid_phone_number_text)
                binding.tvOtpText.setTextColor(getColor(R.color.error))
                binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_error)
            } else {
                val r = Random(System.currentTimeMillis())
                val randomNumber = 1 + r.nextInt(2) * 100000 + r.nextInt(100000)

                sendOTP(mobile, binding.tvCountryCode.text.toString().toInt(), randomNumber)
            }
        }
        binding.clCountry.setOnClickListener {
            val bottomSheet = BottomSheetCountry()
            bottomSheet.show(
                supportFragmentManager, "BottomSheetCountry"
            )
        }
        binding.etMobileNumber.setOnTouchListener { v, _ ->
            binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_active)
            false
        }
        binding.cbTermsAndConditions.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg_white)
                binding.btnLogin.isEnabled = true
            } else {
                binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg)
                binding.btnLogin.isEnabled = false
            }
        }
        binding.etMobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                window.statusBarColor = resources.getColor(R.color.dark_blue)
                if (!binding.cbTermsAndConditions.isChecked || TextUtils.isEmpty(s)) {
                    binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg)
                    binding.btnLogin.isEnabled = false
                } else {
                    binding.btnLogin.setBackgroundResource(R.drawable.d_button_bg_white)
                    binding.btnLogin.isEnabled = true
                }
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        loginViewModel.sendOTPResponseLiveData.observe(this, Observer {
            binding.btnLogin.isEnabled = true
            if (it.success) {
                val intent = Intent(this, VerifyOTPActivity::class.java)
                intent.putExtra(DConstants.MOBILE_NUMBER, mobile)
                intent.putExtra(DConstants.OTP, otp)
                startActivity(intent)
            } else {
                binding.tvOtpText.text = it.message
                binding.tvOtpText.setTextColor(getColor(R.color.error))
                binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_error)
            }
        })
        loginViewModel.sendOTPErrorLiveData.observe(this, Observer {
            binding.btnLogin.isEnabled = true
            binding.tvOtpText.text = getString(R.string.please_try_again_later)
            binding.tvOtpText.setTextColor(getColor(R.color.error))
            binding.cvLogin.setBackgroundResource(R.drawable.card_view_border_error)
        })

        setMessageWithClickableLink()
    }

    private fun sendOTP(mobile: String, countryCode:Int, otp:Int) {
        this.mobile = mobile
        loginViewModel.sendOTP(mobile, countryCode, otp)
    }

    private fun setMessageWithClickableLink() {
        val content = getString(R.string.terms_and_conditions_text)
        val url = "http://my-site.com/information" //TODO need to add the terms & conditions link
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                startActivity(intent)
            }

            override fun updateDrawState(textPaint: TextPaint) {
                super.updateDrawState(textPaint)
                textPaint.color = getColor(R.color.yellow)
                textPaint.isUnderlineText = false
            }
        }
        val startIndex = content.indexOf("terms & conditions")
        val endIndex = startIndex + "terms & conditions".length
        val spannableString = SpannableString(content)
        spannableString.setSpan(
            clickableSpan,
            startIndex,
            endIndex,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvTermsAndConditions.text = spannableString
        binding.tvTermsAndConditions.movementMethod = LinkMovementMethod.getInstance()
    }

    override val number: Country
        get() = TODO("Not yet implemented")
}