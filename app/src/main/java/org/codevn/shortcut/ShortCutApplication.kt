package org.codevn.shortcut

import android.app.Application
import android.util.Log

interface LifecycleDelegate {
    fun onAppBackgrounded()
    fun onAppForegrounded()
}

class ShortCutApplication: Application(), LifecycleDelegate {

    private lateinit var appLifecycleHandler: AppLifecyclerHandler
    override fun onCreate() {
        super.onCreate()
        appLifecycleHandler = AppLifecyclerHandler(this)
        registerActivityLifecycleCallbacks(appLifecycleHandler)
        registerComponentCallbacks(appLifecycleHandler)
    }
    override fun onAppBackgrounded() {
        Log.e("ShortCutApplication", "AppBackgrounded")
    }

    override fun onAppForegrounded() {
        Log.e("ShortCutApplication", "Foregrounded")

    }
}