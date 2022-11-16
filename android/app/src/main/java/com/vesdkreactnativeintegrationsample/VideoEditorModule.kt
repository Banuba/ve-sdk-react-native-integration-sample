package com.vesdkreactnativeintegrationsample

import android.app.Activity
import android.content.Intent
import android.content.res.AssetManager
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.banuba.sdk.cameraui.data.PipConfig
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.export.data.ExportResult
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.facebook.react.bridge.*
import java.io.*
import java.util.*

class VideoEditorModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val EXPORT_REQUEST_CODE = 1111
        private const val E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST"
        private const val E_VIDEO_EDITOR_CANCELLED = "E_VIDEO_EDITOR_CANCELLED"
        private const val E_EXPORTED_VIDEO_NOT_FOUND = "E_EXPORTED_VIDEO_NOT_FOUND"

        const val TAG = "VideoEditorModule"
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

    override fun getName(): String = TAG

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

    @ReactMethod
    fun openVideoEditorPIP(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity == null) {
            inputPromise.reject(
                E_ACTIVITY_DOES_NOT_EXIST,
                "Host activity to open Video Editor does not exist!"
            )
            return
        } else {
            // sample_pip_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
            // Please provide valid video URL to open Video Editor in PIP.
            val sampleVideoFileName = "sample_pip_video.mp4"
            val filesStorage: File = hostActivity.applicationContext.filesDir
            val assets: AssetManager = hostActivity.applicationContext.assets
            val sampleVideoFile = prepareFile(assets, filesStorage, sampleVideoFileName)

            this.exportResultPromise = inputPromise
            val intent = VideoCreationActivity.startFromCamera(
                hostActivity,
                // set PiP video configuration
                PipConfig(
                    video = sampleVideoFile.toUri(),
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

    /**
     * Applies selected audio on custom Audio Browser in Video Editor SDK.
     *
     * This implementation demonstrates how to play audio stored in Android assets in Video Editor SDK.
     *
     * Since audio browsing and downloading logic can be implemented using React Native on JS side
     * you can pass specific audio params in this method to build TrackData
     * and use it in "AudioBrowserActivity.applyAudioTrack".
     */
    @ReactMethod
    fun applyAudioTrack(inputPromise: Promise) {
        val hostActivity = currentActivity

        // Check if host Activity is a your specific Android Activity responsible for
        // passing audio to Video Editor SDK i.e. AudioBrowserActivity.
        if (hostActivity is AudioBrowserActivity) {
            // Sample audio file used to demonstrate how to pass and play audio file
            // in Video Editor SDK
            val sampleAudioFileName = "sample_audio.mp3"
            val filesStorage: File = hostActivity.applicationContext.filesDir
            val assets: AssetManager = hostActivity.applicationContext.assets

            val audioTrack: TrackData? = try {
                // Video Editor SDK can play ONLY audio file stored on device.
                // Make sure that you store audio file on a device before trying to play it.
                val sampleAudioFile = prepareFile(assets, filesStorage, sampleAudioFileName)

                // TrackData is required in Video Editor SDK for playing audio.
                TrackData(
                    UUID.randomUUID(),
                    "Set title",
                    Uri.fromFile(sampleAudioFile),
                    "Set artist"
                )
            } catch (e: IOException) {
                Log.w(TAG, "Cannot prepare sample audio file", e)
                // You can pass null as TrackData to cancel playing last used audio in Video Editor SDK
                null
            }

            Log.d(TAG, "Apply audio track = $audioTrack")
            hostActivity.applyAudioTrack(audioTrack)
        }
    }

    @ReactMethod
    fun discardAudioTrack(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity is AudioBrowserActivity) {
            hostActivity.discardAudioTrack()
            inputPromise.resolve(null)
        } else {
            inputPromise.reject(IllegalStateException("Invalid host Activity"))
        }
    }

    @ReactMethod
    fun closeAudioBrowser(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity is AudioBrowserActivity) {
            hostActivity.close()
            inputPromise.resolve(null)
        } else {
            inputPromise.reject(IllegalStateException("Invalid host Activity"))
        }
    }

    /**
     * Utils methods used to prepare sample video to open Video Editor SDK in PIP.
     * NOT REQUIRED IN YOUR APP.
     */
    @Throws(IOException::class)
    private fun prepareFile(
        assets: AssetManager,
        filesStorage: File,
        audioFileName: String
    ): File {
        val sampleAudioFile = File(filesStorage, audioFileName)

        var outputStream: OutputStream? = null
        try {
            assets.open(audioFileName).use { inputStream ->
                outputStream = FileOutputStream(sampleAudioFile)
                val size = copyStream(inputStream, requireNotNull(outputStream))
                Log.d(TAG, "File has been copied. Size = $size")
            }
        } catch (e: IOException) {
            throw e
        } finally {
            outputStream?.flush()
            outputStream?.close()
        }

        return sampleAudioFile
    }

    @Throws(IOException::class)
    private fun copyStream(
        inStream: InputStream,
        outStream: OutputStream
    ): Long {
        var size = 0L
        val buffer = ByteArray(10 * 1024)
        var bytes = inStream.read(buffer)
        while (bytes >= 0) {
            outStream.write(buffer, 0, bytes)
            size += bytes.toLong()
            bytes = inStream.read(buffer)
        }
        return size
    }
}