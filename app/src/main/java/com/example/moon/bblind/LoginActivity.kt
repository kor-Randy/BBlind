package com.example.moon.bblind

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

        tv_user_id = findViewById(R.id.tv_user_id)
        tv_user_name = findViewById(R.id.tv_user_name)
        iv_user_profile = findViewById(R.id.iv_user_profile)

        login_button = findViewById(R.id.login_button)
        login_button.setOnClickListener{isKakaoLogin()}
    }

    private fun isKakaoLogin() {
        // 카카오 세션을 오픈한다
        //mKakaocallback = object : SessionCallback()
        mKakaocallback = SessionCallback()
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback)
        KakaorequestMe()
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen()
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, this)
    }

    protected fun KakaorequestMe() {

        UserManagement.getInstance().requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                val ErrorCode = errorResult!!.errorCode
                val ClientErrorCode = -777

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(this@LoginActivity, "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity,"오류로 로그인 실패",Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "오류로 카카오로그인 실패 ")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Toast.makeText(this@LoginActivity,"오류로 로그인 실패2",Toast.LENGTH_SHORT).show()
                Log.d("TAG", "오류로 카카오로그인 실패 ")
            }

            override fun onSuccess(userProfile: UserProfile) {
                profileUrl = userProfile.profileImagePath
                userId = userProfile.id.toString()
                userName = userProfile.nickname
                Toast.makeText(this@LoginActivity,"로그인 성공",Toast.LENGTH_SHORT).show()
                Log.i("TAG", "prifileUrl:$profileUrl")
                Log.i("TAG", "userId:$userId")
                Log.i("TAG", "userName:$userName")

                setLayoutText()
            }

            override fun onNotSignedUp() {
                Toast.makeText(this@LoginActivity,"동의창",Toast.LENGTH_SHORT).show()
                // 자동가입이 아닐경우 동의창
            }
        })
    }

    private fun setLayoutText() {
        Toast.makeText(this@LoginActivity,"레이아웃",Toast.LENGTH_SHORT).show()
        tv_user_id.setText(userId)
        tv_user_name.setText(userName)

        Picasso.with(this)
                .load(profileUrl)
                .fit()
                .into(iv_user_profile)
    }



    private inner class SessionCallback : ISessionCallback {
        override fun onSessionOpened() {
            Toast.makeText(this@LoginActivity,"세션오픈",Toast.LENGTH_SHORT).show()
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            if (exception != null) {
                Toast.makeText(this@LoginActivity,"세션실패",Toast.LENGTH_SHORT).show()
                Log.d("TAG", "Session CallBack Error > " + exception.message)
            }
        }
    }
}