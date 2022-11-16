package com.vesdkreactnativeintegrationsample;

import com.facebook.react.ReactActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.banuba.sdk.core.data.TrackData;
import com.banuba.sdk.core.domain.ProvideTrackContract;

/**
 * Custom ReactActivity used for communication between Android and React Native for
 * selecting audio content on customer side.
 * <p>
 * Basically Video Editor SDK requires opening new Android Activity for showing custom audio content
 * that should be used in Video Editor SDK on video recording or post processing stages.
 * <p>
 * This Activity does not expect any native Android UI controls.
 * It contains 2 methods
 * - "applyAudioTrack" passes audio to Video Editor SDK. Call method from "VideoEditorModule" class.
 * - "handleLastUsedAudio" use this method to handle last used audio received from Video Editor SDK.
 *
 */
public class AudioBrowserActivity extends ReactActivity {

    private TrackData lastAudioTrack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(VideoEditorModule.TAG, "AudioBrowserActivity. onCreate");

        handleLastUsedAudio();
    }

    /**
     * Video Editor SDK passes last used audio obtained from Audio Browser so that
     * you can handle this audio in your Audio Browser.
     * For example, highlight last used audio in a list.
     */
    private void handleLastUsedAudio() {
        lastAudioTrack = getIntent().getParcelableExtra("EXTRA_LAST_PROVIDED_TRACK");
        if (lastAudioTrack != null) {
            final String lastAudioPath = lastAudioTrack.getLocalUri().toString();
            // Pass lastAudioPath to React Native side to implement custom logic if it is needed.
            Log.d(VideoEditorModule.TAG, "Last used audio = " + lastAudioPath);
        }
    }

    /**
     * Passes audio track to Video Editor SDK to play on video recording or video
     * post processing stages.
     *
     * @param audioTrack - Video Editor SDK will cancel last used audio if value null.
     *                   Otherwise audio will be applied and played.
     */
    public void applyAudioTrack(final TrackData audioTrack) {
        if (audioTrack == null) {
            // Video Editor SDK will cancel previous used audio.
            setResult(Activity.RESULT_CANCELED, null);
        } else {
            // Video Editor SDK will play this audio.
            final Intent resultIntent = new Intent();
            resultIntent.putExtra(ProvideTrackContract.EXTRA_RESULT_TRACK_DATA, audioTrack);
            setResult(Activity.RESULT_OK, resultIntent);
        }

        finish();
    }

    public void discardAudioTrack() {
        applyAudioTrack(null);
    }

    public void close() {
        applyAudioTrack(lastAudioTrack);
    }

    @Override
    protected String getMainComponentName() {
        return "audio_browser";
    }
}
