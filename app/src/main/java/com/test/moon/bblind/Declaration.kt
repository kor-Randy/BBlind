package com.test.moon.bblind

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_popup_declarationtext.*
import java.util.*

class Declaration : Activity()
{
    var spinner : Spinner? = null
    var Content : EditText? = null
    var spinadapter : ArrayAdapter<String>? = null
    var Kind : String? = null
    private var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val ref : DatabaseReference = mFirebaseDatabase.reference
    var int : Intent?=null
    var Opposite : String? = null
    val today = Date()
    var strdate : String? = null

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup_declarationtext)

        int = getIntent()
        Opposite = int!!.getStringExtra("id")

        spinner = Declaration_Spinner
        Content = Declaration_Content


        spinadapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_item,
                resources.getStringArray(R.array.kind_of_declaration) as Array<String>)
        spinadapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner!!.setAdapter(spinadapter)



        var format1 : SimpleDateFormat? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            format1 = SimpleDateFormat("yyyy-MM-dd")

            strdate = format1.format(today)


        }

        Declaration_OK.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {


                Kind = spinner!!.getSelectedItem().toString()
                //Toast.makeText(this@Declaration, Kind, Toast.LENGTH_SHORT).show()

                ref.child("Declaration").child(strdate!!).child(spinner!!.selectedItem.toString()).child(Opposite!!).setValue(Content!!.text.toString())

                finish()

            }
        })





    }

    fun GoDeclaration(view: View) {





    }

    fun GoCancel(view: View) {

        finish()

    }

}