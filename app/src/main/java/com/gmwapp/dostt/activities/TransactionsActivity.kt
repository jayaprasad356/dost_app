package com.gmwapp.dostt.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.adapters.TransactionAdapter
import com.gmwapp.dostt.databinding.ActivityTransactionsBinding
import com.gmwapp.dostt.retrofit.responses.TransactionsResponseData
import com.gmwapp.dostt.viewmodels.TransactionsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransactionsActivity : BaseActivity() {
    lateinit var binding: ActivityTransactionsBinding
    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        BaseApplication.getInstance()?.getPrefs()?.getUserId()
            ?.let { transactionsViewModel.getTransactions(it) }
        transactionsViewModel.transactionsResponseLiveData.observe(this, Observer {
            if (it.data != null) {
                binding.rvTransactions.setLayoutManager(
                    LinearLayoutManager(
                        this,
                        LinearLayoutManager.VERTICAL,
                        false
                    ))

                var transactionAdapter = TransactionAdapter(this, it.data)
                binding.rvTransactions.setAdapter(transactionAdapter)
            }
        })

    }
}