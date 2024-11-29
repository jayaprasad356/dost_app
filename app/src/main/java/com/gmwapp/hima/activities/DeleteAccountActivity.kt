package com.gmwapp.hima.activities

import android.os.Bundle
import com.gmwapp.hima.databinding.ActivityDeleteAccountBinding


class DeleteAccountActivity : BaseActivity() {
    lateinit var binding: ActivityDeleteAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}