package com.test.moon.bblind
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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_apply.*
import kotlinx.android.synthetic.main.home.*

class Home : Fragment(), View.OnClickListener
{

    private lateinit var text: TextView
    private lateinit var bu: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.home, container, false) as View
        text = view.findViewById(R.id.text)
        bu = view.findViewById(R.id.Home_Apply)
        bu.setOnClickListener(this)
        return view
    }

    override fun onClick(v: View?) {

        when(v)
        {

            bu ->
            {



                            Log.d("zczc","고고")
                            val it : Intent = Intent(activity,ApplyActivity::class.java)
                            startActivity(it)






            }

        }

    }


}