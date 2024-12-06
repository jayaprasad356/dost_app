package com.gmwapp.hima.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.BuildConfig
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.EarningsAdapter
import com.gmwapp.hima.adapters.TransactionAdapter
import com.gmwapp.hima.databinding.ActivityEarningsBinding
import com.gmwapp.hima.databinding.ActivityTransactionsBinding
import com.gmwapp.hima.viewmodels.AccountViewModel
import com.gmwapp.hima.viewmodels.EarningsViewModel
import com.gmwapp.hima.viewmodels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EarningsActivity : BaseActivity() {
    lateinit var binding: ActivityEarningsBinding
    private val earningsViewModel: EarningsViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEarningsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        val prefs = BaseApplication.getInstance()?.getPrefs()
        val supportMail = prefs?.getSettingsData()?.support_mail
        binding.tvSupportMail.text =
            supportMail
        prefs?.getUserData()?.id?.let { accountViewModel.getSettings(it) }
        BaseApplication.getInstance()?.getPrefs()?.getUserData()
            ?.let { earningsViewModel.getEarnings(it.id) }
        accountViewModel.settingsLiveData.observe(this, Observer {
            if (it.success) {
                if (it.data != null) {
                    if (it.data.size > 0) {
                        prefs?.setSettingsData(it.data.get(0))
                        val supportMail = prefs?.getSettingsData()?.support_mail
                        binding.tvSupportMail.text =
                            supportMail
                        val userData = prefs?.getUserData()
                        val subject = getString(R.string.mail_subject, userData?.mobile)

                        val body = getString(
                            R.string.mail_body, userData?.mobile,android.os.Build.MODEL,userData?.language,
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
        earningsViewModel.earningsResponseLiveData.observe(this, Observer {
            if (it.data != null) {
                binding.rvEarnings.setLayoutManager(
                    LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                    ))

                var earningsAdapter = EarningsAdapter(this, it.data)
                binding.rvEarnings.setAdapter(earningsAdapter)
            }
        })

    }
}