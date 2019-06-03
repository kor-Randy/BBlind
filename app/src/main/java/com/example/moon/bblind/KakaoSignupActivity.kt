package com.example.moon.bblind

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import android.content.Intent



class KakaoSignupActivity : Activity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaorequestMe()
    }

    private fun redirectLoginActivity()
    {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }
    private fun redirectAccountActivity()
    {
        startActivity(Intent(this, Account::class.java))
        finish()
    }
    private fun redirectmainActivity()
    {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        startActivity(intent)
        finish()
    }
    protected fun KakaorequestMe() {

        UserManagement.getInstance().requestMe(object : MeResponseCallback() {
            override fun onFailure(errorResult: ErrorResult?) {
                val ErrorCode = errorResult!!.errorCode
                val ClientErrorCode = -777

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(this@KakaoSignupActivity, "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@KakaoSignupActivity,"오류로 로그인 실패", Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "오류로 카카오로그인 실패 ")
                    redirectLoginActivity()
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult) {
                Toast.makeText(this@KakaoSignupActivity,"오류로 로그인 실패2", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "오류로 카카오로그인 실패 ")
                redirectLoginActivity()
            }

            override fun onSuccess(userProfile: UserProfile) {

                Toast.makeText(this@KakaoSignupActivity,"로그인 성공", Toast.LENGTH_SHORT).show()
                redirectmainActivity()


            }

            override fun onNotSignedUp() {
                Toast.makeText(this@KakaoSignupActivity,"동의창", Toast.LENGTH_SHORT).show()
                // 자동가입이 아닐경우 동의창
                //redirectAccountActivity()
            }
        })
    }

}