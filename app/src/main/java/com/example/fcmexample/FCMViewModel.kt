package com.example.fcmexample

import androidx.lifecycle.ViewModel
import com.example.fcmexample.db.FCMExampleDB

class FCMViewModel(private val db: FCMExampleDB) : ViewModel() {

    val notifications = db.notificationsDao().getNotificationsObservable()

}