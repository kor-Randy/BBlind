package com.test.moon.bblind
import android.app.Activity
import android.os.Debug
import android.util.Log
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService


class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {


    override fun onTokenRefresh() {
        super.onTokenRefresh()
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "FirebaseInstanceId Refreshed token: " + refreshedToken!!)
    }

    companion object {
        private val TAG = "FCM_ID"
    }
}
