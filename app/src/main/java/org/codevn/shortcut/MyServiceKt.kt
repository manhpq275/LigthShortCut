package org.codevn.shortcut

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.net.Uri
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import org.codevn.floatingbubbleview.CloseBubbleBehavior
import org.codevn.floatingbubbleview.FloatingBubbleListener
import org.codevn.floatingbubbleview.helper.NotificationHelper
import org.codevn.floatingbubbleview.helper.ViewHelper
import org.codevn.floatingbubbleview.service.expandable.BubbleBuilder
import org.codevn.floatingbubbleview.service.expandable.ExpandableBubbleService
import org.codevn.floatingbubbleview.service.expandable.ExpandedBubbleBuilder
import org.codevn.shortcut.data.DataType


class MyServiceKt : ExpandableBubbleService() {
    private var iconBubble: Int = -1
    private var indexBubble: Int = -1
    var flashLightStatus: Boolean = false
    var oldRingMode: Int = -1
    override fun startNotificationForeground() {
        val noti = NotificationHelper(this)
        noti.createNotificationChannel()
        startForeground(noti.notificationId, noti.defaultNotification())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.extras?.let {
            iconBubble = it.getInt("id")
            indexBubble = it.getInt("index")

            setup()
            minimize()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun getIntentApp(packageName: String): Intent? {
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        launchIntent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        return launchIntent
    }

    private fun doNotDisturbChange() {
        val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (manager.isNotificationPolicyAccessGranted) {
            val current = manager.currentInterruptionFilter
            if (current == NotificationManager.INTERRUPTION_FILTER_ALL) {
                manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_PRIORITY);
            } else {
                manager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_ALL);
            }

        } else {
            val intent = getIntentApp("org.codevn.shortcut")
            intent?.let {
                intent.putExtra("index", indexBubble)
                startActivity(intent)
            }
        }
    }

    private fun silentModeChange() {
        val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (manager.isNotificationPolicyAccessGranted) {
            val am = getSystemService(AUDIO_SERVICE) as AudioManager
            if (oldRingMode != -1 &&
                am.ringerMode == AudioManager.RINGER_MODE_SILENT &&
                oldRingMode != AudioManager.RINGER_MODE_SILENT
            ) {
                am.ringerMode = oldRingMode
            } else if (am.ringerMode != AudioManager.RINGER_MODE_SILENT) {
                oldRingMode = am.ringerMode
                am.ringerMode = AudioManager.RINGER_MODE_SILENT
            }

        } else {
            val intent = getIntentApp("org.codevn.shortcut")
            intent?.let {
                intent.putExtra("index", indexBubble)
                startActivity(intent)
            }
        }
    }

    override fun configBubble(): BubbleBuilder? {
        var imgView = ViewHelper.fromDrawable(this, R.drawable.silent_mode, 60, 60)

        if (iconBubble != 0) {
            imgView = ViewHelper.fromDrawable(this, iconBubble, 60, 60)
        }
        imgView.setOnLongClickListener {
            val intent = getIntentApp("org.codevn.shortcut")
            intent?.let {
                intent.putExtra("index", indexBubble)
                startActivity(intent)
            }

            false
        }
        imgView.setOnClickListener {
            when (indexBubble) {
                DataType.SILENT.ordinal -> {
                    silentModeChange()
                }
                DataType.NOT_DISTURB.ordinal -> {
                    doNotDisturbChange()
                }
                DataType.CAMERA.ordinal -> {
                    val intent = getIntentApp("com.android.camera")
                    intent?.let {
                        intent.putExtra("index", indexBubble)
                        startActivity(intent)
                    }
                }
                DataType.FLASH.ordinal -> {
                    openFlashLight()
                }
                DataType.MEMO.ordinal -> {
                    val intent = getIntentApp("com.android.soundrecorder")
                    intent?.let {
                        startActivity(intent)
                    }
                }
                DataType.MAGNIFIER.ordinal -> {

                }
                DataType.SHORTCUT.ordinal -> {

                }
                else -> {

                }

            }

        }
        return BubbleBuilder(this)
            // set bubble view
            .bubbleView(imgView)
            .bubbleStyle(null)
            .startLocation(100, 100)    // in dp
            .startLocationPx(100, 100)  // in px
            .enableAnimateToEdge(true)
            //  .closeBubbleView(ViewHelper.fromDrawable(this, R.drawable.ic_close_bubble, 60, 60))
            .closeBubbleStyle(null)
            .closeBehavior(CloseBubbleBehavior.FIXED_CLOSE_BUBBLE)
            .distanceToClose(100)
            .bottomBackground(false)

            .addFloatingBubbleListener(object : FloatingBubbleListener {
                override fun onFingerMove(
                    x: Float,
                    y: Float
                ) {
                } // The location of the finger on the screen which triggers the movement of the bubble.

                override fun onFingerUp(
                    x: Float,
                    y: Float
                ) {

                }   // ..., when finger release from bubble

                override fun onFingerDown(x: Float, y: Float) {} // ..., when finger tap the bubble
            })

    }

    @SuppressLint("DiscouragedApi")
    override fun configExpandedBubble(): ExpandedBubbleBuilder? {

        val expandedView = LayoutInflater.from(this).inflate(R.layout.layout_view_test, null)
        expandedView.findViewById<View>(R.id.btn).setOnClickListener {
            minimize()
        }

        return ExpandedBubbleBuilder(this)
            .expandedView(expandedView)
            .onDispatchKeyEvent {
                if (it.keyCode == KeyEvent.KEYCODE_BACK) {
                    minimize()
                }
                null
            }
            .startLocation(0, 0)
            .draggable(true)
            .style(null)
            .fillMaxWidth(false)
            .enableAnimateToEdge(true)
            .dimAmount(0.5f)
    }

    private fun openFlashLight() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0]
        if (!flashLightStatus) {
            try {
                cameraManager.setTorchMode(cameraId, true)
                flashLightStatus = true

            } catch (e: CameraAccessException) {
            }
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false)
                flashLightStatus = false
            } catch (e: CameraAccessException) {
            }
        }

    }
}