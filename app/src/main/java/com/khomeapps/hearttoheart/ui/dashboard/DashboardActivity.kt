package com.khomeapps.hearttoheart.ui.dashboard

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.data.entities.HtHUser
import com.khomeapps.hearttoheart.databinding.ActivityDashboardBinding
import com.khomeapps.hearttoheart.ui.base.BaseActivity
import com.khomeapps.hearttoheart.ui.meeting.MeetingActivity
import com.khomeapps.hearttoheart.ui.utils.ZoomHelper
import us.zoom.sdk.*

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>() {

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun getViewModelType(): DashboardViewModel = DashboardViewModel()
    val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZoomHelper.initZoom(this)
        val settingsClickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(textView: View) {
                val i = Intent(this@DashboardActivity, MeetingActivity::class.java)
                startActivity(i)
            }
        }
        val warningText =
            SpannableStringBuilder("Your Zoom account is not linked. Please link your Zoom account to match and chat with other people. ")
        warningText.append("Click Here").setSpan(
            settingsClickableSpan,
            warningText.indexOf("Click Here"),
            warningText.indexOf("Click Here") + 10,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        warningText.append(" to set your Zoom account.").setSpan(
            ForegroundColorSpan(Color.parseColor("#f4511e")), warningText.indexOf("Click Here"),
            warningText.indexOf("Click Here") + 10,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding?.tvWarning?.isVisible = ZoomSDK.getInstance().isLoggedIn.not()
        binding?.tvWarning?.text = warningText
        binding?.tvWarning?.movementMethod = LinkMovementMethod.getInstance()

        FirebaseAuth.getInstance().currentUser?.let {
            binding?.tvText?.text = it.displayName
            val db = Firebase.firestore
            binding?.btnSignout?.setOnClickListener { view ->
                val user = HtHUser(it.displayName, it.email, it.uid)
                val map = gson.fromJson(gson.toJson(user, HtHUser::class.java), Map::class.java)
                db.collection("users")
                    .document(user.uid ?: "")
                    .set(map)
                    .addOnSuccessListener { documentReference ->
                        Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
                    }
                    .addOnFailureListener { e ->
                        Log.w("TAG", "Error adding document", e)
                    }
            }

        }

    }
}