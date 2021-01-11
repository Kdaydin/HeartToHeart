package com.khomeapps.hearttoheart.ui.dashboard

import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.khomeapps.hearttoheart.R
import com.khomeapps.hearttoheart.data.entities.HtHUser
import com.khomeapps.hearttoheart.databinding.ActivityDashboardBinding
import com.khomeapps.hearttoheart.ui.base.BaseActivity

class DashboardActivity : BaseActivity<DashboardViewModel, ActivityDashboardBinding>() {

    override fun getLayoutRes(): Int = R.layout.activity_dashboard

    override fun getViewModelType(): DashboardViewModel = DashboardViewModel()
    val gson = Gson()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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