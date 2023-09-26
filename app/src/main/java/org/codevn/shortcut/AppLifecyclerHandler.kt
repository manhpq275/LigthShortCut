package org.codevn.shortcut

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.res.Configuration
import android.os.Bundle


class AppLifecyclerHandler(private val mLifecycleDelegate: LifecycleDelegate): Application.ActivityLifecycleCallbacks, ComponentCallbacks2 {
    private var mIsAppInForeground = false

    override fun onActivityResumed(activity: Activity) {
        if (!mIsAppInForeground) {
            mIsAppInForeground = true
            mLifecycleDelegate.onAppForegrounded()
        }
    }

    override fun onConfigurationChanged(p0: Configuration) {
    }

    override fun onLowMemory() {
    }

    override fun onTrimMemory(flag: Int) {
        if (flag == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            mIsAppInForeground = false
            mLifecycleDelegate.onAppBackgrounded()
        }
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        super.onActivityPostCreated(p0, p1)
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityPaused(p0: Activity) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }
}