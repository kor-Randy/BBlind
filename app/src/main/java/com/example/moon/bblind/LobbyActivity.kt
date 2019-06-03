package com.example.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
class LobbyActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lobby)
        Toast.makeText(this,"Hi",Toast.LENGTH_LONG).show()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
        {
            Toast.makeText(this,currentUser.uid,Toast.LENGTH_LONG).show()
            //var intent = Intent(this, Loading::class.java)
            //startActivity(intent)

        }else{Toast.makeText(this,"null",Toast.LENGTH_LONG).show()}

    }
}