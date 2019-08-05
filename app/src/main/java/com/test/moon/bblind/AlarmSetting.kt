package com.test.moon.bblind

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.widget.CompoundButton
import android.widget.ToggleButton
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase

class AlarmSetting : AppCompatActivity()
{
    var mFirebaseDatabase: FirebaseDatabase? = null
    var tb1: ToggleButton? = null
    var tb2: ToggleButton? = null
    var firebaseanalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
    var sf: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    var Matching: String? = null
    var App: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        setContentView(R.layout.alram_setting)


        sf = getSharedPreferences("Alarm", 0)
        editor =  sf!!.edit();

        Matching = sf!!.getString("Matching", "")
        App = sf!!.getString("App", "")

        tb1 = findViewById(R.id.toggleButton1)
        tb2 = findViewById(R.id.toggleButton2)


        if (Matching.equals("true")) {
            tb1!!.setChecked(true)
        } else {
            tb1!!.setChecked(false)
        }

        if (App.equals("true")) {
            tb1!!.setChecked(true)
        } else {
            tb1!!.setChecked(false)
        }





        tb1!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked == true) {

                    editor!!.putString("Matching", "true")

                    firebaseanalytics!!.setUserProperty("MatchingAlarm", "true")

                } else {

                    editor!!.putString("Matching", "false")

                    firebaseanalytics!!.setUserProperty("MatchingAlarm", "false")

                }


            }
        });

        tb2!!.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                if (isChecked == true) {

                    editor!!.putString("App", "true")

                    firebaseanalytics!!.setUserProperty("AppAlarm", "true")

                } else {

                    editor!!.putString("App", "false")

                    firebaseanalytics!!.setUserProperty("AppAlarm", "false")

                }


            }
        });



        fun onBackPressed() {
            super.onBackPressed();
            finish();

        }
    }
}