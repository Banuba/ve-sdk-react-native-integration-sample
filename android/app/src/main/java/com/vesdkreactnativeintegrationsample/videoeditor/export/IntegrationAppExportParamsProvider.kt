package com.vesdkreactnativeintegrationsample.videoeditor.export

import android.net.Uri
import androidx.core.net.toFile
import com.banuba.sdk.core.MediaResolutionProvider
import com.banuba.sdk.core.media.MediaFileNameHelper.Companion.DEFAULT_SOUND_FORMAT
import com.banuba.sdk.export.data.ExportParamsProvider
import com.banuba.sdk.export.data.ExportVideoResolutionProvider
import com.banuba.sdk.ve.domain.VideoRangeList
import com.banuba.sdk.ve.effects.Effects
import com.banuba.sdk.ve.effects.WatermarkAlignment
import com.banuba.sdk.ve.effects.WatermarkBuilder
import com.banuba.sdk.ve.ext.withWatermark
import com.banuba.sdk.ve.player.MusicEffect
import com.banuba.sdk.ve.processing.ExportManager

class IntegrationAppExportParamsProvider(
    private val exportDir: Uri,
    private val sizeProvider: ExportVideoResolutionProvider,
    private val watermarkBuilder: WatermarkBuilder
) : ExportParamsProvider {

    override fun provideExportParams(
        effects: Effects,
        videoRangeList: VideoRangeList,
        musicEffects: List<MusicEffect>,
        videoVolume: Float
    ): List<ExportManager.Params> {
        val exportSessionDir = exportDir.toFile().apply {
            deleteRecursively()
            mkdirs()
        }
        val extraSoundtrackUri = Uri.parse(exportSessionDir.toString()).buildUpon()
            .appendPath("exported_soundtrack.$DEFAULT_SOUND_FORMAT")
            .build()

        return listOf(
            ExportManager.Params.Builder(sizeProvider.videoResolution)
                .effects(effects.withWatermark(watermarkBuilder, WatermarkAlignment.BOTTOM_RIGHT))
                .fileName("export_default_watermark")
                .videoRangeList(videoRangeList)
                .destDir(exportSessionDir)
                .musicEffects(musicEffects)
                .extraAudioFile(extraSoundtrackUri)
                .volumeVideo(videoVolume)
                .build()
        )
    }
}