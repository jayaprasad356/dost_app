package com.gmwapp.hima.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.activities.WalletActivity
import com.gmwapp.hima.adapters.FemaleUserAdapter
import com.gmwapp.hima.adapters.TransactionAdapter
import com.gmwapp.hima.databinding.FragmentHomeBinding
import com.gmwapp.hima.retrofit.responses.FemaleUsersResponseData
import com.gmwapp.hima.viewmodels.FemaleUsersViewModel
import com.gmwapp.hima.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class HomeFragment : BaseFragment() {
    private var isAllFabVisible: Boolean = false
    lateinit var binding: FragmentHomeBinding
    private val femaleUsersViewModel: FemaleUsersViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }


    private fun initUI(){
        binding.clCoins.setOnClickListener({
            val intent = Intent(context, WalletActivity::class.java)
            startActivity(intent)
        })
        BaseApplication.getInstance()?.getPrefs()?.getUserData()?.id?.let {
            femaleUsersViewModel.getFemaleUsers(
                it
            )
        }

        femaleUsersViewModel.femaleUsersResponseLiveData.observe(viewLifecycleOwner, Observer {
            if (it?.data != null) {
                binding.rvProfiles.setLayoutManager(
                    LinearLayoutManager(
                        activity,
                        LinearLayoutManager.VERTICAL,
                        false
                    )
                )

                var transactionAdapter = activity?.let { it1 -> FemaleUserAdapter(it1, it.data) }
                binding.rvProfiles.setAdapter(transactionAdapter)
            }
        })

        initFab()
    }

    fun initFab(){


        val htmlText = " Video <img src='coin_d'/> 60/min"
        val htmlText1 = " Audio <img src='coin_d'/> 60/min"

        binding.tvVideo.text = Html.fromHtml(htmlText, Html.ImageGetter { source ->
            ContextCompat.getDrawable(requireActivity(), R.drawable.coin_d)?.apply {
                setBounds(0, 0, 45, 45)
            }
        }, null)

        binding.tvAudio.text = Html.fromHtml(htmlText1, Html.ImageGetter { source ->
            ContextCompat.getDrawable(requireActivity(), R.drawable.coin_d)?.apply {
                setBounds(0, 0, 45, 45)
            }
        }, null)


        binding.fabRandom.extend()
        binding.fabAudio.hide()
        binding.fabVideo.hide()
        binding.fabRandom.setOnClickListener {
            if (!isAllFabVisible) {
                binding.fabAudio.show()
                binding.fabVideo.show()
                binding.tvAudio.setVisibility(View.VISIBLE)
                binding.tvVideo.setVisibility(View.VISIBLE)

                // change the bg color
                binding.fabRandom.setBackgroundTintList(resources.getColorStateList(R.color.white))

                // Change the icon tint
                binding.fabRandom.setIconTintResource(R.color.black)


                // Change the icon and extend the parent FAB
                binding.fabRandom.setIconResource(R.drawable.ic_close) // Replace with your icon for shrinked state

                binding.fabRandom.shrink()
                isAllFabVisible = true
            } else {
                binding.fabAudio.hide()
                binding.fabVideo.hide()
                binding.tvAudio.setVisibility(View.GONE)
                binding.tvVideo.setVisibility(View.GONE)


                binding.fabRandom.setBackgroundTintList(resources.getColorStateList(R.color.blue))


                binding.fabRandom.setIconTintResource(R.color.white)



                // Change the icon and extend the parent FAB
                binding.fabRandom.setIconResource(R.drawable.random) // Replace with your icon for shrinked state
                binding.fabRandom.extend()

                isAllFabVisible = false
            }
        }
    }
}