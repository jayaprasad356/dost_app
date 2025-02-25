package com.gmwapp.hima

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.gmwapp.hima.activities.AlmostDoneActivity
import com.gmwapp.hima.databinding.ActivityYoutubeBinding
import com.gmwapp.hima.databinding.ActivityYoutubeRechargeBinding
import com.gmwapp.hima.utils.setOnSingleClickListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener

class YoutubeRechargeActivity : AppCompatActivity() {
    lateinit var binding: ActivityYoutubeRechargeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityYoutubeRechargeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()

    }
    private fun initUI() {


        var videoUrl = "https://www.youtube.com/shorts/5URZAx1lgwI"
        val videoId = extractVideoId(videoUrl)

        if (videoId != null) {
            Log.d("VideoID", "Extracted ID: $videoId")
            binding.youtubePlayerView.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        } else {
            Log.e("VideoError", "Invalid YouTube URL")
        }


    }


    private fun extractVideoId(url: String): String? {
        val regex = Regex("(?:youtu\\.be/|youtube\\.com/(?:.*v=|.*\\/|.*embed\\/|.*watch\\?v=|.*shorts/))([a-zA-Z0-9_-]{11})")
        return regex.find(url)?.groupValues?.get(1)
    }
}