package com.vesdkreactnativeintegrationsample

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.net.toFile
import androidx.fragment.app.Fragment
import com.banuba.android.sdk.ve.timeline.`object`.data.ObjectEditorConfig
import com.banuba.sdk.arcloud.data.source.ArEffectsRepositoryProvider
import com.banuba.sdk.arcloud.di.ArCloudKoinModule
import com.banuba.sdk.audiobrowser.di.AudioBrowserKoinModule
import com.banuba.sdk.audiobrowser.domain.AudioBrowserMusicProvider
import com.banuba.sdk.cameraui.data.CameraTimerActionProvider
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.cameraui.data.TimerEntry
import com.banuba.sdk.cameraui.domain.HandsFreeTimerActionProvider
import com.banuba.sdk.core.AspectRatio
import com.banuba.sdk.core.VideoResolution
import com.banuba.sdk.core.data.OrderProvider
import com.banuba.sdk.core.data.TrackData
import com.banuba.sdk.core.domain.AspectRatioProvider
import com.banuba.sdk.core.domain.BackgroundSeparationActionDataProvider
import com.banuba.sdk.core.domain.DraftConfig
import com.banuba.sdk.core.domain.ScannerActionDataProvider
import com.banuba.sdk.core.ext.toPx
import com.banuba.sdk.core.media.MediaFileNameHelper
import com.banuba.sdk.core.ui.ContentFeatureProvider
import com.banuba.sdk.effectplayer.adapter.BanubaEffectPlayerKoinModule
import com.banuba.sdk.export.data.*
import com.banuba.sdk.export.di.VeExportKoinModule
import com.banuba.sdk.export.utils.EXTRA_EXPORTED_SUCCESS
import com.banuba.sdk.gallery.di.GalleryKoinModule
import com.banuba.sdk.playback.di.VePlaybackSdkKoinModule
import com.banuba.sdk.token.storage.di.TokenStorageKoinModule
import com.banuba.sdk.ve.di.VeSdkKoinModule
import com.banuba.sdk.ve.domain.VideoRangeList
import com.banuba.sdk.ve.effects.Effects
import com.banuba.sdk.ve.effects.music.MusicEffect
import com.banuba.sdk.ve.effects.watermark.WatermarkAlignment
import com.banuba.sdk.ve.effects.watermark.WatermarkBuilder
import com.banuba.sdk.ve.effects.watermark.WatermarkProvider
import com.banuba.sdk.ve.ext.withWatermark
import com.banuba.sdk.ve.flow.di.VeFlowKoinModule
import com.banuba.sdk.veui.data.EditorConfig
import com.banuba.sdk.veui.di.VeUiSdkKoinModule
import com.banuba.sdk.veui.domain.CoverProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module

class BanubaVideoEditorSDK {

    fun initialize(application: Application) {
        startKoin {
            androidContext(application)
            allowOverride(true)

            // IMPORTANT! order of modules is required
            modules(
                VeSdkKoinModule().module,
                VeExportKoinModule().module,
                VePlaybackSdkKoinModule().module,

                // Use AudioBrowserKoinModule ONLY if your contract includes this feature.
                AudioBrowserKoinModule().module,

                // IMPORTANT! ArCloudKoinModule should be set before TokenStorageKoinModule to get effects from the cloud
                ArCloudKoinModule().module,

                TokenStorageKoinModule().module,
                VeUiSdkKoinModule().module,
                VeFlowKoinModule().module,
                BanubaEffectPlayerKoinModule().module,
                GalleryKoinModule().module,

                // Sample integration module
                SampleIntegrationVeKoinModule().module,
            )
        }
    }
}

/**
 * This class represents parsing result from VideoCreationActivity
 *
 * ExportResult is an object that holds all necessary data related
 * to the video created in VideoCreationActivity
 */

class SampleExportVideoContract: ActivityResultContract<Intent?, ExportResult?>() {

    override fun createIntent(context: Context, input: Intent?): Intent {
        check(input != null) {
            "Can not create Intent to create video"
        }
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ExportResult? {
        if (resultCode == Activity.RESULT_OK) {
            return intent?.getParcelableExtra(EXTRA_EXPORTED_SUCCESS) as? ExportResult.Success
        }
        return ExportResult.Inactive
    }

}

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
private class SampleIntegrationVeKoinModule {

    val module = module {
        single<ExportFlowManager> {
            ForegroundExportFlowManager(
                exportDataProvider = get(),
                sessionParamsProvider = get(),
                exportSessionHelper = get(),
                exportDir = get(named("exportDir")),
                shouldClearSessionOnFinish = true,
                publishManager = get(),
                errorParser = get(),
                mediaFileNameHelper = get(),
                exportBundleProvider = get()
            )
        }

        /**
         * Provides params for export
         * */
        factory<ExportParamsProvider> {
            SampleExportParamsProvider(
                exportDir = get(named("exportDir")),
                watermarkBuilder = get()
            )
        }

        factory<WatermarkProvider> {
            SampleWatermarkProvider()
        }

        factory<CameraTimerStateProvider> {
            SampleTimerStateProvider()
        }

        single<ArEffectsRepositoryProvider>(createdAtStart = true) {
            ArEffectsRepositoryProvider(
                arEffectsRepository = get(named("backendArEffectsRepository")),
                ioDispatcher = get(named("ioDispatcher"))
            )
        }

        single<ContentFeatureProvider<TrackData, Fragment>>(
            named("musicTrackProvider")
        ) {
            AudioBrowserMusicProvider()
        }

        single<CoverProvider> {
            CoverProvider.EXTENDED
        }

        single<CameraTimerActionProvider> {
            HandsFreeTimerActionProvider()
        }

        single<OrderProvider>(named("colorFilterOrderProvider")) {
            SampleColorFilterOrderProvider()
        }

        single<OrderProvider>(named("maskOrderProvider")) {
            SampleMaskOrderProvider()
        }

        factory<DraftConfig> {
            DraftConfig.ENABLED_ASK_TO_SAVE
        }

        single<AspectRatioProvider> {
            object : AspectRatioProvider {
                override fun provide(): AspectRatio = AspectRatio(9.0 / 16)
            }
        }

        single<EditorConfig> {
            EditorConfig(
                minTotalVideoDurationMs = 1500
            )
        }

        single<ObjectEditorConfig> {
            ObjectEditorConfig(
                objectEffectDefaultDuration = 2000
            )
        }
    }
}

private class SampleExportParamsProvider(
    private val exportDir: Uri,
    private val watermarkBuilder: WatermarkBuilder
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoRangeList: VideoRangeList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float
    ): List<ExportParams> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val extraSoundtrackUri = Uri.parse(exportSessionDir.toString()).buildUpon()
            .appendPath("exported_soundtrack.${MediaFileNameHelper.DEFAULT_SOUND_FORMAT}")
            .build()

        return listOf(
            // Will export video in HD with watermark
            ExportParams.Builder(VideoResolution.Exact.HD)
                .effects(
                    effects.withWatermark(
                        watermarkBuilder,
                        WatermarkAlignment.BottomRight(marginRightPx = 16.toPx)
                    )
                )
                .fileName("export_default_watermark")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
                .build()
            // You can export more video files by creating ExportParams instances
        )
    }
}

// Override list of timer options
private class SampleTimerStateProvider : CameraTimerStateProvider {

    override val timerStates = listOf(
        TimerEntry(
            durationMs = 0
        ),
        TimerEntry(
            durationMs = 3000
        )
    )
}

// Override watermark
private class SampleWatermarkProvider : WatermarkProvider {
    //Provide your own watermark image
    override fun getWatermarkBitmap(): Bitmap? {
        return null
    }
}

class SampleMaskOrderProvider : OrderProvider {
    override fun provide(): List<String> =
        listOf(
            ScannerActionDataProvider.EFFECT_NAME,
            BackgroundSeparationActionDataProvider.EFFECT_NAME
        )
}

class SampleColorFilterOrderProvider : OrderProvider {

    override fun provide() = listOf(
        "egypt",
        "byers",
        "chile",
        "hyla",
        "new_zeland",
        "korben",
        "canada",
        "remy",
        "england",
        "retro",
        "norway",
        "neon",
        "japan",
        "instant",
        "lux",
        "sunset",
        "bubblegum",
        "chroma",
        "lilac",
        "pinkvine",
        "spark",
        "sunny",
        "vinyl",
        "glitch",
        "grunge"
    )
}