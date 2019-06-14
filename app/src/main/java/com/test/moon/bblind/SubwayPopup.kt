package com.test.moon.bblind
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_apply.*

import kotlinx.android.synthetic.main.activity_popup_subway.*
class SubwayPopup : Activity()
{

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_popup_subway)

        val intent = getIntent()
        val SubwayNum = intent.getStringExtra("Subway")
        var subList: Array<String>? = null

        Log.d("Tag",SubwayNum)







        when(SubwayNum)
        {

            "1" -> subList = resources.getStringArray(R.array.subway1)
            "2" -> subList = resources.getStringArray(R.array.subway2)
            "3" -> subList = resources.getStringArray(R.array.subway3)
            "4" -> subList = resources.getStringArray(R.array.subway4)
            "5" -> subList = resources.getStringArray(R.array.subway5)
            "6" -> subList = resources.getStringArray(R.array.subway6)
            "7" -> subList = resources.getStringArray(R.array.subway7)
            "8" -> subList = resources.getStringArray(R.array.subway8)
            "9" -> subList = resources.getStringArray(R.array.subway9)
            "gyungjung" -> subList = resources.getStringArray(R.array.subwaygyungjung)
            "incheon1" -> subList = resources.getStringArray(R.array.subwayincheon1)
            "incheon2" -> subList = resources.getStringArray(R.array.subwayincheon2)

            else -> Toast.makeText(this,"Not Working",Toast.LENGTH_SHORT).show()
        }


        val adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,subList)
        var SubString : String =""
        Pop_List_Subway.adapter = adapter

        Pop_List_Subway.setOnItemClickListener { parent, view, position, id ->



            SubString = parent.getItemAtPosition(position).toString()
            Toast.makeText(this@SubwayPopup, SubString,Toast.LENGTH_SHORT).show()
            val Returnintent : Intent = Intent()
            Returnintent.putExtra("result",SubString)
            setResult(RESULT_OK,Returnintent)
            finish()

        }


    }



}

