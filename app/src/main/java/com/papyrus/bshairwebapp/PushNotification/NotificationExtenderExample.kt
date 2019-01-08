package com.papyrus.bshairwebapp.PushNotification

import android.support.v4.app.NotificationCompat
import android.util.Log

import com.onesignal.NotificationExtenderService
import com.onesignal.OSNotificationDisplayedResult
import com.onesignal.OSNotificationReceivedResult

import java.math.BigInteger

class NotificationExtenderExample : NotificationExtenderService() {
    override fun onNotificationProcessing(receivedResult: OSNotificationReceivedResult): Boolean {
        // Read Properties from result
        val overrideSettings = NotificationExtenderService.OverrideSettings()
        overrideSettings.extender = NotificationCompat.Extender { builder ->
            // Sets the background notification color to Red on Android 5.0+ devices.
            builder.setColor(BigInteger("FFFF0000", 16).toInt())
        }

        val displayedResult = displayNotification(overrideSettings)
        Log.d("OneSignalExample", "Notification displayed with id: " + displayedResult.androidNotificationId)

        // Return true to stop the notification from displaying
        return true
    }
}
