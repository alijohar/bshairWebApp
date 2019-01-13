package com.papyrus.bshairwebapp

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import com.onesignal.OSNotification
import com.onesignal.OSNotificationAction
import com.onesignal.OSNotificationOpenResult
import com.onesignal.OneSignal
import com.papyrus.bshairwebapp.Ui.MainActivity
import com.papyrus.bshairwebapp.Ui.SingleNewsFromPush

class bshaerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // OneSignal Initialization
        OneSignal.startInit(this)
                .setNotificationReceivedHandler(MyNotificationReceivedHandler())
                .setNotificationOpenedHandler(MyNotificationOpenedHandler())
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init()
    }

    inner class MyNotificationReceivedHandler : OneSignal.NotificationReceivedHandler {
        override fun notificationReceived(notification: OSNotification) {
            val data = notification.payload.additionalData
            val notificationID = notification.payload.notificationID
            val title = notification.payload.title
            val body = notification.payload.body
            val smallIcon = notification.payload.smallIcon
            val largeIcon = notification.payload.largeIcon
            val bigPicture = notification.payload.bigPicture
            val smallIconAccentColor = notification.payload.smallIconAccentColor
            val sound = notification.payload.sound
            val ledColor = notification.payload.ledColor
            val lockScreenVisibility = notification.payload.lockScreenVisibility
            val groupKey = notification.payload.groupKey
            val groupMessage = notification.payload.groupMessage
            val fromProjectNumber = notification.payload.fromProjectNumber
            val rawPayload = notification.payload.rawPayload

            val customKey: String?

            Log.i("OneSignalExample", "NotificationID received: $notificationID")

            if (data != null) {
                customKey = data.optString("customKey", null)
                if (customKey != null)
                    Log.i("OneSignalExample", "customKey set with value: $customKey")
            }
        }
    }

    inner class MyNotificationOpenedHandler : OneSignal.NotificationOpenedHandler {
        // This fires when a notification is opened by tapping on it.
        override fun notificationOpened(result: OSNotificationOpenResult) {
            val actionType = result.action.type
            val data = result.notification.payload.additionalData
            val launchUrl = result.notification.payload.launchURL // update docs launchUrl

            val customKey: String?
            var openURL: String? = null
            var openApp:String? = null
            var activityToLaunch: Any = MainActivity::class.java

            if (data != null) {
                customKey = data.optString("customKey", null)
                openURL = data.optString("openURL", null)
                openApp = data.optString("openApp", null)

                if (customKey != null) {
                    Log.i("OneSignalExample", "customKey set with value: $customKey")
                    activityToLaunch = SingleNewsFromPush::class.java
                    val intent = Intent(applicationContext, activityToLaunch as Class<*>)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.putExtra("customKey", customKey)
                    startActivity(intent)
                }


                    if (openURL != null) {
                        Log.i("OneSignalExample", "openURL to webview with URL value: $openURL")
                        val uris = Uri.parse(openURL)
                        val intents = Intent(Intent.ACTION_VIEW, uris)
                        val b = Bundle()
                        b.putBoolean("new_window", true)
                        intents.putExtras(b)
                        startActivity(intents)
                    }

                if (openApp != null) {
                    Log.i("OneSignalExample", "App will opened")
                    activityToLaunch = MainActivity::class.java
                    val intent = Intent(applicationContext, activityToLaunch as Class<*>)
                    intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }

            }
    }



}
}
