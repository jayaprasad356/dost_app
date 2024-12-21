package com.gmwapp.hima.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.CoinAdapter
import com.gmwapp.hima.adapters.RecentCallsAdapter
import com.gmwapp.hima.callbacks.OnItemSelectionListener
import com.gmwapp.hima.databinding.FragmentRecentBinding
import com.gmwapp.hima.retrofit.responses.CallsListResponseData
import com.gmwapp.hima.retrofit.responses.CoinsResponseData
import com.gmwapp.hima.viewmodels.AccountViewModel
import com.gmwapp.hima.viewmodels.RecentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecentFragment : BaseFragment() {
    lateinit var binding: FragmentRecentBinding
    private val recentViewModel: RecentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater)

        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.btnConnect.text =
            getString(R.string.connect_with_a_hima, getString(R.string.app_name))
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        userData?.let { recentViewModel.getCallsList(userData.id, userData.gender) }
        recentViewModel.callsListLiveData.observe(viewLifecycleOwner, Observer {
            if(it.success && it.data!=null) {
                binding.rvCalls.setLayoutManager(
                    LinearLayoutManager(
                        requireActivity(), LinearLayoutManager.VERTICAL, false
                    )
                )
                val recentCallsAdapter = RecentCallsAdapter(requireActivity(),it.data)
                binding.rvCalls.setAdapter(recentCallsAdapter)
                binding.rvCalls.visibility = View.VISIBLE
                binding.tlTitle.visibility = View.GONE
                binding.tlTitleDetails.visibility = View.GONE
                binding.btnConnect.visibility = View.GONE

            }
        })
    }
}