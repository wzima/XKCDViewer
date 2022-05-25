package com.zima.myxkcdviewer.data.logic.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment

class PermissionsHelper(val fragment: Fragment, val permissions: Array<String>, val startAction: () -> Unit) {
    private val permReqLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value == true
            }

            if (granted) {
                startAction()
            }
        }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean = permissions.all {
        ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun start() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            fragment.activity?.let {
                if (hasPermissions(fragment.activity as Context, permissions)) {
                    startAction()
                } else {
                    permReqLauncher.launch(
                        permissions
                    )
                }
            }
        } else
            startAction()
    }

}