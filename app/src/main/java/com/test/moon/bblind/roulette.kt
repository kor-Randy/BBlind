package com.test.moon.bblind

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageButton
import android.widget.ToggleButton
import java.util.*

class roulette : Activity() {
    private lateinit var rullet : ImageButton
    private lateinit var child : ToggleButton
    private lateinit var adult : ToggleButton
    val child_string : Array<String> = arrayOf("눈치게임","훈민정음","더 게임 오브 데스","손병호게임","딸기","클레오파트라","잔치기","두부","홍삼","공동묘지","바니바니","출석부","지하철"
    ,"딸기당근수박참외메론","31")
    val adult_string : Array<String> = arrayOf("뱀사안사","왕게임","산넘어산","모텔에가면","업그레이드 이미지게임","문자 게임")
    var gameName : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.roulette)
        child = findViewById(R.id.childbtn)
        adult = findViewById(R.id.adultbtn)
        child.setChecked(true)
        adult.setChecked(false)
        child.setOnClickListener { if(adult.isChecked()){adult.setChecked(false); child.setChecked(true)}; SetStringChild() }
        adult.setOnClickListener { if(child.isChecked()){child.setChecked(false); adult.setChecked(true)}; SetStringAdult() }
        rullet = findViewById(R.id.btn)
        val animation : Animation = AnimationUtils.loadAnimation(applicationContext,R.anim.rotate)

        rullet.setOnClickListener { if(child.isChecked()){SetStringChild()} ;else if(adult.isChecked()){SetStringAdult()} ;rullet.startAnimation(animation) }

       animation.setAnimationListener(object : Animation.AnimationListener {
           override fun onAnimationRepeat(animation: Animation?) {

           }

           override fun onAnimationEnd(animation: Animation?) {
               val intent = Intent(this@roulette,RoulettePopup::class.java)
               intent.putExtra("gameName",gameName)
               startActivity(intent)
           }

           override fun onAnimationStart(animation: Animation?) {

           }
       })
    }
    fun SetStringAdult(){
        val random = Random()
        val num = random.nextInt(adult_string.size-1)
        gameName = adult_string[num]
    }
    fun SetStringChild(){
        val random = Random()
        val num = random.nextInt(child_string.size-1)
        gameName = child_string[num]}
}