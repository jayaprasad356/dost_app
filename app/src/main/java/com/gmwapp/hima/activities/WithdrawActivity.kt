package com.gmwapp.hima.activities

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.addListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmwapp.hima.R
import com.gmwapp.hima.adapters.UpiListAdapter

class WithdrawActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)

        initUI()
    }

    private fun initUI() {
        val upiDetailsLayout = findViewById<LinearLayout>(R.id.ll_upi_details)
        val addUpiImage = findViewById<ImageView>(R.id.iv_add_upi)
        val rvUpiTypes = findViewById<RecyclerView>(R.id.rv_upi_types)
        val etUpiId = findViewById<EditText>(R.id.et_upi_id)
//        val btnWithdraw = findViewById<EditText>(R.id.btn_withdraw)

        val textList = listOf("@ybl", "@sbi", "@okicici", "@okaxis")

        var isExpanded = false

        addUpiImage.setOnClickListener {
            isExpanded = !isExpanded
            if (isExpanded) {
                expandView(upiDetailsLayout, rvUpiTypes)
                rotateImage(addUpiImage, 0f, 45f)
            } else {
                collapseView(upiDetailsLayout)
                rotateImage(addUpiImage, 45f, 0f)
            }
        }

        // Setup RecyclerView
        val upiAdapter = UpiListAdapter(textList) { selectedText ->
            val baseText = etUpiId.text.toString().substringBefore("@")
            etUpiId.setText("$baseText$selectedText")
        }
        rvUpiTypes.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvUpiTypes.adapter = upiAdapter

//        btnWithdraw.setOnClickListener(
//        )
    }

    private fun rotateImage(view: ImageView, fromAngle: Float, toAngle: Float) {
        ObjectAnimator.ofFloat(view, "rotation", fromAngle, toAngle).apply {
            duration = 300
            start()
        }
    }

    private fun expandView(view: View, upiList: RecyclerView) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val targetHeight = view.measuredHeight

        view.layoutParams.height = 0
        view.visibility = View.VISIBLE
        upiList.visibility = View.VISIBLE

        ValueAnimator.ofInt(0, targetHeight).apply {
            addUpdateListener {
                view.layoutParams.height = it.animatedValue as Int
                view.requestLayout()
            }
            duration = 300
            start()
        }
    }

    private fun collapseView(view: View) {
        val initialHeight = view.measuredHeight
        ValueAnimator.ofInt(initialHeight, 0).apply {
            addUpdateListener {
                view.layoutParams.height = it.animatedValue as Int
                view.requestLayout()
            }
            addListener(onEnd = { view.visibility = View.GONE })
            duration = 300
            start()
        }
    }
}