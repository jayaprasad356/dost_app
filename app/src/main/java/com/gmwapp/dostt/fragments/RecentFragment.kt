package com.gmwapp.dostt.fragments

import android.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.databinding.FragmentHomeBinding
import com.gmwapp.dostt.databinding.FragmentRecentBinding


class RecentFragment : BaseFragment() {
    lateinit var binding: FragmentRecentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRecentBinding.inflate(layoutInflater)

        return binding.root
    }
}