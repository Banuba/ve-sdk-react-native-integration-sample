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
import androidx.core.content.FileProvider
import com.banuba.sdk.token.storage.license.LicenseStateCallback
import com.banuba.sdk.token.storage.license.BanubaVideoEditor

class VideoEditorModule(reactContext: ReactApplicationContext) :
    ReactContextBaseJavaModule(reactContext) {

    companion object {
        const val TAG = "VideoEditorModule"
        private const val EXPORT_REQUEST_CODE = 1111

        private const val ERR_ACTIVITY_DOES_NOT_EXIST = "ERR_ACTIVITY_DOES_NOT_EXIST"
        private const val ERR_VIDEO_EDITOR_CANCELLED = "ERR_VIDEO_EDITOR_CANCELLED"
        private const val ERR_EXPORTED_VIDEO_NOT_FOUND = "ERR_EXPORTED_VIDEO_NOT_FOUND"

        private const val ERR_SDK_NOT_INITIALIZED_CODE = "ERR_VIDEO_EDITOR_NOT_INITIALIZED"
        private const val ERR_LICENSE_REVOKED_CODE = "ERR_VIDEO_EDITOR_LICENSE_REVOKED"
        private const val ERR_SDK_NOT_INITIALIZED_MESSAGE
                = "Banuba Video Editor SDK is not initialized: license token is unknown or incorrect.\nPlease check your license token or contact Banuba"
        private const val ERR_LICENSE_REVOKED_MESSAGE = "License is revoked or expired. Please contact Banuba https://www.banuba.com/faq/kb-tickets/new";
    }

    private var exportResultPromise: Promise? = null
    private var videoEditorSDK: BanubaVideoEditor? = null
    private var videoEditorSdkDependencies: VideoEditorIntegrationHelper? = null

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

                        if (resultUri == null) {
                            exportResultPromise?.reject(
                                ERR_EXPORTED_VIDEO_NOT_FOUND,
                                "Exported video is null"
                            )
                        } else {
                            exportResultPromise?.resolve(resultUri.toString())
                            /*
                                NOT REQUIRED FOR INTEGRATION
                                Added for playing exported video file.
                            */
                            activity?.let { demoPlayExportedVideo(it, resultUri) }
                        }
                    }
                    requestCode == Activity.RESULT_CANCELED -> {
                        exportResultPromise?.reject(
                            ERR_VIDEO_EDITOR_CANCELLED,
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
    fun initVideoEditor(licenseToken: String, inputPromise: Promise) {
        videoEditorSDK = BanubaVideoEditor.initialize(licenseToken)

        if (videoEditorSDK == null) {
            // Token you provided is not correct - empty or truncated
            Log.e(TAG, ERR_SDK_NOT_INITIALIZED_MESSAGE)
            inputPromise.reject(ERR_SDK_NOT_INITIALIZED_CODE, ERR_SDK_NOT_INITIALIZED_MESSAGE)
        } else {
            if (videoEditorSdkDependencies == null) {
                // Initialize video editor sdk dependencies
                videoEditorSdkDependencies = VideoEditorIntegrationHelper().apply {
                    initializeDependencies(reactApplicationContext.applicationContext)
                }
            }
            inputPromise.resolve(null)
        }
    }

    @ReactMethod
    fun openVideoEditor(inputPromise: Promise) {
        checkVideoEditorLicense(
                licenseStateCallback = { isValid ->
                    if (isValid) {
                        // ✅ License is active, all good
                        // You can show button that opens Video Editor or
                        // Start Video Editor right away
                        openVideEditorInternal(inputPromise)
                    } else {
                        // ❌ Use of Video Editor is restricted. License is revoked or expired.
                        inputPromise.reject(ERR_LICENSE_REVOKED_CODE, ERR_LICENSE_REVOKED_MESSAGE)
                    }
                },
                notInitializedError = {
                    inputPromise.reject(ERR_SDK_NOT_INITIALIZED_CODE, ERR_SDK_NOT_INITIALIZED_MESSAGE)
                }
        )
    }

    private fun openVideEditorInternal(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity == null) {
            inputPromise.reject(
                    ERR_ACTIVITY_DOES_NOT_EXIST,
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
        checkVideoEditorLicense(
                licenseStateCallback = { isValid ->
                    if (isValid) {
                        // ✅ License is active, all good
                        // You can show button that opens Video Editor or
                        // Start Video Editor right away
                        openVideoEditorPIPInternal(inputPromise)
                    } else {
                        // ❌ Use of Video Editor is restricted. License is revoked or expired.
                        inputPromise.reject(ERR_LICENSE_REVOKED_CODE, ERR_LICENSE_REVOKED_MESSAGE)
                    }
                },
                notInitializedError = {
                    inputPromise.reject(ERR_SDK_NOT_INITIALIZED_CODE, ERR_SDK_NOT_INITIALIZED_MESSAGE)
                }
        )
    }

    private fun openVideoEditorPIPInternal(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity == null) {
            inputPromise.reject(
                    ERR_ACTIVITY_DOES_NOT_EXIST,
                    "Host activity to open Video Editor does not exist!"
            )
            return
        } else {
            // sample_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
            // Please provide valid video URL to open Video Editor in PIP.
            val sampleVideoFileName = "sample_video.mp4"
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

    @ReactMethod
    fun openVideoEditorTrimmer(inputPromise: Promise) {
        checkVideoEditorLicense(
                licenseStateCallback = { isValid ->
                    if (isValid) {
                        // ✅ License is active, all good
                        // You can show button that opens Video Editor or
                        // Start Video Editor right away
                        openVideoEditorTrimmerInternal(inputPromise)
                    } else {
                        // ❌ Use of Video Editor is restricted. License is revoked or expired.
                        inputPromise.reject(ERR_LICENSE_REVOKED_CODE, ERR_LICENSE_REVOKED_MESSAGE)
                    }
                },
                notInitializedError = {
                    inputPromise.reject(ERR_SDK_NOT_INITIALIZED_CODE, ERR_SDK_NOT_INITIALIZED_MESSAGE)
                }
        )
    }

    private fun openVideoEditorTrimmerInternal(inputPromise: Promise) {
        val hostActivity = currentActivity
        if (hostActivity == null) {
            inputPromise.reject(
                    ERR_ACTIVITY_DOES_NOT_EXIST,
                    "Host activity to open Video Editor does not exist!"
            )
            return
        } else {
            // sample_video.mp4 file is hardcoded for demonstrating how to open video editor sdk in the simplest case.
            // Please provide valid video URL to open Video Editor in trimmer.
            val sampleVideoFileName = "sample_video.mp4"
            val filesStorage: File = hostActivity.applicationContext.filesDir
            val assets: AssetManager = hostActivity.applicationContext.assets
            val sampleVideoFile = prepareFile(assets, filesStorage, sampleVideoFileName)

            this.exportResultPromise = inputPromise
            val intent = VideoCreationActivity.startFromTrimmer(
                    hostActivity,
                    // set trimmer video configuration
                    arrayOf(sampleVideoFile.toUri()),
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

    /*
    NOT REQUIRED FOR INTEGRATION
    Added for playing exported video file.
    */
    private fun demoPlayExportedVideo(
        activity: Activity,
        videoUri: Uri
    ) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val uri = FileProvider.getUriForFile(
                activity.applicationContext,
                "${activity.packageName}.provider",
                File(videoUri.encodedPath)
            )
            setDataAndType(uri, "video/mp4")
        }
        activity.startActivity(intent)
    }

    private fun checkVideoEditorLicense(
            licenseStateCallback: LicenseStateCallback,
            notInitializedError: () -> Unit
    ) {
        if (videoEditorSDK == null) {
            Log.e(
                    "BanubaVideoEditor",
                    "Cannot check license state. Please initialize Video Editor SDK"
            )
            notInitializedError()
        } else {
            // Checking the license might take around 1 sec in the worst case.
            // Please optimize use if this method in your application for the best user experience
            videoEditorSDK?.getLicenseState(licenseStateCallback)
        }
    }
}