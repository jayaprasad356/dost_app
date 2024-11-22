package com.gmwapp.dostt.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import com.gmwapp.dostt.R
import com.gmwapp.dostt.databinding.ActivityMainBinding
import com.gmwapp.dostt.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.dostt.fragments.HomeFragment
import com.gmwapp.dostt.fragments.ProfileFragment
import com.gmwapp.dostt.fragments.RecentFragment
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.material.navigation.NavigationBarView


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
        removeShiftMode()
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

    @SuppressLint("RestrictedApi")
    fun removeShiftMode() {
        binding.bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);
        val menuView = binding.bottomNavigationView.getChildAt(0) as BottomNavigationMenuView
        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShifting(false)
            item.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED)

            // set once again checked value, so view will be updated
            item.setChecked(item.itemData!!.isChecked)
        }
    }
}