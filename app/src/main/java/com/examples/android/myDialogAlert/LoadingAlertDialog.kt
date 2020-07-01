package com.examples.android.myDialogAlert

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import com.examples.android.R

class LoadingAlertDialog(private val activity: Activity) {
    private lateinit var alertDialog: AlertDialog

    init {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        builder.setView(inflater.inflate(R.layout.loading_alert_dialog, null))
        builder.setCancelable(true)
        alertDialog = builder.create()
    }

    fun show(): Unit {
        alertDialog.show()
    }


    fun dismiss(): Unit {
        alertDialog.dismiss()
    }


}