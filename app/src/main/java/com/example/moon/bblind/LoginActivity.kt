package com.example.moon.bblind

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.kakao.auth.AuthType
//import jdk.nashorn.internal.runtime.ECMAErrors.getMessage
import com.kakao.util.exception.KakaoException
import com.kakao.auth.ISessionCallback
import android.content.pm.PackageManager
import android.content.pm.PackageInfo
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
//import sun.security.krb5.internal.KDCOptions.with
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.network.ErrorResult
import android.widget.Toast
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.UserManagement
import java.security.MessageDigest
import android.widget.TextView
import com.kakao.auth.Session
import com.squareup.picasso.Picasso


class LoginActivity :AppCompatActivity() {

    private lateinit var mKakaocallback: SessionCallback
    // view
    private lateinit var login_button: Button
    private lateinit var tv_user_id: TextView
    private lateinit var tv_user_name: TextView
    private lateinit var iv_user_profile: ImageView
    private lateinit var userName: String
    private lateinit var userId: String
    private lateinit var profileUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

       // tv_user_id = findViewById(R.id.tv_user_id)
        //tv_user_name = findViewById(R.id.tv_user_name)
        //iv_user_profile = findViewById(R.id.iv_user_profile)

       // login_button = findViewById(R.id.com_kakao_login)
        mKakaocallback = SessionCallback()
        Session.getCurrentSession().addCallback(mKakaocallback)
    }
    protected fun redirectSignupActivity() {       //세션 연결 성공 시 SignupActivity로 넘김
        var intent = Intent(this,KakaoSignupActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(Session.getCurrentSession().handleActivityResult(requestCode,resultCode,data)){return}
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        Session.getCurrentSession().removeCallback(mKakaocallback)
    }
    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            Toast.makeText(this@LoginActivity,"세션오픈",Toast.LENGTH_SHORT).show()
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            redirectSignupActivity()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Toast.makeText(this@LoginActivity,"세션실패",Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Session CallBack Error > " + exception.message)
                setContentView(R.layout.activity_login)
            }
        }


    }
}