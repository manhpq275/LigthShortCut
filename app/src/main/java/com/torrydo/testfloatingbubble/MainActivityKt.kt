package com.torrydo.testfloatingbubble

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivityKt : AppCompatActivity() {

    companion object{
        var isVisible = false
        var isBubbleRunning = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)

        button.setOnClickListener {
            askForDrawOverlayPermission()
            if (isBubbleRunning) {
                stopMyService()
            } else {

                val intent = Intent(this, MyServiceKt::class.java)
                intent.putExtra("size", 60)
                intent.putExtra("noti_message", "HELLO FROM MAIN ACT")
                ContextCompat.startForegroundService(this, intent)
                isVisible = false
            }

            isBubbleRunning = !isBubbleRunning
        }
    }
    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {}

    fun askForDrawOverlayPermission() {
        if (Settings.canDrawOverlays(this)) {
            return
        }
        val packageName = packageName ?: return
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName"),
        )

        overlayPermissionLauncher.launch(intent)
    }

    private fun stopMyService() {
        val intent = Intent(this, MyServiceKt::class.java)
        stopService(intent)
        Toast.makeText(this, "stop service", Toast.LENGTH_SHORT).show()
    }

}