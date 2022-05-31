package com.vesdkreactnativeintegrationsample

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.facebook.react.bridge.*

class VideoEditorModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val EXPORT_REQUEST_CODE = 1111
        private const val E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST"
        private const val E_VIDEO_EDITOR_CANCELLED = "E_VIDEO_EDITOR_CANCELLED"
        private const val E_EXPORTED_VIDEO_NOT_FOUND = "E_EXPORTED_VIDEO_NOT_FOUND"
    }

    private var exportResultPromise: Promise? = null

    private val videoEditorResultListener = object : ActivityEventListener {
        override fun onActivityResult(
            activity: Activity?,
            requestCode: Int,
            resultCode: Int,
            data: Intent?
        ) {
            if (requestCode == EXPORT_REQUEST_CODE) {
                when {
                    resultCode == Activity.RESULT_OK -> {
                        val exportResult = data?.getParcelableExtra<ExportResult.Success>(
                            EXTRA_EXPORTED_SUCCESS
                        )
                        val exportedVideos = exportResult?.videoList ?: emptyList()
                        val resultUri = exportedVideos.firstOrNull()?.sourceUri
                        resultUri?.let {
                            exportResultPromise?.resolve(it.toString())
                        } ?: exportResultPromise?.reject(
                            E_EXPORTED_VIDEO_NOT_FOUND,
                            "Exported video is null"
                        )
                    }
                    requestCode == Activity.RESULT_CANCELED -> {
                        exportResultPromise?.reject(
                            E_VIDEO_EDITOR_CANCELLED,
                            "Video editor export was cancelled"
                        )
                    }
                }
                exportResultPromise = null
            }
        }

        override fun onNewIntent(intent: Intent?) {}
    }


    init {
        reactApplicationContext.addActivityEventListener(videoEditorResultListener)
    }

    override fun getName(): String = "VideoEditorModule"

    @ReactMethod
    fun openVideoEditor(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity == null) {
            inputPromise.reject(
                E_ACTIVITY_DOES_NOT_EXIST,
                "Host activity to open Video Editor does not exist!"
            )
            return
        } else {
            this.exportResultPromise = inputPromise
            val intent = VideoCreationActivity.startFromCamera(
                hostActivity,
                // set PiP video configuration
                PipConfig(
                    video = Uri.EMPTY,
                    openPipSettings = false
                ),
                // setup data that will be acceptable during export flow
                null,
                // set TrackData object if you open VideoCreationActivity with preselected music track
                null
            )
            hostActivity.startActivityForResult(intent, EXPORT_REQUEST_CODE)
        }

    }
}