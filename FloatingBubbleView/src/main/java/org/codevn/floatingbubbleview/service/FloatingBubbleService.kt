package org.codevn.floatingbubbleview.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.codevn.floatingbubbleview.canDrawOverlays
import org.codevn.floatingbubbleview.helper.NotificationHelper
import org.codevn.floatingbubbleview.sez


abstract class FloatingBubbleService : Service() {

    open fun startNotificationForeground() {
        val noti = NotificationHelper(this)
        noti.createNotificationChannel()
        startForeground(noti.notificationId, noti.defaultNotification())
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()

        if (canDrawOverlays().not()) {
            return
        }

        sez.with(this.applicationContext)

        startNotificationForeground()
       // setup()
    }

    abstract fun setup()

    abstract fun removeAll()

    override fun onDestroy() {
        removeAll()
        super.onDestroy()
    }
}