package me.erika.nutrime.viewModel

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import androidx.lifecycle.ViewModel

class DrinkWaterViewModel: ViewModel(){

    val JOB_ID = 0

    fun scheduleWaterJob(jobScheduler: JobScheduler, serviceName: ComponentName,
                         selectedNetworkOption: Int, idle: Boolean, charging: Boolean, deadline: Long){

        val builder = JobInfo.Builder(JOB_ID, serviceName).setRequiredNetworkType(selectedNetworkOption)
            .setRequiresDeviceIdle(idle)
            .setRequiresCharging(charging)

        if(deadline > 0){
            builder.setOverrideDeadline(deadline)
        }

        //Passing info to the scheduler
        val myJobInfo = builder.build()
        jobScheduler.schedule(myJobInfo)
    }

    fun cancelScheduledJob(jobScheduler: JobScheduler){
        jobScheduler.cancelAll()
    }

}