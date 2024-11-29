package com.gmwapp.hima.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gmwapp.hima.databinding.FragmentHomeBinding


class HomeFragment : BaseFragment() {
    private var isAllFabVisible: Boolean = false
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        initFab()
        return binding.root
    }

    fun initFab(){
        binding.fabRandom.extend()
        binding.fabAudio.hide()
        binding.fabVideo.hide()
        binding.fabRandom.setOnClickListener {
            if (!isAllFabVisible) {
                binding.fabAudio.show()
                binding.fabVideo.show()
                binding.tvAudio.setVisibility(View.VISIBLE)
                binding.tvVideo.setVisibility(View.VISIBLE)

                binding.fabRandom.shrink()
                isAllFabVisible = true
            } else {
                binding.fabAudio.hide()
                binding.fabVideo.hide()
                binding.tvAudio.setVisibility(View.GONE)
                binding.tvVideo.setVisibility(View.GONE)

                binding.fabRandom.extend()

                isAllFabVisible = false
            }
        }
    }
}