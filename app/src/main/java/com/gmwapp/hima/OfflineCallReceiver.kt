package com.gmwapp.hima

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import com.google.firebase.messaging.RemoteMessage
import com.zegocloud.uikit.prebuilt.call.MyZPNsReceiver
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.core.CallInvitationServiceImpl
import com.zegocloud.uikit.prebuilt.call.core.push.ZIMPushMessage
import com.zegocloud.uikit.prebuilt.call.invite.internal.CallInviteActivity
import im.zego.uikit.libuikitreport.ReportUtil
import im.zego.zpns.ZPNsMessageReceiver
import im.zego.zpns.entity.ZPNsMessage
import im.zego.zpns.entity.ZPNsRegisterMessage
import im.zego.zpns.enums.ZPNsConstants.PushSource
import timber.log.Timber

class OfflineCallReceiver : MyZPNsReceiver() {
    override fun onThroughMessageReceived(context: Context, message: ZPNsMessage) {
        super.onThroughMessageReceived(context, message)
        val pushMessage = getZIMPushMessage(message)
        if (message.pushSource == PushSource.FCM) {
            if (!TextUtils.isEmpty(pushMessage!!.invitationID)) { // not zim push message.
                Handler(Looper.getMainLooper()).postDelayed({
                    try {
                        if(BaseApplication.getInstance()?.getRoomId()==null) {
                            val incomingCallButtonListener =
                                ZegoUIKitPrebuiltCallService.events.invitationEvents.incomingCallButtonListener
                            incomingCallButtonListener?.onIncomingCallDeclineButtonPressed()
                            CallInvitationServiceImpl.getInstance().rejectInvitation {
                                val hashMap = java.util.HashMap<String, Any>()
                                val invitationData =
                                    CallInvitationServiceImpl.getInstance().callInvitationData
                                if (invitationData != null) {
                                    hashMap["call_id"] = invitationData.invitationID
                                } else {
                                    hashMap["call_id"] = ""
                                }
                                hashMap["app_state"] = "active"
                                hashMap["action"] = "refuse"
                                ReportUtil.reportEvent("call/respondInvitation", hashMap)
                            }

                            try {
                                (CallInvitationServiceImpl.getInstance().topActivity as CallInviteActivity).finishCallActivityAndMoveToFront()
                            } catch (e: Exception) {
                            }
                            CallInvitationServiceImpl.getInstance().hideIncomingCallDialog()
                            CallInvitationServiceImpl.getInstance().dismissCallNotification()
                            CallInvitationServiceImpl.getInstance().clearPushMessage()
                        }
                    } catch (e: Exception) {
                    }

                }, 7000)
            }
        }
    }

    override fun onNotificationClicked(context: Context, message: ZPNsMessage) {
        super.onNotificationClicked(context, message)
    }

    override fun onNotificationArrived(context: Context, message: ZPNsMessage) {
        super.onNotificationArrived(context, message)
    }

    override fun onRegistered(context: Context, message: ZPNsRegisterMessage) {
        super.onRegistered(context, message)
    }

    companion object {
        fun getZIMPushMessage(message: ZPNsMessage): ZIMPushMessage? {
            var pushMessage: ZIMPushMessage? = null
            when (message.pushSource) {
                PushSource.FCM -> {
                    val remoteMessage = message.pushMessage as RemoteMessage
                    val payload = remoteMessage.data["payload"]
                    val title = remoteMessage.data["title"]
                    val body = remoteMessage.data["body"]
                    val invitationID = remoteMessage.data["call_id"]
                    pushMessage = ZIMPushMessage(invitationID, title, body, payload)
                }

                else -> {}
            }
            return pushMessage
        }
    }
}
