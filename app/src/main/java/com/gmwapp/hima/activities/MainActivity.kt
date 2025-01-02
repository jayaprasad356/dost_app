package com.gmwapp.hima.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityMainBinding
import com.gmwapp.hima.dialogs.BottomSheetWelcomeBonus
import com.gmwapp.hima.fragments.FemaleHomeFragment
import com.gmwapp.hima.fragments.HomeFragment
import com.gmwapp.hima.fragments.ProfileFemaleFragment
import com.gmwapp.hima.fragments.ProfileFragment
import com.gmwapp.hima.fragments.RecentFragment
import com.gmwapp.hima.viewmodels.OfferViewModel
import com.gmwapp.hima.viewmodels.UpiViewModel
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityMainBinding
    var isBackPressedAlready = false
    var userName:String? = null
    var userID :String? = null;

    val offerViewModel: OfferViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }
        val userData = BaseApplication.getInstance()?.getPrefs()?.getUserData()
        userID = userData?.id.toString()


        initUI()

        userName = userData?.name
        onBackPressedDispatcher.addCallback(this ) {
            if (isBackPressedAlready) {
               finish()
            } else {
                Toast.makeText(
                    this@MainActivity, getString(R.string.press_back_again_to_exit), Toast.LENGTH_SHORT
                ).show()
                isBackPressedAlready = true
                Handler().postDelayed({
                    isBackPressedAlready = false
                }, 3000)
            }
        }
    }
    private fun initUI() {


        userID?.let { offerViewModel.getOffer(it.toInt()) }

//        offerViewModel.offerResponseLiveData.observe(this) { response ->
//            if (response.success) {
//                val coin = response.data[0].coins
//                val price = response.data[0].price
//                val save = response.data[0].save
//
//                if (BaseApplication.getInstance()?.getPrefs()?.getUserData()?.gender == DConstants.MALE) {
//                    val bottomSheet = BottomSheetWelcomeBonus(coin, price, save)
//                    bottomSheet.show(supportFragmentManager, "BottomSheetWelcomeBonus")
//                }
//            }
//        }






        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this)
        binding.bottomNavigationView.selectedItemId = R.id.home
        removeShiftMode()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {

                val homeFragment = if(BaseApplication.getInstance()?.getPrefs()?.getUserData()?.gender == DConstants.FEMALE)
                    FemaleHomeFragment() else HomeFragment()
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, homeFragment)
                    .commit()
                return true
            }

            R.id.recent -> {
                supportFragmentManager.beginTransaction().replace(R.id.flFragment, RecentFragment())
                    .commit()
                return true
            }

            R.id.profile -> {
                if (BaseApplication.getInstance()?.getPrefs()
                        ?.getUserData()?.gender == DConstants.MALE
                ) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, ProfileFragment()).commit()
                } else {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.flFragment, ProfileFemaleFragment()).commit()
                }
                return true
            }
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    fun removeShiftMode() {
        binding.bottomNavigationView.labelVisibilityMode =
            NavigationBarView.LABEL_VISIBILITY_LABELED
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