package com.gmwapp.hima.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.databinding.ActivityGrantPermissionsBinding

class GrantPermissionsActivity : BaseActivity() {
    lateinit var binding: ActivityGrantPermissionsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrantPermissionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.btnContinue.setOnClickListener {
            try {
                finish()
                startActivity(
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", packageName, null)
                    )
                )
            } catch (e: Exception) {
                e.message?.let { Log.e("GrantPermissionActivity", it) }
            }
        }

    }
}