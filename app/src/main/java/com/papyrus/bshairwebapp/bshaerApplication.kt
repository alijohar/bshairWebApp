package com.papyrus.bshairwebapp

import android.app.Application
import android.content.Intent
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
                customKey = data.optString("customkey", null)
                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: $customKey")
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
            var activityToLaunch: Any = MainActivity::class.java

            if (data != null) {
                customKey = data.optString("customkey", null)
                openURL = data.optString("openURL", null)

                if (customKey != null)
                    Log.i("OneSignalExample", "customkey set with value: $customKey")
                    activityToLaunch = SingleNewsFromPush::class.java


                if (openURL != null)
                    Log.i("OneSignalExample", "openURL to webview with URL value: $openURL")
            }

            if (actionType == OSNotificationAction.ActionType.ActionTaken) {
                Log.i("OneSignalExample", "Button pressed with id: " + result.action.actionID)

                if (result.action.actionID == "id1") {
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID)
                    activityToLaunch = SingleNewsFromPush::class.java
                } else
                    Log.i("OneSignalExample", "button id called: " + result.action.actionID)
            }
            // The following can be used to open an Activity of your choice.
            // Replace - getApplicationContext() - with any Android Context.
            // Intent intent = new Intent(getApplicationContext(), YourActivity.class);
            val intent = Intent(applicationContext, activityToLaunch as Class<*>)
            // intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT or Intent.FLAG_ACTIVITY_NEW_TASK
//            intent.putExtra("openURL", openURL)
//            Log.i("OneSignalExample", "openURL = " + openURL!!)
//            // startActivity(intent);
            startActivity(intent)


        }
    }



}
