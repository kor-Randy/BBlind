package com.test.moon.bblind

import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout

import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.test.moon.bblind.databinding.ActivityMainBinding
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.usermgmt.LoginButton
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.util.exception.KakaoException

import android.os.Handler
import android.util.Base64
import android.widget.Toast
import com.android.volley.*
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_apply.*

import org.json.JSONObject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.Signature
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var loginButton : LoginButton
    private var mAuth: FirebaseAuth?  = FirebaseAuth.getInstance()
    private var user : FirebaseUser?= mAuth!!.currentUser
    val database : FirebaseDatabase? = FirebaseDatabase.getInstance()
    val myRef : DatabaseReference = database!!.reference
    var auth : FirebaseAuth?=null
    var myuid : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)





        activity = this
        //어플의 모든 푸시알림 삭제
        val  notifiyMgr : NotificationManager= getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notifiyMgr.cancelAll();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loginButton = findViewById<View>(R.id.login_button) as LoginButton

        auth = FirebaseAuth.getInstance()


        Log.d("crdcheck","first"+auth!!.currentUser)
        if(auth!!.currentUser==null)
        {
            Log.d("crdcheck","1111"+auth!!.currentUser)
            myuid = null
            loginButton.visibility=View.VISIBLE
        }
        else
        {

            loginButton.visibility=View.GONE
            myuid = auth!!.currentUser!!.uid
        }
        updateUI()

       /* myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onDataChange(p0: DataSnapshot) {



                if(p0.child("Account").child(auth!!.currentUser!!.uid).exists())
                {
                    Log.d("crdcheck","2222"+auth!!.currentUser)
                    loginButton.visibility=View.GONE
                    myuid = auth!!.currentUser!!.uid
                }
                Log.d("crdcheck","second"+auth!!.currentUser)

            }
        })*/


        Session.getCurrentSession().addCallback(KakaoSessionCallback())
    }


    override fun onStart() {
        super.onStart()

    }

    /**
     * Update UI based on Firebase's current user. Show Login Button if not logged in.
     */
    private fun updateUI() {
        Log.d("crdcheck","updateUI"+myuid)
        if (!myuid.equals(null)) {
            loginButton.visibility = View.INVISIBLE
            binding!!.setCurrentUser(auth!!.currentUser)


            /**
             * SHOW LOGO FOR 2SEC
             */


            DirectLobby(myuid!!)
            /*if (currentUser.photoUrl != null) {
                Glide.with(this)
                        .load(currentUser.photoUrl)
                        .into(imageView)
            }
            loginButton.visibility = View.INVISIBLE
            loggedInView.visibility = View.VISIBLE
            logoutButton.visibility = View.VISIBLE*/
        } else {
            /**
             * SHOW LOGO FOR 2SEC
             */

            loginButton.visibility = View.VISIBLE

        }
    }

    /**
     * OnActivityResult() should be overridden for Kakao Login because Kakao Login uses startActivityForResult().
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)
    }

    /**
     *
     * @param kakaoAccessToken Access token retrieved after successful Kakao Login
     * @return Task object that will call validation server and retrieve firebase token
     */
    private fun getFirebaseJwt(kakaoAccessToken: String): Task<String> {
        val source = TaskCompletionSource<String>()

        val queue = Volley.newRequestQueue(this)
        val url = resources.getString(R.string.validation_server_domain) + "/verifyToken"
        val validationObject = HashMap<String, String>()
        validationObject["token"] = kakaoAccessToken

        val request = object : JsonObjectRequest(Request.Method.POST, url, JSONObject(validationObject), Response.Listener { response ->
            try {


                val firebaseToken = response.getString("firebase_token")
                source.setResult(firebaseToken)
            } catch (e: Exception) {
                source.setException(e)
            }
        }, Response.ErrorListener { error ->
            source.setException(error)
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["token"] = kakaoAccessToken
                return params
            }
        }
        // request.setRetryPolicy(DefaultRetryPolicy( 20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(request)
        return source.task
    }
    private fun DirectLobby( str : String)
    {

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {

                Mysex = p0.child("Account").child(str).child("Sex").getValue(true).toString()

                if(p0.child("Account").child(str).child("Myapply").exists()) {
                    MainActivity.checkapplylist = p0.child("Account").child(str).child("Myapply").getValue(CheckApplyListData::class.java)!!
                }
                else
                {
                    for(i in 1..MainActivity.checkapplylist!!.checklist!!.size)
                        MainActivity.checkapplylist!!.checklist!!.removeAt(0)
                }

                if(MainActivity.checkapplylist!!.checklist!!.size>1)
                {
                    for ( i in 1..MainActivity.checkapplylist!!.checklist!!.size-1)
                    //1부터인 이유 = 0번째는 "초기화"
                    {
                        //이미지난 매칭정보가 존재할 경우


                        var s1 = MainActivity.checkapplylist!!.checklist!![i].split("/")[0]
                        var s2 = MainActivity.checkapplylist!!.checklist!![i].split("/")[1]
                        var s3 = MainActivity.checkapplylist!!.checklist!![i].split("/")[2]


                        val today = Date()
                        var strdate: String? = null

                        var format1: SimpleDateFormat? = null
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            format1 = SimpleDateFormat("yyyy-MM-dd")

                            strdate = format1.format(today)

                        }


                        if(s3.compareTo(strdate!!)<0)
                        {


                            val delquery: Query = myRef.child("Apply").child("SubwayStation").child(s1)
                                    .child(s2).child(Mysex!!).orderByChild("name").equalTo(str)


                            delquery.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(p0: DatabaseError) {
                                }

                                override fun onDataChange(p0: DataSnapshot) {

                                    for (appleSnapshot in p0.getChildren()) {
                                        appleSnapshot.getRef().removeValue()

                                        MainActivity.checkapplylist!!.checklist!!.removeAt(i)
                                        myRef.child("Account").child(str).child("Myapply").setValue(MainActivity.checkapplylist)


                                    }

                                }
                            })
                        }





                    }
                }


                if(str==null)
                {

                }
                else if(!p0.child("Account").child(str).exists())
                {
                    DirectSignUp()
                }

                else if (p0.child("Account").child(str).exists() && p0.child("Account").child(str).child("ChatNum").exists())
                {

                    //아이디가 존재할 경우 && 매칭상대가 존재할 경우


                    Log.d("crdcheck","오잖아")
                    crd = p0.child("Account").child(str).child("ChatNum").getValue(ChatRoomData::class.java)!!
                    if(crd!!.ChatRoom.size>0) {
                        for (i in 0..crd!!.ChatRoom.size - 1) {

                            MainActivity.ChatRoomNum = crd!!.ChatRoom[i]
                            var strr = MainActivity.ChatRoomNum


                            val today = Date()
                            var strdate: String? = null

                            var format1: SimpleDateFormat? = null
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                format1 = SimpleDateFormat("yyyy-MM-dd")

                                strdate = format1.format(today)

                            }

                            if (p0.child("Chat").child(ChatRoomNum!!).exists()) {
                                //이미 지난 채팅방이 존재할 경우 삭제

                                if (p0.child("Chat").child(ChatRoomNum!!).child("Info").child("ChatRoomList").child("meetDate").getValue(true).toString().compareTo(strdate!!) < 0) {
                                    myRef.child("Chat").child(ChatRoomNum!!).removeValue()

                                    strr = strr!!.replace(str, "")
                                    Myuid = str



                                    crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!
                                    for (i in 0..crdd!!.Token.size - 1) {
                                        crdd!!.Token.remove(p0.child("Account").child(Myuid!!).child("fcmToken").getValue(true))
                                    }
                                    for (i in 0..crdd!!.ChatRoom.size - 1) {
                                        crdd!!.ChatRoom.remove(ChatRoomNum!!)
                                    }

                                    for (i in 0..crd!!.Token.size - 1) {
                                        crd!!.Token.remove(p0.child("Account").child(strr!!).child("fcmToken").getValue(true))
                                    }
                                    for (i in 0..crd!!.ChatRoom.size - 1) {
                                        crd!!.ChatRoom.remove(ChatRoomNum!!)
                                    }


                                    myRef.child("Account").child(Myuid!!).child("ChatNum").setValue(crd)
                                    myRef.child("Account").child(strr).child("ChatNum").setValue(crdd)
                                    ChatRoomNum = null

                                } else {
                                    strr = strr!!.replace(str, "")
                                    Myuid = str


                                }


                            } else {

                                strr = strr!!.replace(str, "")
                                Myuid = str


                                crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!

                                crdd!!.Token.remove(p0.child("Account").child(Myuid!!).child("fcmToken").getValue(true))
                                crdd!!.ChatRoom.remove(ChatRoomNum!!)

                                crd!!.Token.remove(p0.child("Account").child(strr!!).child("fcmToken").getValue(true))
                                crd!!.ChatRoom.remove(ChatRoomNum!!)


                                myRef.child("Account").child(Myuid!!).child("ChatNum").setValue(crd)
                                myRef.child("Account").child(strr).child("ChatNum").setValue(crdd)
                                ChatRoomNum = null


                            }

                        }
                    }


                    val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    startActivity(intent)
                }

                else {
                    //매칭 상대가 없을 경우



                    //  crdd = p0.child("Account").child(strr).child("ChatNum").getValue(ChatRoomData::class.java)!!


                    Myuid =FirebaseAuth.getInstance().uid





                    val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

                    startActivity(intent)

                }

            }
        })



    }



    private fun DirectSignUp()
    {
        var intent = Intent(this, Account::class.java)

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)

    }

    /**
     * Session callback class for Kakao Login. OnSessionOpened() is called after successful login.
     */
    private inner class KakaoSessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            Toast.makeText(applicationContext, "Successfully logged in to Kakao. Now creating or updating a Firebase User.", Toast.LENGTH_LONG).show()
            val accessToken = Session.getCurrentSession().accessToken
            getFirebaseJwt(accessToken!!).continueWithTask { task ->
                val firebaseToken = task.result
                auth = FirebaseAuth.getInstance()
                auth!!.signInWithCustomToken(firebaseToken!!)
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    myRef.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                        }

                        override fun onDataChange(p0: DataSnapshot) {

                            if (p0.child("Account").child(auth!!.currentUser!!.uid).exists()) {
                                DirectLobby(auth!!.currentUser!!.uid)
                            } else {
                                DirectSignUp()

                            }
                        }
                    })



                    //updateUI()
                } else {
                    Toast.makeText(applicationContext, "Failed to create a Firebase user.", Toast.LENGTH_LONG).show()
                    if (task.exception != null) {
                    }
                }
            }
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Log.e(TAG,"2" + exception.toString())
            }
        }
    }


    companion object {

        var activity: Activity? = null
        var Accountactivity : Activity? = null
        private val TAG = MainActivity::class.java.name
        var applyactivity : Activity? = null
        var ChatRoomNum : String? = null
        var Token : String? = null
        var Myuid : String? = null
        var Mysex : String? = null
        var crd  :ChatRoomData? = ChatRoomData()
        var crdtemp  :ChatRoomData? = ChatRoomData()
        var checkapplylist : CheckApplyListData? = CheckApplyListData()
        var checkapplylistt : CheckApplyListData? = CheckApplyListData()

        var crdd  :ChatRoomData? = ChatRoomData()
        var nowChatRoomNum : String? = null
        var nowToken : String? = null
        var nowAc : String? = null

    }



}