package com.gmwapp.hima.activities

import android.os.Bundle
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.gmwapp.hima.databinding.ActivityReviewBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ReviewActivity : BaseActivity() {
    lateinit var binding: ActivityReviewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initUI()
    }

    private fun initUI() {
        binding.ivClose.setOnClickListener({
            finish()
        })
        binding.rating.rating = 5f
        binding.tvTitle.text = getString(R.string.review_hint, intent.getStringExtra(DConstants.RECEIVER_NAME))
        binding.btnSubmit.setOnClickListener({
            finish()
        })
    }

}
