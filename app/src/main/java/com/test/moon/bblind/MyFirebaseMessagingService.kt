package com.test.moon.bblind

import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.util.Log
import com.test.moon.bblind.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if (remoteMessage.getNotification() != null) {
            val body = remoteMessage.getNotification()!!.getBody()
            Log.d(TAG, "Notification Body: $body")

            val notificationBuilder = NotificationCompat.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher) // 알림 영역에 노출 될 아이콘.
                    .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                    .setContentText(body) // Firebase Console 에서 사용자가 전달한 메시지내용

            val notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext())
            notificationManagerCompat.notify(0x1001, notificationBuilder.build())
        }
    }


    //dQ46cx4gPio:APA91bHyTDQ9fGLSubNSPrj0Zr2PLEXajqZVPw5GgMdPe3zvU9p-h1Ju01inn6RZtIdfdawsYC-tQFeT9ZGrSIDy6u7GJRYV_0ZYHb-ylfyjkgYuCCKFwfL6-8BxreBoyIJjZtMI0dND
    //d_pNjttl9VE:APA91bFuMwv8gj_gVUEuts-v2nHC6ws5FkNZ2U7wrMt9TgL5X71O37a242F1yurVrmcB7I9p3U9zMtl8WyY1aGFD2kg54VQ8_WJVwP3yzcT0FCIwYRqmcleEhYgyqVdIA8qkcGC6L10O


    override fun onNewToken(s: String) {
        super.onNewToken(s)

        Log.d("asdasdasd", s)


    }

    companion object {

        private val TAG = "asdasdasd"
    }


}
