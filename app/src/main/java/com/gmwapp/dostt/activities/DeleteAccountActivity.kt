package com.gmwapp.dostt.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.gmwapp.dostt.R
import com.gmwapp.dostt.databinding.ActivityAccountPrivacyBinding
import com.gmwapp.dostt.databinding.ActivityDeleteAccountBinding
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.databinding.ActivityWalletBinding
import com.gmwapp.dostt.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.dostt.fragments.HomeFragment
import com.gmwapp.dostt.fragments.ProfileFragment
import com.gmwapp.dostt.fragments.RecentFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView


class DeleteAccountActivity : BaseActivity() {
    lateinit var binding: ActivityDeleteAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeleteAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}