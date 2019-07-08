package com.test.moon.bblind

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.test.moon.bblind.MainActivity.Companion.checkapplylist

class CheckApplyActivity :AppCompatActivity()
{
    var arr1 :String= String()
    var arr2 :String= String()
    var arr3 :String= String()
    var arr4 :String= String()

    var stringdata :ArrayList<CheckString>?= ArrayList<CheckString>()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    var s1 : String? = null
    var s2 : String? = null
    var v0 : String? = null
    var v1 : String? = null
    var v2 : String? = null
    var v3 : String? = null
    var v4 : String? = null
    var v5 : String? = null
    var v6 : String? = null
    var v7 : String? = null

    val ref : DatabaseReference = database.reference
    var lv : ListView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_check_apply)




      lv  = findViewById(R.id.check_lv)



        for ( i in 0..MainActivity.checkapplylist!!.checklist!!.size-1)
        {

            Log.d("checkcheck",i.toString())

            s1 = MainActivity.checkapplylist!!.checklist!![i].split("/")[0]
            s2 = MainActivity.checkapplylist!!.checklist!![i].split("/")[1]


            ref.child("Apply").child("SubwayStation").child(s1!!).child(s2!!).orderByChild("name").equalTo(MainActivity.Myuid!!).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {


                }

                override fun onDataChange(p0: DataSnapshot) {




                    v0 =  p0.getValue(true).toString().split("{")[2]

                    v1 =  v0!!.split(",")[0]
                    v2 =  v0!!.split(",")[1]
                    v3 =  v0!!.split(",")[2]
                    v4 =  v0!!.split(",")[4]
                    v5 =  v0!!.split(",")[5]
                    v6 =  v0!!.split(",")[6]
                    v7 =  v0!!.split(",")[7]


                    Log.d("checkcheck",
                            v1+v2+v3+v4+v5+v6+v7
                    )

                    v1 =  v1!!.split("=")[1]
                    v2 =  v2!!.split("=")[1]
                    v3 =  v3!!.split("=")[1]
                    v4 =  v4!!.split("=")[1]
                    v5 =  v5!!.split("=")[1]
                    v6 =  v6!!.split("=")[1]
                    v7 =  v7!!.split("=")[1]


                    var cc : CheckString = CheckString(s1!!,s2!!,v1!!,v2!!)

                    stringdata!!.add(cc)

                    Log.d("checkcheck",
                            v1+" "+v2+" "+v3+" "+v4+" "+v5+" "+v6+" "+v7
                    )

                    var adap : CheckApplyListAdapter = CheckApplyListAdapter(stringdata!!)


                    adap.notifyDataSetChanged()

                    lv!!.adapter = adap

                    setListViewHeightBasedOnChildren(lv!!)

                    Log.d("checkcheck",lv!!.count.toString())
                }
            })


        }





    }

    fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter
                ?: // pre-condition
                return

        var totalHeight = 0
        val desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.width, View.MeasureSpec.AT_MOST)

        for (i in 0 until listAdapter.count) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

}