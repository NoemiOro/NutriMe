package me.erika.nutrime.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.Color
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_drink_water.*
import me.erika.nutrime.NotificationJobService
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

    //JobScheduler
    val JOB_ID = 0
    lateinit var mScheduler: JobScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drink_water)

        createNotificationChannel()
        registerReceiver(mReceiver, IntentFilter( ACTION_UPDATE_NOTIFICATION))

        drink_water_settings_expandable_tv.setOnClickListener{
            updateNotificationSettingsUI()
        }

        drink_water_create_expandable_tv.setOnClickListener{
            updateCreateReminderUI()
        }

        drink_water_notify_btn.setOnClickListener {
            sendNotification()
        }

        drink_water_update_btn.setOnClickListener {
            updateNotification()
        }

        drink_water_delete_btn.setOnClickListener {
            cancelNotification()
        }

        drink_water_schedule_btn.setOnClickListener{
            getNetworkPreference()
        }

        drink_water_cancel_btn.setOnClickListener{
            cancelJobs()
        }
    }

    private fun updateCreateReminderUI() {
        if(drink_water_create_cl.visibility == View.GONE){
            drink_water_create_cl.visibility = View.VISIBLE
            drink_water_create_expandable_tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            drink_water_create_expandable_tv.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this,android.R.drawable.ic_menu_add), null,
                ContextCompat.getDrawable(this, android.R.drawable.arrow_up_float), null)
        }
        else{
            drink_water_create_cl.visibility = View.GONE
            drink_water_create_expandable_tv.setTextColor(ContextCompat.getColor(this, R.color.colorFontDark))
            drink_water_create_expandable_tv.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this,android.R.drawable.ic_menu_add), null,
                ContextCompat.getDrawable(this, android.R.drawable.arrow_down_float), null)
        }
    }

    private fun updateNotificationSettingsUI() {
        if(drink_water_settings_cl.visibility == View.GONE){
            drink_water_settings_cl.visibility = View.VISIBLE
            drink_water_settings_expandable_tv.setTextColor(ContextCompat.getColor(this, R.color.colorPrimaryDark))
            drink_water_settings_expandable_tv.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this,android.R.drawable.ic_menu_preferences), null,
                ContextCompat.getDrawable(this, android.R.drawable.arrow_up_float), null)
        }
        else{
            drink_water_settings_cl.visibility = View.GONE
            drink_water_settings_expandable_tv.setTextColor(ContextCompat.getColor(this, R.color.colorFontDark))
            drink_water_settings_expandable_tv.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(this,android.R.drawable.ic_menu_preferences), null,
                ContextCompat.getDrawable(this, android.R.drawable.arrow_down_float), null)
        }
    }

    //region Notification example
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
        //Android 7 an above don't show notification icon, however it is still required.
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
    //endregion

    //region Job Scheduler example

    fun getNetworkPreference(){

        mScheduler =  getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val selectedNetworkID = drink_water_network_options_rg.checkedRadioButtonId
        var selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE

        when (selectedNetworkID) {
            R.id.drink_water_none -> selectedNetworkOption = JobInfo.NETWORK_TYPE_NONE
            R.id.drink_water_any -> selectedNetworkOption = JobInfo.NETWORK_TYPE_ANY
            R.id.drink_water_wifi -> selectedNetworkOption = JobInfo.NETWORK_TYPE_UNMETERED
        }

        //Used to associate jobService and jobInfo
        //jobService is the job and jobInfo is the critteria which defines when the job runs
        val serviceName = ComponentName(
            packageName,
            NotificationJobService::class.java.name
        )
        val builder = JobInfo.Builder(JOB_ID, serviceName).setRequiredNetworkType(selectedNetworkOption)

        //Passing jobInfo to scheduler
        val myJobInfo = builder.build()
        mScheduler.schedule(myJobInfo)

        Toast.makeText(this, "Job Scheduled, job will run when " +
                "the constraints are met.", Toast.LENGTH_SHORT).show();

    }

    private fun cancelJobs() {
        if (mScheduler!=null){
            mScheduler.cancelAll()
            Toast.makeText(this, "Jobs cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    //endregion
}