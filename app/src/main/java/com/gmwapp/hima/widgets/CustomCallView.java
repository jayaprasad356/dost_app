package com.gmwapp.hima.widgets;

import static im.zego.connection.internal.ZegoConnectionImpl.context;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gmwapp.hima.R;
import com.zegocloud.uikit.components.audiovideo.ZegoBaseAudioVideoForegroundView;
import com.zegocloud.uikit.service.defines.ZegoUIKitUser;

import java.util.HashMap;

public class CustomCallView extends ZegoBaseAudioVideoForegroundView {

    public CustomCallView(@NonNull Context context, String userID) {
        super(context, userID);
    }

    public CustomCallView(@NonNull Context context, @Nullable AttributeSet attrs,
                          String userID) {
        super(context, attrs, userID);
    }

    protected void onForegroundViewCreated(ZegoUIKitUser uiKitUser) {
        // init your custom view

        ((Activity)context).getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        Toast.makeText(
                context,
        context.getString(R.string.please_try_again_later),
                Toast.LENGTH_LONG
                ).show();
    }

    protected void onCameraStateChanged(boolean isCameraOn) {
        // will be called when camera changed
    }

    protected void onMicrophoneStateChanged(boolean isMicrophoneOn) {
        // will be called when microphone changed
    }

    protected void onInRoomAttributesUpdated(HashMap<String, String> inRoomAttributes) {
        // will be called when inRoomAttributes changed
    }
}