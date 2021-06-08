package com.vesdkreactnativeintegrationsample

import com.banuba.sdk.ve.flow.VideoCreationActivity
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod

class ActivityStarterModule(reactContext: ReactApplicationContext)
    : ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String = "ActivityStarter"

    @ReactMethod
    fun navigateToVideoEditor() {
        val intent = VideoCreationActivity.buildIntent(
                reactApplicationContext,
                // setup data that will be acceptable during export flow
                null,
                // set TrackData object if you open VideoCreationActivity with preselected music track
                null
        )
        reactApplicationContext.startActivityForResult(intent, 1, null);
    }
}