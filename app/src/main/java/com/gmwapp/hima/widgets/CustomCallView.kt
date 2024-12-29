package com.gmwapp.hima.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.gmwapp.hima.BaseApplication
import com.gmwapp.hima.R
import com.gmwapp.hima.constants.DConstants
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class CustomCallView : ZegoBaseAudioVideoForegroundView {
    private var tvRemainingTime: TextView? = null

    constructor(context: Context, userID: String?) : super(context, userID)

    constructor(
        context: Context, attrs: AttributeSet?, userID: String?
    ) : super(context, attrs, userID)

    override fun onForegroundViewCreated(uiKitUser: ZegoUIKitUser) {
        // init your custom view
        val prefs = BaseApplication.getInstance()?.getPrefs()
        val userData = prefs?.getUserData()

        val inflate = inflate(context, if(userData?.gender == DConstants.MALE) R.layout.widget_custom_call else R.layout.widget_custom_call_female, this)
        val view = inflate
        tvRemainingTime = view.findViewById<View>(R.id.tv_remaining_time) as TextView?
    }

    fun updateTime(seconds: Int) {
        tvRemainingTime?.visibility = View.VISIBLE
        val hours = seconds / 3600
        val minutes = (seconds % 3600) / 60;
        val secs = seconds % 60;
        tvRemainingTime?.text = String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    override fun onCameraStateChanged(isCameraOn: Boolean) {
        // will be called when camera changed
    }

    override fun onMicrophoneStateChanged(isMicrophoneOn: Boolean) {
        // will be called when microphone changed
    }
}