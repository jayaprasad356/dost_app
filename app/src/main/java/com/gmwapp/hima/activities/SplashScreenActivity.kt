package com.gmwapp.hima.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
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
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        if (userData == null) {
            intent = Intent(
                this@SplashScreenActivity, LoginActivity::class.java
            )
        } else {
            if(userData.gender == DConstants.MALE) {
                intent = Intent(
                    this@SplashScreenActivity, MainActivity::class.java
                )
            }else{
                if(userData.status == 2){
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra(
                        DConstants.AVATAR_ID, getIntent().getIntExtra(DConstants.AVATAR_ID, 0)
                    )
                    intent.putExtra(DConstants.LANGUAGE, userData.language)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else if(userData.status == 1){
                    val intent = Intent(this, AlmostDoneActivity::class.java)
                    startActivity(intent)
                } else{
                    val intent = Intent(this, VoiceIdentificationActivity::class.java)
                    intent.putExtra(DConstants.LANGUAGE, userData.language)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
    }

}