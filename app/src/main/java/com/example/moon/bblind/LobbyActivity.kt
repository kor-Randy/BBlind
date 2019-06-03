package com.example.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback

class LobbyActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        var mBtnSignOut  : Button = findViewById(R.id.btn_google_signout)
        Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            Toast.makeText(this,currentUser.uid,Toast.LENGTH_LONG).show()
            //var intent = Intent(this, Loading::class.java)
            //startActivity(intent)

        }else{Toast.makeText(this,"null",Toast.LENGTH_LONG).show()}

        mBtnSignOut.setOnClickListener {
            UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
                override fun onCompleteLogout() {
                    FirebaseAuth.getInstance().signOut()

                    //val handler = Handler(Looper.getMainLooper())
                    //handler.post { updateUI() }
                }
            })
        }

    }
}