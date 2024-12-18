package com.gmwapp.hima.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.adapters.TransactionAdapter
import com.gmwapp.hima.databinding.ActivityTransactionsBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.TransactionsViewModel
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
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnAddCoins.setOnSingleClickListener({
            val intent = Intent(this, WalletActivity::class.java)
            startActivity(intent)
        })
        BaseApplication.getInstance()?.getPrefs()?.getUserData()
            ?.let {

                if (this.let { it1 -> isInternetAvailable(it1) } == true) {
                    transactionsViewModel.getTransactions(it.id)
                } else {

                    binding.tvNointernet.visibility = View.VISIBLE

                }

            }
        transactionsViewModel.transactionsResponseLiveData.observe(this, Observer {

            if(it.success){
                //  Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

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



        // Check for Internet Connection
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        }
}