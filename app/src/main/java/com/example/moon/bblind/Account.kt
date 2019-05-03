package com.example.moon.bblind

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.activity_main.*

class Account : AppCompatActivity()
{
    // val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)

        var spinadapter = ArrayAdapter.createFromResource(this,R.array.year,android.R.layout.simple_spinner_item)

        spinadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        Account_Spinner_Year.adapter = spinadapter

    }


}