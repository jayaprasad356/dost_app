package com.gmwapp.dostt.activities

import android.os.Bundle
import android.view.MenuItem
import com.gmwapp.dostt.R
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.dostt.fragments.HomeFragment
import com.gmwapp.dostt.fragments.ProfileFragment
import com.gmwapp.dostt.fragments.RecentFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : BaseActivity(), BottomNavigationView
.OnNavigationItemSelectedListener  {
    lateinit var binding: ActivityMainBinding
    var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomSheet: BottomSheetWelcomeBonus =
            BottomSheetWelcomeBonus()
        bottomSheet.show(
            supportFragmentManager,
            "BottomSheetWelcomeBonus"
        )
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        binding.bottomNavigationView.selectedItemId = R.id.home
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flFragment, HomeFragment())
                    .commit()
                return true
            }

            R.id.recent -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flFragment, RecentFragment())
                    .commit()
                return true
            }

            R.id.profile -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flFragment, ProfileFragment())
                    .commit()
                return true
            }
        }
        return false
    }
}