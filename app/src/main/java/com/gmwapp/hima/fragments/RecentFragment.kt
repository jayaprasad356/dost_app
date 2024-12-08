package com.gmwapp.hima.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.R
import com.gmwapp.hima.databinding.FragmentRecentBinding


class RecentFragment : BaseFragment() {
    lateinit var binding: FragmentRecentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater)

        binding.btnConnect.text = getString(R.string.connect_with_a_hima, getString(R.string.app_name))
        return binding.root
    }
}