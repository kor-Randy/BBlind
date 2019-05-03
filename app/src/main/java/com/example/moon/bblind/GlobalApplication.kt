package com.example.moon.bblind

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.kakao.auth.*
import com.kakao.auth.ApprovalType
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionConfig
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.KakaoAdapter
import com.kakao.auth.KakaoSDK


class GlobalApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        mInstance = this
        KakaoSDK.init(KakaoSDKAdapter())
    }

    companion object {

        private var mInstance: GlobalApplication? = null
        @Volatile
        var currentActivity: Activity? = null
            get() {
                Log.d("TAG", "++ currentActivity : " + if (field != null) field!!.javaClass.simpleName else "")
                return field
            }

        val globalApplicationContext: GlobalApplication
            get() {
                if (mInstance == null)
                    throw IllegalStateException("this application does not inherit GlobalApplication")
                return mInstance!!
            }
    }
}

class KakaoSDKAdapter : KakaoAdapter() {

    override fun getSessionConfig(): ISessionConfig {
        return object : ISessionConfig {
            override fun getAuthTypes(): Array<AuthType> {
                return arrayOf(AuthType.KAKAO_LOGIN_ALL)
            }

            override fun isUsingWebviewTimer(): Boolean {
                return false
            }

            override fun isSecureMode(): Boolean {
                return true
            }

            override fun getApprovalType(): ApprovalType? {
                return ApprovalType.INDIVIDUAL
            }

            override fun isSaveFormData(): Boolean {
                return true
            }
        }
    }

    override fun getApplicationConfig(): IApplicationConfig {
        return IApplicationConfig { GlobalApplication.globalApplicationContext }
    }
}