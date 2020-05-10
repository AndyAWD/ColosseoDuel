package tw.com.andyawd.colosseoduel

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.jakewharton.rxbinding3.view.clicks
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.EasyPermissions
import tw.com.andyawd.andyawdlibrary.AWDLog
import tw.com.andyawd.andyawdlibrary.AWDPermissionsFailAlertDialog
import tw.com.andyawd.andyawdlibrary.AWDToastMgr
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    private val mainMenuFragment = MainMenuFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setWindowBackground()

        initComponent()
        initListener()
    }

    private fun setWindowBackground() {
        window.decorView.background = ActivityCompat.getDrawable(this, R.color.colorAccent)
    }

    private fun initComponent() {
        supportFragmentManager.inTransaction {
            replace(R.id.clAmGroup, mainMenuFragment)
        }
    }

    @SuppressLint("CheckResult")
    private fun initListener() {
        ibAmStartRecord.clicks().throttleFirst(BaseConstants.CLICK_TIMER, TimeUnit.MILLISECONDS)
            .subscribe {
                createVideoRecord()
            }

        ibAmEndRecord.clicks().throttleFirst(BaseConstants.CLICK_TIMER, TimeUnit.MILLISECONDS)
            .subscribe {
                crglsvAmVideo.endRecording()
            }

        mainMenuFragment.fragmentOpenListener = fragmentOpenListener
    }

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
        beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).func()
            .commitAllowingStateLoss()
    }

    private fun createVideoRecord() {
        val videoRecordPermission = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        if (!EasyPermissions.hasPermissions(this, *videoRecordPermission)) {
            EasyPermissions.requestPermissions(
                this,
                resources.getString(R.string.no_video_record_permission),
                BaseConstants.VIDEO_RECORD_PERMISSION_RESULT,
                *videoRecordPermission
            )
            return
        }

        crglsvAmVideo.presetCameraForward(true)
        crglsvAmVideo.setFitFullView(true)
        crglsvAmVideo.startRecording(
            FileManager.instance.getUriFromFile(
                BaseApplication.context(),
                Environment.DIRECTORY_MOVIES,
                "1090504",
                FileManager.MP4
            ).toString()
        )
    }

    private val fragmentOpenListener = object : FragmentOpenListener() {
        override fun onOpenVideoRecordView() {
            super.onOpenVideoRecordView()

            AWDLog.d("onOpenVideoRecordView")
        }

        override fun onOpenVideoListView() {
            super.onOpenVideoListView()

            AWDLog.d("onOpenVideoListView")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AWDPermissionsFailAlertDialog(this, perms)
        } else {
            AWDToastMgr.init(this).build()
                .show(resources.getString(R.string.open_video_record_permission))
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        AWDLog.d("onPermissionsGranted requestCode: $requestCode")
        when (requestCode) {
            BaseConstants.VIDEO_RECORD_PERMISSION_RESULT -> {
                createVideoRecord()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AWDLog.d("onActivityResult: $requestCode / resultCode: $resultCode")
        when (requestCode) {
            BaseConstants.VIDEO_RECORD_PERMISSION_RESULT -> {
                createVideoRecord()
            }
        }
    }
}
