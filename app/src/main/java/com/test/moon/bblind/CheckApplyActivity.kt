package com.test.moon.bblind

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.*
import com.test.moon.bblind.MainActivity.Companion.checkapplylist
import kotlinx.android.synthetic.main.activity_apply.*

class CheckApplyActivity :AppCompatActivity()
{
    var arr1 :String= String()
    var arr2 :String= String()
    var arr3 :String= String()
    var arr4 :String= String()

    var stringdata :ArrayList<CheckString>?= ArrayList<CheckString>()
    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    var s1 : ArrayList<String> = ArrayList<String>()
    var s2 : ArrayList<String> = ArrayList<String>()
    var s3 : ArrayList<String> = ArrayList<String>()
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


    MainActivity.nowAc="CheckApplyActivity"

      lv  = findViewById(R.id.check_lv)




        s1.add("check")
        s2.add("check")
        s3.add("check")

        for ( i in 1..MainActivity.checkapplylist!!.checklist!!.size-1)
            //1부터인 이유 = 0번째는 "초기화"
            {

                Log.d("checkcheck",i.toString())



                s1.add(MainActivity.checkapplylist!!.checklist!![i].split("/")[0])
                s2.add(MainActivity.checkapplylist!!.checklist!![i].split("/")[1])
                s3.add(MainActivity.checkapplylist!!.checklist!![i].split("/")[2])


            Log.d("checkcheck111",s1.toString()+s2.toString()+s3.toString())

            ref.child("Apply").child("SubwayStation").child(s1[i]).child(s2[i]).orderByChild("name").equalTo(MainActivity.Myuid!!).addListenerForSingleValueEvent(object: ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {


                }

                override fun onDataChange(p0: DataSnapshot) {




                        v0 = p0.getValue(true).toString().split("{")[2]

                        v1 = v0!!.split(",")[0]
                        v2 = v0!!.split(",")[1]
                        v3 = v0!!.split(",")[2]
                        v4 = v0!!.split(",")[4]
                        v5 = v0!!.split(",")[5]
                        v6 = v0!!.split(",")[6]
                        v7 = v0!!.split(",")[7]


                        Log.d("checkcheck",
                                v1 + v2 + v3 + v4 + v5 + v6 + v7
                        )

                        v1 = v1!!.split("=")[1]
                        v2 = v2!!.split("=")[1]
                        v3 = v3!!.split("=")[1]
                        v4 = v4!!.split("=")[1]
                        v5 = v5!!.split("=")[1]
                        v6 = v6!!.split("=")[1]
                        v7 = v7!!.split("=")[1]


                        var cc: CheckString = CheckString(s1[i], s2[i], v1!!, v2!!)

                        stringdata!!.add(cc)

                        Log.d("checkcheck",
                                v1 + " " + v2 + " " + v3 + " " + v4 + " " + v5 + " " + v6 + " " + v7
                        )

                        var adap: CheckApplyListAdapter = CheckApplyListAdapter(stringdata!!)


                        adap.notifyDataSetChanged()

                        lv!!.adapter = adap

                        setListViewHeightBasedOnChildren(lv!!)

                        Log.d("checkcheck", lv!!.count.toString())

                }
            })
            setListViewHeightBasedOnChildren(lv!!)

        }

        lv!!.setOnItemLongClickListener(object: AdapterView.OnItemLongClickListener {
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {

                val alt_bld =AlertDialog.Builder(this@CheckApplyActivity)
                alt_bld!!.setMessage("매칭을 삭제하시겠습니까?").setCancelable(true).setPositiveButton("네",
                        object: DialogInterface.OnClickListener {

                            override fun onClick(p0: DialogInterface?, p1: Int) {

                                Log.d("deldeldel","1")
                                val delquery: Query = ref.child("Apply").child("SubwayStation").child(s1[position+1])
                                        .child(s2[position+1]).orderByChild("name").equalTo(MainActivity.Myuid!!)

                                Log.d("deldeldel","2")

                                Log.d("deldeldel",s1[position+1]+"/"+s2[position+1]+"/"+s3[position+1])

                                delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(p0: DatabaseError) {
                                    }

                                    override fun onDataChange(p0: DataSnapshot) {

                                        for (appleSnapshot in p0.getChildren()) {

                                            Log.d("deldeldel","3")
                                            if(appleSnapshot.child("date").getValue(true).toString().equals(s3[position+1])) {

                                                appleSnapshot.getRef().removeValue()
                                                Log.d("aaaaz", "삭제")

                                                Log.d("deldeldel","4")
                                                val count = MainActivity.checkapplylist!!.checklist!!.indexOf(s1[position+1]+"/"+s2[position+1]+"/"+s3[position+1])

                                                MainActivity.checkapplylist!!.checklist!!.removeAt(count)

                                                ref.child("Account").child(MainActivity.Myuid!!).child("Myapply").setValue(MainActivity.checkapplylist)

                                                Log.d("deldeldel","5")

                                                MainActivity.nowAc="Main"
                                                finish()

                                            }
                                        }

                                    }
                                })


                            }
                        }).setNegativeButton("아니요", object :DialogInterface.OnClickListener{
                    override fun onClick(p0: DialogInterface?, p1: Int) {
                        p0!!.cancel()
                    }})
                alt_bld.create().show()
                return true

            }
        })



    }

    override fun onBackPressed() {
        super.onBackPressed()
        MainActivity.nowAc="Main"
        finish()
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