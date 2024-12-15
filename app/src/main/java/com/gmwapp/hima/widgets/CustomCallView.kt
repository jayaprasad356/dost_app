package com.gmwapp.hima.widgets

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.gmwapp.hima.R
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView
import com.zegocloud.uikit.service.defines.ZegoUIKitUser

class CustomCallView : ZegoBaseAudioVideoForegroundView {
    private var tvTime: TextView? = null

    constructor(context: Context, userID: String?) : super(context, userID)

    constructor(
        context: Context, attrs: AttributeSet?, userID: String?
    ) : super(context, attrs, userID)

    override fun onForegroundViewCreated(uiKitUser: ZegoUIKitUser) {
        // init your custom view
        val view = inflate(context, R.layout.widget_custom_call, this)
        tvTime = view.findViewById<View>(R.id.tv_time) as TextView?
    }

    fun updateTime(seconds: Long) {
        val mins = seconds / 60
        val remainingSecs = seconds % 60
        val result =
            (if (mins > 9) mins.toString() else "0" + mins) + ":" + (if (remainingSecs > 9) remainingSecs.toString() else "0" + remainingSecs)
        tvTime?.text = result
    }

    override fun onCameraStateChanged(isCameraOn: Boolean) {
        // will be called when camera changed
    }

    override fun onMicrophoneStateChanged(isMicrophoneOn: Boolean) {
        // will be called when microphone changed
    }
}