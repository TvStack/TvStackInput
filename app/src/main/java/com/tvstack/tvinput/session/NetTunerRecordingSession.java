package com.tvstack.tvinput.session;

import android.content.Context;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.N)
public class NetTunerRecordingSession extends TvInputService.RecordingSession {

    private NetTunerRecordingSessionWorker mSessionWorker;


    /**
     * Creates a new RecordingSession.
     *
     * @param context The context of the application
     */
    public NetTunerRecordingSession(Context context) {
        super(context);
        mSessionWorker = NetTunerRecordingSessionWorker.Factory.create(context);
    }

    @Override
    public void onTune(Uri channelUri) {
        mSessionWorker.tune(channelUri);
    }

    @Override
    public void onStartRecording(@Nullable Uri programUri) {
        mSessionWorker.startRecording(programUri);
    }

    @Override
    public void onStopRecording() {
        mSessionWorker.onStopRecording();
    }

    @Override
    public void onRelease() {

    }
}
