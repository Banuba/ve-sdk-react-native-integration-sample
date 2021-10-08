package com.vesdkreactnativeintegrationsample.videoeditor.di

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import com.banuba.sdk.cameraui.data.CameraTimerStateProvider
import com.banuba.sdk.ve.effects.WatermarkProvider
import com.banuba.sdk.ve.flow.FlowEditorModule
import com.banuba.sdk.veui.data.ExportParamsProvider
import com.vesdkreactnativeintegrationsample.videoeditor.export.IntegrationAppExportParamsProvider
import com.vesdkreactnativeintegrationsample.videoeditor.impl.IntegrationAppWatermarkProvider
import com.vesdkreactnativeintegrationsample.videoeditor.impl.IntegrationTimerStateProvider
import org.koin.core.definition.BeanDefinition
import org.koin.core.qualifier.named

/**
 * All dependencies mentioned in this module will override default
 * implementations provided from SDK.
 * Some dependencies has no default implementations. It means that
 * these classes fully depends on your requirements
 */
class VideoEditorKoinModule : FlowEditorModule() {

    /**
     * Provides params for export
     * */
    override val exportParamsProvider: BeanDefinition<ExportParamsProvider> =
        factory(override = true) {
            IntegrationAppExportParamsProvider(
                exportDir = get(named("exportDir")),
                sizeProvider = get(),
                watermarkBuilder = get()
            )
        }

    /**
     * Provides path for exported files
     * */
    override val exportDir: BeanDefinition<Uri> = single(named("exportDir"), override = true) {
        get<Context>().getExternalFilesDir("")
            ?.toUri()
            ?.buildUpon()
            ?.appendPath("export")
            ?.build() ?: throw NullPointerException("exportDir should't be null!")
    }

    override val watermarkProvider: BeanDefinition<WatermarkProvider> = factory(override = true) {
        IntegrationAppWatermarkProvider()
    }

    override val cameraTimerStateProvider: BeanDefinition<CameraTimerStateProvider> =
            factory(override = true) {
                IntegrationTimerStateProvider()
            }
}