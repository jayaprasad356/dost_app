package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.gmwapp.hima.databinding.ActivityAccountPrivacyBinding


class AccountPrivacyActivity : BaseActivity() {
    lateinit var binding: ActivityAccountPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cvDeleteAccount.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, DeleteAccountActivity::class.java)
            startActivity(intent)
        })
    }
}