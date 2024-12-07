package com.gmwapp.hima.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.BuildConfig
import com.gmwapp.hima.R
import com.gmwapp.hima.databinding.ActivityAlmostDoneBinding
import com.gmwapp.hima.viewmodels.AccountViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AlmostDoneActivity : BaseActivity() {
    lateinit var binding: ActivityAlmostDoneBinding
    private val accountViewModel: AccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlmostDoneBinding.inflate(layoutInflater)
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val supportMail = prefs?.getSettingsData()?.support_mail
        binding.tvSupportMail.text =
            supportMail
        prefs?.getUserData()?.id?.let { accountViewModel.getSettings(it) }
        accountViewModel.settingsLiveData.observe(this, Observer {
            if (it.success) {
                if (it.data != null) {
                    if (it.data.size > 0) {
                        prefs?.setSettingsData(it.data.get(0))
                        val supportMail = prefs?.getSettingsData()?.support_mail
                        binding.tvSupportMail.text =
                            supportMail
                        val userData = prefs?.getUserData()
                        val subject = getString(R.string.delete_account_mail_subject, userData?.mobile,  userData?.language)

                        val body = getString(R.string.mail_body, userData?.mobile,android.os.Build.MODEL,userData?.language,
                            BuildConfig.VERSION_CODE
                        )
                        binding.tvSupportMail.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)

                            val data = Uri.parse(("mailto:$supportMail?subject=$subject").toString() + "&body=$body")
                            intent.setData(data)

                            startActivity(intent)
                        }
                        binding.tvSupportMail.paintFlags =
                            binding.tvSupportMail.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                    }
                }
            }
        })
        setContentView(binding.root)
    }

}