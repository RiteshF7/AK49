package com.trex.rexnetwork.domain.firebasecore.fcm.fcmrequestscreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class PermissionsExtraData(
    private val permissionsList: Array<String>,
) : Parcelable

class PermissionHandlerActivity : ComponentActivity() {
    companion object {
        private const val PERMISSION_REQUEST_CODE = 123
        const val PERMISSION_RESULT_REQUEST_CODE = 10
        private const val KEY_PERMISSION_EXTRA_DATA = "PERMISSIONS_EXTRA_DATA"
        private val RUNTIME_PERMISSIONS =
            arrayOf(
                // Location permissions
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.CHANGE_WIFI_STATE,
                // Storage permissions
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                // Phone permissions
                Manifest.permission.READ_PHONE_STATE,
                // SMS permissions
                Manifest.permission.SEND_SMS,
                Manifest.permission.RECEIVE_SMS,
                // Contacts permissions
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                // Notifications (Android 13+)
                Manifest.permission.POST_NOTIFICATIONS,
            )

        fun go(context: Activity) {
            val intent = Intent(context, PermissionHandlerActivity::class.java)
            intent.putExtra(
                KEY_PERMISSION_EXTRA_DATA,
                RUNTIME_PERMISSIONS,
            )
            context.startActivityForResult(intent, PERMISSION_RESULT_REQUEST_CODE)
        }
    }

    private lateinit var requiredPermissions: Array<String>
    private val deniedPermissions = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get permissions from intent
        requiredPermissions = intent
            .getStringArrayExtra(KEY_PERMISSION_EXTRA_DATA)
            ?.filterNotNull()
            ?.toTypedArray()
            ?: arrayOf()

        if (requiredPermissions.isEmpty()) {
            Toast.makeText(this, "No permissions requested", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        deniedPermissions.clear()

        // Check which permissions are not granted
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED
            ) {
                deniedPermissions.add(permission)
            }
        }

        if (deniedPermissions.isEmpty()) {
            // All permissions are granted
            setResult(RESULT_OK)
            finish()
        } else {
            // Request permissions
            ActivityCompat.requestPermissions(
                this,
                deniedPermissions.toTypedArray(),
                PERMISSION_REQUEST_CODE,
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            var allGranted = true

            for (i in permissions.indices) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false
                    break
                }
            }

            if (allGranted) {
                // All permissions granted
                setResult(RESULT_OK)
                finish()
            } else {
                // Some permissions still denied, check again
                checkAndRequestPermissions()
            }
        }
    }
}
