package com.test.moon.bblind

import android.content.Intent
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
import android.widget.Toast
import com.android.volley.*
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_apply.*

import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    lateinit var loginButton : LoginButton
    private var mAuth: FirebaseAuth?  = FirebaseAuth.getInstance()
    private var user : FirebaseUser?= mAuth!!.currentUser
    val database : FirebaseDatabase? = FirebaseDatabase.getInstance()
    val myRef : DatabaseReference = database!!.reference
    var auth : FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)




        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        loginButton = findViewById<View>(R.id.login_button) as LoginButton



        Session.getCurrentSession().addCallback(KakaoSessionCallback())
    }

    override fun onStart() {
        super.onStart()
        updateUI()
    }

    /**
     * Update UI based on Firebase's current user. Show Login Button if not logged in.
     */
    private fun updateUI() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            loginButton.visibility = View.INVISIBLE
            binding!!.setCurrentUser(currentUser)
            /**
             * SHOW LOGO FOR 2SEC
             */
            DirectLobby(currentUser.uid)
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
            Log.e(TAG, "3" +error.toString())
            source.setException(error)
        }) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["token"] = kakaoAccessToken
                return params
            }
        }
      // request.setRetryPolicy(DefaultRetryPolicy( 50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT))
        queue.add(request)
        return source.task
    }
    private fun DirectLobby( str : String)
    {

        myRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {





               if(str==null)
               {
                   Log.d("zczc","uid없음")

               }
                else if(!p0.child("Account").child(str).exists())
               {
                   Log.d("zczc","계정이없음")
                   DirectSignUp()
               }
                else if (p0.child("Account").child(str).exists()&&p0.child("Account").child(str).child("Match").getValue(true)!!.toString().equals("Y"))
                {

                    //매칭상대가 존재할 경우



                    Log.d("matchhh", "gogo")
                    MainActivity.ChatRoomNum = p0.child("Account").child(str).child("ChatRoomNum").getValue(true).toString()
                    var strr = MainActivity.ChatRoomNum

                    val today = Date()
                    var strdate : String? = null

                    var format1 : SimpleDateFormat? = null
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        format1 = SimpleDateFormat("yyyy-MM-dd")

                        strdate = format1.format(today)
                        Log.d("zczc",format1!!.format(today))
                        Log.d("cancelll","지워짐"+p0.child("Chat").child(strr!!).child("Info").child("MeetDate").getValue(true).toString()+"지금"+strdate)

                    }

                    if(p0.child("Chat").child(ChatRoomNum!!).child("Info").child("MeetDate").getValue(true).toString().compareTo(strdate!!)<0)
                    {

                        if(p0.child("Chat").child(ChatRoomNum!!).exists())
                        {
                            myRef.child("Chat").child(ChatRoomNum!!).removeValue()
                        }


                        strr = strr!!.replace(str, "")
                        Myuid = str
                        Log.d("cancellll","zz지워짐"+strr+"/"+Myuid)

                        if(p0.child("ApplyInformation").child(Myuid!!).exists())
                        {
                            myRef.child("ApplyInformation").child(Myuid!!).removeValue()

                            myRef.child("Account").child(Myuid!!).child("ChatRoomNum").setValue("")
                            myRef.child("Account").child(Myuid!!).child("Match").setValue("N")

                        }

                        if(p0.child("ApplyInformation").child(strr).exists())
                        {
                            myRef.child("ApplyInformation").child(strr).removeValue()

                            myRef.child("Account").child(strr).child("ChatRoomNum").setValue("")
                            myRef.child("Account").child(strr).child("Match").setValue("N")
                        }


                        Token=null
                        ChatRoomNum=null

                    }
                    else
                    {
                        strr = strr!!.replace(str, "")
                        Myuid = str
                        MainActivity.Token = p0.child("Account").child(strr!!).child("fcmToken").getValue(true).toString()


                    }
                    val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                    startActivity(intent)
                }

               else {
                   //매칭 상대가 없을 경우

                   Token=null
                   ChatRoomNum=null
                   Myuid = user!!.uid
                    Log.d("matchhh", "nono")
                   val intent = Intent(this@MainActivity, LobbyActivity::class.java)
                   startActivity(intent)

                }

            }
        })



    }



    private fun DirectSignUp()
    {
        var intent = Intent(this, Account::class.java)
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

                               if(p0.child("Account").child(auth!!.currentUser!!.uid).exists())
                               {
                                   DirectLobby(auth!!.currentUser!!.uid)
                               }
                                else
                               {
                                   DirectSignUp()
                               }
                           }
                       })




                    //updateUI()
                } else {
                    Toast.makeText(applicationContext, "Failed to create a Firebase user.", Toast.LENGTH_LONG).show()
                    if (task.exception != null) {
                        Log.e(TAG, "11111" + task.exception!!.toString())
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

        private val TAG = MainActivity::class.java.name
        var ChatRoomNum : String? = null
        var Token : String? = null
        var Myuid : String? = null

    }



}