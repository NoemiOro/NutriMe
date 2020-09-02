package me.erika.nutrime.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_drink_water.*
import me.erika.nutrime.R


class DrinkWaterActivity :AppCompatActivity() {

    //Create notification with a channel
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    private val PRIMARY_CHANNEL_ID = "drink_water_channel_id"
    private val CHANEL_NAME = "Miscellaneous"
    private val NOTIFICATION_ID = 0
    //Add action to the notification
    private val ACTION_UPDATE_NOTIFICATION =
        "me.erika.nutrime.view.ACTION_UPDATE_NOTIFICATION"
    var mReceiver: NotificationReceiver = NotificationReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_water)

        drink_water_notify_btn.setOnClickListener {
            sendNotification()
        }

        drink_water_update_btn.setOnClickListener {
            updateNotification()
        }

        drink_water_delete_btn.setOnClickListener {
            cancelNotification()
        }

        createNotificationChannel()
        registerReceiver(mReceiver, IntentFilter( ACTION_UPDATE_NOTIFICATION))
    }

    private fun createNotificationChannel() {

        ////1.-NotificationManager is a class to notify the user of events that happen.
        notificationManager =  getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        //2.-Create a channel
        //Notification channels are only available in API 26 and higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Each notification channel represents a type of notification.
            //It's possible to group several notifications in each notification channel.
            //For each notification channel, the app sets behavior for the channel, and the behavior is applied to all the notifications in the channel
            notificationChannel = NotificationChannel(PRIMARY_CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription(getString(R.string.drink_water_notification_channel_description))
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder {

        //By using pending item to communicate with other app, we are telling that app to execute some predefined code
        //at some point in the future. The other app can perform an action on behalf of our app.
        val notificationIntent = Intent(this, DrinkWaterActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, notificationIntent,
        PendingIntent.FLAG_UPDATE_CURRENT)
        val waterImage = BitmapFactory
            .decodeResource(resources, R.drawable.img_water)
        //To add an action
        val updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        //Wrap up intent in a pending intent to be executed later
        val updatePendingIntent = PendingIntent.getBroadcast(
            this, NOTIFICATION_ID,
            updateIntent, PendingIntent.FLAG_ONE_SHOT)

        //3.-Build notification
        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle(getString(R.string.drink_water_notification_title))
            .setContentText(getString(R.string.drink_water_notification_text))
            .setSmallIcon(R.drawable.ic_water_glass)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(waterImage))
            .addAction(R.drawable.ic_snooze, getString(R.string.drink_water_notification_snooze), updatePendingIntent)
        //Android 7 an avobe don't show notification icon, however it is still required.
        // Still used for wearable and older versions
        return notifyBuilder;
    }

    private fun sendNotification() {
        val notifyBuilder = getNotificationBuilder()
        //4.- Send notification
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
    }

    private fun updateNotification() {
        val waterImage = BitmapFactory
            .decodeResource(resources, R.drawable.img_water)
        val notifyBuilder = getNotificationBuilder()
        notifyBuilder.setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(waterImage)
            .setBigContentTitle("Notification Updated"))
        notificationManager.notify(NOTIFICATION_ID, notifyBuilder.build())
    }

    private fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    inner class NotificationReceiver: BroadcastReceiver() {

        override fun onReceive(p0: Context?, p1: Intent?) {
            updateNotification()
            //Todo Snooze water notification here
        }

    }

}