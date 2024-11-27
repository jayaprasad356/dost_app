package com.gmwapp.dostt.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gmwapp.dostt.BaseApplication
import com.gmwapp.dostt.R
import com.gmwapp.dostt.adapters.CoinAdapter
import com.gmwapp.dostt.callbacks.OnItemSelectionListener
import com.gmwapp.dostt.databinding.ActivityWalletBinding
import com.gmwapp.dostt.retrofit.responses.CoinsResponseData
import com.gmwapp.dostt.viewmodels.WalletViewModel
import com.gmwapp.dostt.widgets.SpacesItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletActivity : BaseActivity() {
    lateinit var binding: ActivityWalletBinding
    private val WalletViewModel: WalletViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val layoutManager = GridLayoutManager(this, 3)
        binding.rvPlans.addItemDecoration(SpacesItemDecoration(20))
        binding.rvPlans.setLayoutManager(layoutManager)
        binding.rvPlans.addItemDecoration(SpacesItemDecoration(10))
        BaseApplication.getInstance()?.getPrefs()?.getUserData()?.let { WalletViewModel.getCoins(it.id) }
        WalletViewModel.coinsLiveData.observe(this, Observer {
            if(it.success && it.data!=null) {
                val coinAdapter = CoinAdapter(this,it.data,object :
                    OnItemSelectionListener<CoinsResponseData> {
                    override fun onItemSelected(coin: CoinsResponseData) {
                        binding.btnAddCoins.text = getString(R.string.add_quantity_coins, coin.coins)
                        binding.btnAddCoins.visibility = View.VISIBLE
                    }
                })
                binding.rvPlans.setAdapter(coinAdapter)

            }
        })
    }
}