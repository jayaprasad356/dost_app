package com.gmwapp.hima.activities

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.BuildConfig
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.EarningsAdapter
import com.gmwapp.hima.databinding.ActivityEarningsBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.AccountViewModel
import com.gmwapp.hima.viewmodels.EarningsViewModel
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
        binding.ivBack.setOnSingleClickListener {
            finish()
        }

        val prefs = BaseApplication.getInstance()?.getPrefs()
        val settingsData = prefs?.getSettingsData()
        val supportMail = settingsData?.support_mail
        binding.tvSupportMail.text = supportMail
        binding.tlBalanceHint.text =
            getString(R.string.balance_hint_text, settingsData?.minimum_withdrawals)
        accountViewModel.getSettings()
        prefs?.getUserData()?.let {
                earningsViewModel.getEarnings(it.id)
                val balance = it.balance
                if (settingsData?.minimum_withdrawals != null && balance != null && balance < settingsData.minimum_withdrawals) {
                    binding.btnWithdraw.visibility = View.GONE
                    binding.ivBalance.visibility = View.VISIBLE
                    binding.tlBalanceHint.visibility = View.VISIBLE
                } else {
                    binding.btnWithdraw.visibility = View.VISIBLE
                    binding.ivBalance.visibility = View.GONE
                    binding.tlBalanceHint.visibility = View.GONE
                }
                binding.tvCurrentBalance.text = balance.toString()
            }
        accountViewModel.settingsLiveData.observe(this, Observer {
            if (it!=null && it.success) {
                if (it.data != null) {
                    if (it.data.size > 0) {
                        val settingsData1 = it.data[0]
                        prefs?.setSettingsData(settingsData1)
                        val supportMail = settingsData1.support_mail
                        binding.tlBalanceHint.text =
                            getString(R.string.balance_hint_text, settingsData1.minimum_withdrawals)
                        binding.tvSupportMail.text = supportMail
                        val userData = prefs?.getUserData()

                        val subject = getString(
                            R.string.delete_account_mail_subject,
                            userData?.mobile,
                            userData?.language
                        )

                        val body = getString(
                            R.string.mail_body,
                            userData?.mobile,
                            android.os.Build.MODEL,
                            userData?.language,
                            BuildConfig.VERSION_CODE
                        )
                        binding.tvSupportMail.setOnSingleClickListener {
                            val intent = Intent(Intent.ACTION_VIEW)

                            val data =
                                Uri.parse(("mailto:$supportMail?subject=$subject").toString() + "&body=$body")
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
                        this, LinearLayoutManager.VERTICAL, false
                    )
                )

                var earningsAdapter = EarningsAdapter(this, it.data)
                binding.rvEarnings.setAdapter(earningsAdapter)
            }
        })

    }
}