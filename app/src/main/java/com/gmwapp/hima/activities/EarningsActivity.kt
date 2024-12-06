package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.adapters.EarningsAdapter
import com.gmwapp.hima.adapters.TransactionAdapter
import com.gmwapp.hima.databinding.ActivityTransactionsBinding
import com.gmwapp.hima.viewmodels.EarningsViewModel
import com.gmwapp.hima.viewmodels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EarningsActivity : BaseActivity() {
    lateinit var binding: ActivityTransactionsBinding
    private val earningsViewModel: EarningsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAddCoins.setOnClickListener({
            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
        })
        BaseApplication.getInstance()?.getPrefs()?.getUserData()
            ?.let { earningsViewModel.getEarnings(it.id) }
        earningsViewModel.earningsResponseLiveData.observe(this, Observer {
            if (it.data != null) {
                binding.rvTransactions.setLayoutManager(
                    LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                    ))

                var earningsAdapter = EarningsAdapter(this, it.data)
                binding.rvTransactions.setAdapter(earningsAdapter)
            }
        })

    }
}