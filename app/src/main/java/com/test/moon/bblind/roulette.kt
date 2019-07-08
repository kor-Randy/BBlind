package com.test.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton

class roulette : AppCompatActivity() {
    private lateinit var rullet : ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.roulette)
        rullet = findViewById(R.id.btn)
        val animation : Animation = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate)

        rullet.setOnClickListener { rullet.startAnimation(animation) }

       animation.setAnimationListener(object : Animation.AnimationListener {
           override fun onAnimationRepeat(animation: Animation?) {

           }

           override fun onAnimationEnd(animation: Animation?) {
               val intent = Intent(this@roulette,RoulettePopup::class.java)
               startActivity(intent)
           }

           override fun onAnimationStart(animation: Animation?) {

           }
       })
    }
}