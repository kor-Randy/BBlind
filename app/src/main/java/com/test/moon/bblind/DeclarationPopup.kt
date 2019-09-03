package com.test.moon.bblind

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_popup_declaration.*
import java.util.*

class DeclarationPopup : Activity() {

    var list : Array<String>? = null


    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup_declaration)

        val int : Intent =  getIntent()

        list = resources.getStringArray(R.array.declaration)

        val adapter = ArrayAdapter<String>(this,R.layout.list_declare,R.id.declare_content,list)

        Pop_List_Declaration.adapter = adapter

        Pop_List_Declaration.setOnItemClickListener(object: AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                when(position)
                {
                    0 ->
                    {
                        var intent : Intent = Intent()

                        intent.putExtra("select","0")

                        setResult(RESULT_OK,intent)
                        finish()


                    }

                        1->
                        {

                            var intent : Intent = Intent()

                            intent.putExtra("select","1")

                            setResult(RESULT_OK,intent)
                            finish()

                        }
                }



            }
        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }



}