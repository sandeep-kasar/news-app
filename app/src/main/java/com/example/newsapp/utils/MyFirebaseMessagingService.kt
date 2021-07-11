package com.example.newsapp.utils

import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.newsapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * This class is used to handle FCM notification
 *
 * @author SandeepK
 * @version 1.0
 */
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var TAG = "Firebase"

    /**
     * Called if the FCM registration token is updated.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        // FCM registration token to your app server for further use case
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token:String){
        //send token to server
    }

    /**
     * This method is for received the FCM notification message
     *
     * @param remoteMessage: RemoteMessage
     */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            handleNow(remoteMessage.data.get("title")!!, remoteMessage.data.get("body")!!)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            // generate notification
            handleNow(it.title!!, it.body!!)
        }
    }


    /**
     * This function will generate notification when app is in foreground
     *
     * @param title:String, this is the title of notification
     *
     * @param body:String, this is the body of notification
     *
     * */
    private fun handleNow(title:String,body:String){
        // Create an explicit intent for an Activity in your app
        val intent = Intent(this, this::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val builder = NotificationCompat.Builder(this, "News App")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // Set the intent that will fire when the user taps the notification
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1001, builder.build())
        }
    }

}