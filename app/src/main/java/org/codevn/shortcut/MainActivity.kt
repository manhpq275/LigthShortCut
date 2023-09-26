package org.codevn.shortcut

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import org.codevn.shortcut.adapters.SliderAdapter
import org.codevn.shortcut.adapters.setPreviewBothSide
import org.codevn.shortcut.data.AdapterDataType
import org.codevn.shortcut.data.DataType
import org.codevn.shortcut.data.ShortCutData


class MainActivity : AppCompatActivity() {
    private lateinit var sliderChoose: ViewPager2
    private lateinit var sliderChooseAdapter: SliderAdapter
    private var dataChoose: ArrayList<ShortCutData> = ArrayList()
    private lateinit var background1: ImageView
    private lateinit var background2: ImageView
    private lateinit var title: AppCompatTextView
    private lateinit var description: AppCompatTextView
    private var currentPosition = 0

    companion object {
        var isVisible = false
    }

    private fun initData() {
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_silent_active,
                R.drawable.ic_silent,
                R.drawable.ic_b_silent,
                R.drawable.bg_silent,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_not_disturb_active,
                R.drawable.ic_not_disturb,
                R.drawable.ic_b_not_sturb,
                R.drawable.bg_not_disturb,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_camera_active,
                R.drawable.ic_camera,
                R.drawable.ic_b_camera,
                R.drawable.bg_camera,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_flash_active,
                R.drawable.ic_flash,
                R.drawable.ic_b_flash,
                R.drawable.bg_flash,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_memo_active,
                R.drawable.ic_memo,
                R.drawable.ic_b_memo,
                R.drawable.bg_memo,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_magnifier_active,
                R.drawable.ic_magnifier,
                R.drawable.ic_b_magnifier,
                R.drawable.bg_magnifier,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_shortcut_active,
                R.drawable.ic_shortcut,
                R.drawable.ic_b_shortcut,
                R.drawable.bg_short_cut,
                AdapterDataType.CHOOSE
            )
        )
        dataChoose.add(
            ShortCutData(
                R.drawable.ic_no_action_active,
                R.drawable.ic_no_action,
                R.drawable.ic_b_no_action,
                R.drawable.bg_no_action,
                AdapterDataType.CHOOSE
            )
        )
//        val firstChoose = dataChoose.first()
//        val lastChoose = dataChoose.last()
//        dataChoose.add(0, lastChoose)
//        dataChoose.add(firstChoose)
    }
    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        sliderChoose = findViewById(R.id.slider2)
        background1 = findViewById(R.id.imgBG1)
        background2= findViewById(R.id.imgBG2)
        title= findViewById(R.id.tvTitle)
        description= findViewById(R.id.tvDescription)
        sliderChooseAdapter = SliderAdapter(this)
        sliderChoose.apply {
            adapter = sliderChooseAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 10
            setPreviewBothSide(R.dimen._50dp,R.dimen._50dp)
        }

        sliderChoose.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            private var myState = 0
            override fun onPageScrollStateChanged(state: Int) {
                myState = state
                super.onPageScrollStateChanged(state)
            }
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (currentPosition == position) {
                    var tmpIndex = currentPosition + 1
                    if (tmpIndex == dataChoose.size) {
                        tmpIndex = 0
                    }
                    background1.setImageResource(dataChoose[tmpIndex].background)
                    background2.alpha = 1 - positionOffset
                    title.alpha = 1 - positionOffset
                    description.alpha = 1 - positionOffset
                } else {
                    background1.setImageResource(dataChoose[position].background)
                    background2.alpha = positionOffset
                    title.alpha = positionOffset
                    description.alpha = positionOffset
                }
                if (myState == ViewPager2.SCROLL_STATE_SETTLING) {
                    title.alpha = 0f
                    description.alpha =  0f
                    title.animate()
                        .setDuration(300)
                        .alpha(1.0f)
                        .setListener(null)
                    description.animate()
                        .setDuration(300)
                        .alpha(1.0f)
                        .setListener(null)
                }

                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                currentPosition = position
                super.onPageSelected(position)
                sliderChooseAdapter.setCurrentItem(position)
                background2.setImageResource(dataChoose[position].background)
                askPermissionIfNeed(position)
                createBubble(position)


            }


        })
        initData()
        sliderChooseAdapter.addItems(dataChoose, true)
        sliderChoose.setCurrentItem(currentPosition, false)
        sliderChooseAdapter.setCurrentItem(currentPosition)
        background2.setImageResource(dataChoose[currentPosition].background)
        TabLayoutMediator(findViewById(R.id.tabLayout), sliderChoose) { tab, position ->

        }.attach()
    }

    private fun askPermissionDialog(message: String, needUri: Boolean, permission: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Warning")
        builder.setMessage(message)
        builder.setPositiveButton("OK"
        ) { _, _ ->
            val packageName = packageName ?: return@setPositiveButton
            val intent = Intent(permission)
            if (needUri) {
                val uri = Uri.parse("package:$packageName")
                intent.data = uri
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            overlayPermissionLauncher.launch(intent)
        }
        builder.setNegativeButton("Cancel"
        ) { dialog, _ ->
            dialog.dismiss()
        }

        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }
    private fun askPermissionIfNeed(indexBubble: Int) {
        when (indexBubble) {
            DataType.SILENT.ordinal -> {
                val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if(!manager.isNotificationPolicyAccessGranted) {
                    askPermissionDialog("App cần quyền không làm phiền", false, android.provider.Settings
                        .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS)
                }
            }
            DataType.NOT_DISTURB.ordinal -> {

            }
            DataType.CAMERA.ordinal -> {
            }
            DataType.FLASH.ordinal -> {

            }
            DataType.MEMO.ordinal -> {

            }
            DataType.MAGNIFIER.ordinal -> {

            }
            DataType.SHORTCUT.ordinal -> {

            }
            else -> {

            }

        }
    }
    override fun onStop() {
        super.onStop()
        finishAndRemoveTask()

    }
    fun createBubble(position: Int){
        askForDrawOverlayPermission()
        stopMyService()
        val intent = Intent(this, MyServiceKt::class.java)
        intent.putExtra("size", 60)
        intent.putExtra("id", dataChoose[position].bubble)
        intent.putExtra("index", position)
        ContextCompat.startForegroundService(this, intent)
        isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        intent.extras?.let {
            currentPosition = it.getInt("index")
        }
        setContentView(R.layout.activity_main)
        initView()
    }

    private val overlayPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {}

    private fun askForDrawOverlayPermission() {
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
    }

}