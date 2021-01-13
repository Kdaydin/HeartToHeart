package com.khomeapps.hearttoheart.ui.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.khomeapps.hearttoheart.BuildConfig
import com.khomeapps.hearttoheart.ui.meeting.MeetingActivity
import us.zoom.sdk.*

object ZoomHelper {
    var handle =
        InMeetingNotificationHandle { context, intent ->
            var intent = intent
            intent = Intent(context, MeetingActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.action = InMeetingNotificationHandle.ACTION_RETURN_TO_CONF
            context.startActivity(intent)
            true
        }

    fun initZoom(context: Context) {
        val params = ZoomSDKInitParams().apply {
            appKey = BuildConfig.APIKEY
            appSecret = BuildConfig.APISECRET
            domain = "zoom.us"
            enableLog = true
        }
        ZoomSDK.getInstance().initialize(context, object : ZoomSDKInitializeListener {
            override fun onZoomSDKInitializeResult(errorCode: Int, internalErrorCode: Int) {
                if (errorCode != ZoomError.ZOOM_ERROR_SUCCESS) {
                    Toast.makeText(
                        context,
                        "Failed to initialize Zoom SDK. Error: $errorCode, internalErrorCode=$internalErrorCode",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    ZoomSDK.getInstance().zoomUIService.enableMinimizeMeeting(true)
                    ZoomSDK.getInstance().zoomUIService.setMiniMeetingViewSize(
                        CustomizedMiniMeetingViewSize(
                            0,
                            0,
                            360,
                            540
                        )
                    )
                    ZoomSDK.getInstance().meetingSettingsHelper.enable720p(false)
                    ZoomSDK.getInstance().meetingSettingsHelper.enableShowMyMeetingElapseTime(true)
                    ZoomSDK.getInstance().meetingService.addListener(object :
                        MeetingServiceListener {
                        override fun onMeetingStatusChanged(p0: MeetingStatus?, p1: Int, p2: Int) {

                        }

                    })
                    ZoomSDK.getInstance().meetingSettingsHelper.setCustomizedNotificationData(
                        null,
                        handle
                    )
                    Toast.makeText(context, "Initialize Zoom SDK successfully.", Toast.LENGTH_LONG)
                        .show()
                }
            }

            override fun onZoomAuthIdentityExpired() {
            }

        }, params)
    }

}