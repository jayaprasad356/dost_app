package com.gmwapp.dostt.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.gmwapp.dostt.R
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.databinding.ActivityTransactionsBinding
import com.gmwapp.dostt.databinding.ActivityWalletBinding
import com.gmwapp.dostt.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.dostt.fragments.HomeFragment
import com.gmwapp.dostt.fragments.ProfileFragment
import com.gmwapp.dostt.fragments.RecentFragment
import com.gmwapp.dostt.viewmodels.LoginViewModel
import com.gmwapp.dostt.viewmodels.TransactionsViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView


class TransactionsActivity : BaseActivity() {
    lateinit var binding: ActivityTransactionsBinding
    private val transactionsViewModel: TransactionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}