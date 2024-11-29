package com.gmwapp.hima.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.databinding.ActivityMainBinding
import com.gmwapp.hima.databinding.ActivitySplashScreenBinding
import com.gmwapp.hima.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.hima.fragments.HomeFragment
import com.gmwapp.hima.fragments.ProfileFragment
import com.gmwapp.hima.fragments.RecentFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView


class SplashScreenActivity : BaseActivity() {
    lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        var intent: Intent? = null
        if (BaseApplication.getInstance()?.getPrefs()?.getUserData() == null) {
            intent = Intent(
                this@SplashScreenActivity, LoginActivity::class.java
            )
        } else {
            intent = Intent(
                this@SplashScreenActivity, MainActivity::class.java
            )
        }
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
    }

}