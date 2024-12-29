package com.gmwapp.hima.activities

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivitySplashScreenBinding
import com.gmwapp.hima.viewmodels.LoginViewModel
import com.gmwapp.hima.viewmodels.ProfileViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {
    lateinit var binding: ActivitySplashScreenBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private val viewModel: LoginViewModel by viewModels()
    private var currentVersion: Int = 0
    private var latestVersion: Int = 0
    private var link: String = ""
    private var description: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        // Check for network connectivity
        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            return
        }


        var intent: Intent? = null
        val prefs = BaseApplication.getInstance()?.getPrefs()
        var userData = prefs?.getUserData()

            viewModel.appUpdate()


        try {
            val pInfo = packageManager.getPackageInfo(packageName, 0)
            currentVersion = pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        viewModel.appUpdateResponseLiveData.observe(this, Observer {
            if (it.success) {
                 latestVersion = it.data[0].app_version
                 link = it.data[0].link
                 description = it.data[0].description
            }
        })




        profileViewModel.getUserLiveData.observe(this, Observer {
            prefs?.setUserData(it.data)
            userData = it.data

            intent = when {
                userData?.status == 2 -> {
                    Intent(this, MainActivity::class.java).apply {
                        putExtra(DConstants.AVATAR_ID, getIntent().getIntExtra(DConstants.AVATAR_ID, 0))
                        putExtra(DConstants.LANGUAGE, userData?.language)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }
                userData?.status == 1 -> {
                    Intent(this, AlmostDoneActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }
                else -> {
                    Intent(this, VoiceIdentificationActivity::class.java).apply {
                        putExtra(DConstants.LANGUAGE, userData?.language)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                }
            }
            startActivity(intent)
            finish()
        })


        if (currentVersion!!.toInt() >= latestVersion.toInt()) {
            if (userData == null) {
                intent = Intent(this@SplashScreenActivity, NewLoginActivity::class.java)
            } else {
                if (userData?.gender == DConstants.MALE) {
                    intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                } else {
                    BaseApplication.getInstance()?.getPrefs()?.getUserData()?.id?.let {
                        profileViewModel.getUsers(it)
                    }

                }
            }
        } else {
            showUpdateDialog(link, description)
        }



        intent?.let {
            Handler().postDelayed({
                startActivity(it)
                finish()
            }, 3000)
        }
    }

    class GotoActivity {

    }

    // Function to check network availability
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private fun showUpdateDialog(link: String, description: String) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_dialog_update, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(false);

        val btnUpdate = view.findViewById<View>(R.id.btnUpdate)
        val dialogMessage = view.findViewById<TextView>(R.id.dialog_message)
        dialogMessage.text = description
        btnUpdate.setOnClickListener(View.OnClickListener {
            val url = link;
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        })


        // Customize your bottom dialog here
        // For example, you can set text, buttons, etc.

        bottomSheetDialog.show()
    }

}

