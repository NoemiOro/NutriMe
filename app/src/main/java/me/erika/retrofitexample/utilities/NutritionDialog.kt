package me.erika.retrofitexample.utilities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import me.erika.retrofitexample.R


class NutritionDialog{

    fun simpleAlert(context: Context,
                    message: String,
                    title: String = context.getString(R.string.alert_title)){

            // build dialog
            val dialogBuilder = AlertDialog.Builder(context)
            dialogBuilder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Ok", DialogInterface.OnClickListener {
                        dialog, id -> dialog.dismiss()
                })

            // create dialog box
            val alert = dialogBuilder.create()
            alert.setTitle(title)
            alert.show()
    }
}