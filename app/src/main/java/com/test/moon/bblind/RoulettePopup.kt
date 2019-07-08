package com.test.moon.bblind

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.Window.FEATURE_NO_TITLE



class RoulettePopup : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)


        setContentView(R.layout.roulette_popup)

    }
    public fun mOnClose(v : View){
        finish()
    }
    public override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event!!.action == MotionEvent.ACTION_OUTSIDE){return false}
        return true;
    }
    public override fun onBackPressed() {
       return;
    }
}