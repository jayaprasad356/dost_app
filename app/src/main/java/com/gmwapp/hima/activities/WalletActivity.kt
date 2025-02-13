package com.gmwapp.hima.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.CoinAdapter
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityWalletBinding
import com.gmwapp.hima.retrofit.responses.CoinsResponseData
import com.gmwapp.hima.retrofit.responses.RazorPayApiResponse
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.gmwapp.hima.viewmodels.AccountViewModel
import com.gmwapp.hima.viewmodels.UpiViewModel
import com.gmwapp.hima.viewmodels.WalletViewModel
import com.gmwapp.hima.widgets.SpacesItemDecoration
import com.google.androidbrowserhelper.trusted.LauncherActivity
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.internal.CallInviteActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WalletActivity : BaseActivity()  {
    lateinit var binding: ActivityWalletBinding
    private val WalletViewModel: WalletViewModel by viewModels()
    private val accountViewModel: AccountViewModel by viewModels()


    private val viewModel: UpiViewModel by viewModels()
    var amount = ""
    var pointsId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
        addObservers()
    }

    private fun initUI() {

        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        binding.tvCoins.text = userData?.coins.toString()


        val layoutManager = GridLayoutManager(this, 3)
        binding.ivBack.setOnSingleClickListener {
            finish()
        }
//        binding.rvPlans.addItemDecoration(SpacesItemDecoration(20))
        binding.rvPlans.setLayoutManager(layoutManager)
//        binding.rvPlans.addItemDecoration(SpacesItemDecoration(10))
        BaseApplication.getInstance()?.getPrefs()?.getUserData()?.let { WalletViewModel.getCoins(it.id) }
        WalletViewModel.coinsLiveData.observe(this, Observer {

            if(it.success){
              //  Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

            if (it!=null && it.success && it.data != null) {
                // Create the adapter
                val coinAdapter = CoinAdapter(this, it.data, object : OnItemSelectionListener<CoinsResponseData> {
                    override fun onItemSelected(coin: CoinsResponseData) {
                        // Update button text and make it visible when an item is selected
                        binding.btnAddCoins.text = getString(R.string.add_quantity_coins, coin.coins)
                        binding.btnAddCoins.visibility = View.VISIBLE
                        amount = coin.price.toString()
                        pointsId = coin.id.toString()

                    }

                    val number: CoinsResponseData?
                        get() = null
                })

                // Set the adapter
                binding.rvPlans.adapter = coinAdapter

                // Set default button text and visibility for the first item
                if (it.data.isNotEmpty()) {
                    val firstCoin = it.data[0]
                    binding.btnAddCoins.text = getString(R.string.add_quantity_coins, firstCoin.coins)
                    binding.btnAddCoins.visibility = View.VISIBLE
                    amount = firstCoin.price.toString()
                    pointsId = firstCoin.id.toString()

                }
            }

        })


        binding.btnAddCoins.setOnSingleClickListener {
            val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()

            val userId = userData?.id
            val name = userData?.name ?: ""
            val email = "test@gmail.com"
            val mobile = userData?.mobile ?: ""

            // get 2 percentage of amount
            val twoPercentage = amount.toDouble() * 0.02
            val roundedAmount = Math.round(twoPercentage)
            val total_amount = (amount.toDouble() + roundedAmount).toString()



            Log.d("Amount", "amount $amount")
            Log.d("Amount", "Totalamount $total_amount")
            Log.d("Amount", "Roundamount $roundedAmount")
            if (userId != null && pointsId.isNotEmpty() && total_amount.isNotEmpty()) {
                val userIdWithPoints = "$userId-$pointsId"

                val apiService = RetrofitClient.instance
                val call = apiService.addCoins(name, total_amount, email, mobile, userIdWithPoints)
                val callRazor = apiService.addCoinsRazorPay(userIdWithPoints,name,total_amount,email,mobile)

                accountViewModel.getSettings()

                accountViewModel.settingsLiveData.observe(this, Observer { response ->
                    if (response != null && response.success) {
                        response.data?.let { settingsList ->
                            if (settingsList.isNotEmpty()) {
                                val settingsData = settingsList[0]

                                when (settingsData.payment_gateway_type) {
                                    "razorpay" -> {


                                        callRazor.enqueue(object : retrofit2.Callback<RazorPayApiResponse> {
                                            override fun onResponse(call: retrofit2.Call<RazorPayApiResponse>, response: retrofit2.Response<RazorPayApiResponse>) {
                                                if (response.isSuccessful && response.body() != null) {
                                                    val apiResponse = response.body()

                                                    // Extract the Razorpay payment link
                                                    val paymentUrl = apiResponse?.short_url

                                                    if (!paymentUrl.isNullOrEmpty()) {

                                                        val intent =Intent(this@WalletActivity, LauncherActivity::class.java)
                                                        intent.setData(Uri.parse(response.body()?.short_url))
                                                        Log.d("WalletResponse","${response.body()?.short_url}")
                                                        startActivity(intent)

//                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
//                                startActivity(intent)
                                                    } else {
                                                        Toast.makeText(this@WalletActivity, "Failed to get payment link", Toast.LENGTH_SHORT).show()
                                                    }
                                                } else {
                                                    Toast.makeText(this@WalletActivity, "Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(call: retrofit2.Call<RazorPayApiResponse>, t: Throwable) {
                                                Toast.makeText(this@WalletActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        })





                                    }

                                    "instamojo" -> {


                                        call.enqueue(object : retrofit2.Callback<ApiResponse> {
                                            override fun onResponse(call: retrofit2.Call<ApiResponse>, response: retrofit2.Response<ApiResponse>) {
                                                if (response.isSuccessful && response.body()?.success == true) {
                                                    Toast.makeText(this@WalletActivity, response.body()?.message, Toast.LENGTH_SHORT).show()
                                                } else {
                                                    // println("Long URL: ${it.longurl}") // Print to the terminal
                                                    //Toast.makeText(mContext, it.longurl, Toast.LENGTH_SHORT).show()

                                                    val intent =
                                                        Intent(this@WalletActivity, LauncherActivity::class.java)
                                                    intent.setData(Uri.parse(response.body()?.longurl))
                                                    Log.d("WalletResponse","${response.body()?.longurl}")
                                                    startActivity(intent)
                                                    finish()// Directly starting the intent without launcher
                                                    //  Toast.makeText(this@WalletActivity, response.body()?.message ?: "Error", Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            override fun onFailure(call: retrofit2.Call<ApiResponse>, t: Throwable) {
                                                Toast.makeText(this@WalletActivity, "Failed: ${t.message}", Toast.LENGTH_SHORT).show()
                                            }
                                        })




                                    }
                                    else -> {
                                        Toast.makeText(this, "Invalid Payment Gateway", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }
                })













            } else {
                Toast.makeText(this, "Invalid input data", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun addObservers() {
        viewModel.addpointResponseLiveData.observe(this, Observer {
            if (it != null && it.success) {

                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()

            }
            else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        })

    }





}