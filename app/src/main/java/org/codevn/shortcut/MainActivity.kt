package org.codevn.shortcut

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.jaredrummler.materialspinner.MaterialSpinner
import org.codevn.shortcut.adapters.DropDownListAdapter
import org.codevn.shortcut.adapters.SliderAdapter
import org.codevn.shortcut.adapters.setPreviewBothSide
import org.codevn.shortcut.data.AppListMain
import org.codevn.shortcut.data.DataType
import org.codevn.shortcut.data.ShortCutConfig
import java.lang.reflect.Field
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private lateinit var sliderChoose: ViewPager2
    private lateinit var sliderChooseAdapter: SliderAdapter
    private var dataChoose: ArrayList<DataType> = ArrayList()
    private lateinit var background1: ImageView
    private lateinit var background2: ImageView
    private lateinit var title: AppCompatTextView
    private lateinit var description: AppCompatTextView
    private var currentPosition = 0
    private lateinit var spinner: MaterialSpinner
    private lateinit var shortCutConfig: ShortCutConfig
    private var sourceAdditional : Array<String> = arrayOf()
    private var appListMainArrayList: ArrayList<AppListMain> = ArrayList()

    companion object {
        var isVisible = false
        private const val CAMERA_PERMISSION = android.Manifest.permission.CAMERA
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }
    @SuppressLint("QueryPermissionsNeeded")
    fun loadApps() {
        try {
            appListMainArrayList = ArrayList()
            val intent = Intent(Intent.ACTION_MAIN, null)
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            for (resolveInfo in resolveInfoList) {
                val appListMain = AppListMain(
                    resolveInfo.activityInfo.loadIcon(packageManager),
                    resolveInfo.loadLabel(packageManager).toString(),
                    resolveInfo.activityInfo.packageName
                )
                appListMainArrayList.add(appListMain)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @SuppressLint("ClickableViewAccessibility", "DiscouragedPrivateApi")
    private fun initView() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        spinner = findViewById(R.id.additionalOption)

        sliderChoose = findViewById(R.id.slider2)
        background1 = findViewById(R.id.imgBG1)
        background2 = findViewById(R.id.imgBG2)
        title = findViewById(R.id.tvTitle)
        description = findViewById(R.id.tvDescription)
        sliderChooseAdapter = SliderAdapter(this)
        sliderChoose.apply {
            adapter = sliderChooseAdapter
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 10
            setPreviewBothSide(R.dimen._50dp, R.dimen._50dp)
        }
        loadApps()
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
                    background1.setImageResource(dataChoose[tmpIndex].getBackground())
                    background2.alpha = 1 - positionOffset
                    title.alpha = 1 - positionOffset
                    description.alpha = 1 - positionOffset
                } else {
                    background1.setImageResource(dataChoose[position].getBackground())
                    background2.alpha = positionOffset
                    title.alpha = positionOffset
                    description.alpha = positionOffset
                }
                if (myState == ViewPager2.SCROLL_STATE_SETTLING) {
                    title.alpha = 0f
                    description.alpha = 0f
                    title.text = dataChoose[position].title()
                    description.text = dataChoose[position].description()
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
                background2.setImageResource(dataChoose[position].getBackground())
                when (currentPosition) {
                    DataType.CAMERA.ordinal -> {
                        spinner.visibility = View.VISIBLE
                        spinner.adapter = DropDownListAdapter(this@MainActivity,
                            arrayOf(R.drawable.ic_photo, R.drawable.ic_camera_small)
                            , DataType.CAMERA.getAdditionalOption()
                        )
                        shortCutConfig.saveShortCut(currentPosition, DataType.CAMERA.getAdditionalOption()[0])

                    }
                    DataType.SHORTCUT.ordinal -> {
                        spinner.visibility = View.VISIBLE
                        spinner.adapter = DropDownListAdapter(this@MainActivity,
                            arrayOf(R.drawable.ic_shortcut_active),
                            appListMainArrayList.map { it.appName }.toTypedArray(),
                            appListMainArrayList.map { it.icon }.toTypedArray()
                        )
                        shortCutConfig.saveShortCut(currentPosition, DataType.SHORTCUT.getAdditionalOption()[0])

                    }
                    else -> {
                        spinner.visibility = View.GONE
                        shortCutConfig.saveShortCut(currentPosition, "")
                    }
                }
                askPermissionIfNeed(position)
                createBubble(position)
            }
        })
        dataChoose.addAll(DataType.values())
        if (shortCutConfig.getShortCut()?.additionalOption == "") {
            spinner.adapter = DropDownListAdapter(this@MainActivity,
                arrayOf()
                ,  arrayOf()
            )
            sourceAdditional = arrayOf()
            spinner.visibility = View.GONE
        } else {
            spinner.visibility = View.VISIBLE
            when (shortCutConfig.getShortCut()?.position) {
                DataType.CAMERA.ordinal -> {
                    spinner.adapter = DropDownListAdapter(this@MainActivity,
                        arrayOf(R.drawable.ic_photo, R.drawable.ic_camera_small)
                        , DataType.CAMERA.getAdditionalOption()
                    )
                    sourceAdditional = DataType.CAMERA.getAdditionalOption()
                }
                DataType.SHORTCUT.ordinal -> {
                    spinner.adapter = DropDownListAdapter(this@MainActivity,
                        arrayOf(R.drawable.ic_shortcut_active),
                        appListMainArrayList.map { it.appName }.toTypedArray(),
                        appListMainArrayList.map { it.icon }.toTypedArray()
                    )
                    sourceAdditional = appListMainArrayList.map { it.appName }.toTypedArray()
                }
            }

        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                when (dataChoose[currentPosition].ordinal) {
                    DataType.CAMERA.ordinal -> {
                        shortCutConfig.saveShortCut(currentPosition, sourceAdditional[position])
                    }
                    else -> {
                        shortCutConfig.saveShortCut(currentPosition, "")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        }
        sliderChooseAdapter.addItems(dataChoose, true)
        sliderChoose.setCurrentItem(currentPosition, false)
        sliderChooseAdapter.setCurrentItem(currentPosition)
        background2.setImageResource(dataChoose[currentPosition].getBackground())
        TabLayoutMediator(findViewById(R.id.tabLayout), sliderChoose) { tab, position ->

        }.attach()
    }

    private fun askPermissionDialog(message: String, needUri: Boolean, permission: String) {
        val inflater = this.layoutInflater
        var alertDialog: AlertDialog? = null
        val dialogView: View = inflater.inflate(R.layout.dialog_layout, null)
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        builder.setCancelable(false)
        dialogView.findViewById<TextView>(R.id.message).text = message
        dialogView.findViewById<TextView>(R.id.btnCancel).setOnClickListener {
            alertDialog?.dismiss()
        }
        dialogView.findViewById<TextView>(R.id.btnOk).setOnClickListener {
            val packageName = packageName ?: return@setOnClickListener
            alertDialog?.dismiss()
            val intent = Intent(permission)
            if (needUri) {
                val uri = Uri.parse("package:$packageName")
                intent.data = uri
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            overlayPermissionLauncher.launch(intent)

        }

        alertDialog = builder.create()
        alertDialog?.show()
        alertDialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.9).toInt(),
            (180 * resources.displayMetrics.density).roundToInt()
        )
        alertDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    private fun askPermissionIfNeed(indexBubble: Int) {
        when (indexBubble) {
            DataType.SILENT.ordinal -> {
                val manager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
                if (!manager.isNotificationPolicyAccessGranted) {
                    askPermissionDialog(
                        "App cần quyền không làm phiền", false, android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
                    )
                }
            }
            DataType.NOT_DISTURB.ordinal -> {
            }
            DataType.CAMERA.ordinal -> {
                if (!checkPermission(CAMERA_PERMISSION)) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(CAMERA_PERMISSION),
                        200
                    )
                }

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
        // finishAndRemoveTask()

    }

    fun createBubble(position: Int) {
        askForDrawOverlayPermission()
        if (!Settings.canDrawOverlays(this)) {
            return
        }
        stopMyService()
        val intent = Intent(this, MyServiceKt::class.java)
        intent.putExtra("size", 60)
        intent.putExtra("id", dataChoose[position].bubble())
        intent.putExtra("index", position)
        ContextCompat.startForegroundService(this, intent)
        isVisible = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shortCutConfig = ShortCutConfig(this)
        shortCutConfig.getShortCut()?.position?.let {
            currentPosition = it
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
        askPermissionDialog("App cần quyền vẽ", true, Settings.ACTION_MANAGE_OVERLAY_PERMISSION)

    }

    private fun stopMyService() {
        val intent = Intent(this, MyServiceKt::class.java)
        stopService(intent)
    }

}