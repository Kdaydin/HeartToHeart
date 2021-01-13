package com.khomeapps.hearttoheart.ui.meeting

import android.os.Bundle
import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.databinding.ActivityMeetingBinding
import com.khomeapps.hearttoheart.ui.base.BaseActivity
import us.zoom.sdk.ZoomApiError
import us.zoom.sdk.ZoomSDK
import us.zoom.sdk.ZoomSDKAuthenticationListener

class MeetingActivity : BaseActivity<MeetingViewModel, ActivityMeetingBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_meeting

    override fun getViewModelType(): MeetingViewModel = MeetingViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoomSDK.getInstance().addAuthenticationListener(object : ZoomSDKAuthenticationListener {
            override fun onZoomSDKLoginResult(p0: Long) {
                //TODO: change btn to Unlink
            }

            override fun onZoomSDKLogoutResult(p0: Long) {
                //TODO: change btn to link
            }

            override fun onZoomIdentityExpired() {
            }

            override fun onZoomAuthIdentityExpired() {
            }

        })
        binding?.btnLink?.setOnClickListener {
            val result = ZoomSDK.getInstance()
                .loginWithZoom(viewModel?.username?.value, viewModel?.password?.value)
            if (result == ZoomApiError.ZOOM_API_ERROR_SUCCESS) {

            }
        }
    }
}