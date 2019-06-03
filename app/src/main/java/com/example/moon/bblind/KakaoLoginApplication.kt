package com.example.moon.bblind

import android.app.Application;
import android.content.Context;

import com.google.firebase.FirebaseApp;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

class KakaoLoginApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        self = this
        FirebaseApp.initializeApp(this)
        KakaoSDK.init(object : KakaoAdapter() {
            override fun getApplicationConfig(): IApplicationConfig {
                return IApplicationConfig { self }
            }
        })

    }

    companion object {
        private var self: KakaoLoginApplication? = null
    }
}