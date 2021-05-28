package com.rodriguesporan.itransfer

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context) {
    val builder = NotificationCompat.Builder(applicationContext, applicationContext.getString(R.string.itransfer_notification_channel_id))

            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}
