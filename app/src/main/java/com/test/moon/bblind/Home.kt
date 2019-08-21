package com.test.moon.bblind
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_apply.*
import kotlinx.android.synthetic.main.home.*

class Home : Fragment(), View.OnClickListener
{

    private lateinit var text: TextView
    private lateinit var bu: Button
    private lateinit var checkbu : Button

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()



    val ref : DatabaseReference = database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.home, container, false) as View
        bu = view.findViewById(R.id.Home_Apply)
        bu.setOnClickListener(this)
        checkbu = view.findViewById(R.id.Home_Check_Apply)
        checkbu.setOnClickListener(this)


        ref.child("Account").addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                if(p0.child(MainActivity.Myuid!!).child("Myapply").exists())
                MainActivity.checkapplylist = p0.child(MainActivity.Myuid!!).child("Myapply").getValue(CheckApplyListData::class.java)
            }
        })

        return view
    }

    override fun onClick(v: View?) {

        when(v)
        {

            bu ->
            {

                            val it : Intent = Intent(activity,ApplyActivity::class.java)
                            startActivity(it)

            }

            checkbu ->
            {

                val it : Intent = Intent(activity,CheckApplyActivity::class.java)
                 startActivity(it)

            }

        }

    }




}