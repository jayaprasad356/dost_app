package com.gmwapp.hima.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.databinding.ActivityYoutubeBinding
import com.gmwapp.hima.viewmodels.AccountViewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class YoutubeActivity : BaseActivity() {
    lateinit var binding: ActivityYoutubeBinding
    private val accountViewModel: AccountViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        val prefs = BaseApplication.getInstance()?.getPrefs()
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                prefs?.getSettingsData()?.demo_video?.let { youTubePlayer.loadVideo(it, 0f) }
            }
        })

        prefs?.getUserData()?.id?.let { accountViewModel.getSettings(it) }
        accountViewModel.settingsLiveData.observe(this, Observer {
            if (it.success) {
                if (it.data != null) {
                    if (it.data.size > 0) {
                        val settingsData = it.data.get(0)
                        prefs?.setSettingsData(settingsData)
                        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                youTubePlayer.loadVideo("settingsData.demo_video", 0f)
                            }
                        })
                    }
                }
            }
        })
        binding.btnComplete.setOnClickListener({
            val intent = Intent(this, AlmostDoneActivity::class.java)
            startActivity(intent)
        })
        binding.tvSkip.setOnClickListener({
            val intent = Intent(this, AlmostDoneActivity::class.java)
            startActivity(intent)
        })
    }

}