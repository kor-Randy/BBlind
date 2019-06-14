package com.test.moon.bblind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler

class Loading : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loading)
        Handler().postDelayed({

            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },1000)
    }
}