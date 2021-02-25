package com.tvstack.tvinput.session;

import android.content.Context;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.view.Surface;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Created by cfp on 2021/2/9.
 */
public class NetTunerSession extends TvInputService.Session {


    private NetTunerSessionWorker mNetTunerSessionWorker;

    /**
     * Creates a new Session.
     *
     * @param context The context of the application
     */
    public NetTunerSession(Context context,
                           NetTunerSessionWorker.Factory netTunerSessionWorkerFactory) {
        super(context);
        mNetTunerSessionWorker = netTunerSessionWorkerFactory.create(context);
    }

    @Override
    public void onRelease() {
        mNetTunerSessionWorker.release();
    }

    @Override
    public boolean onSetSurface(@Nullable Surface surface) {
        mNetTunerSessionWorker.setSurface(surface);
        return true;
    }

    @Override
    public void onSetStreamVolume(float volume) {

    }

    @Override
    public boolean onTune(Uri channelUri) {
        mNetTunerSessionWorker.tune(channelUri);
        notifyVideoAvailable();
        return true;
    }

    @Override
    public void onSetCaptionEnabled(boolean enabled) {

    }

    @Override
    public View onCreateOverlayView() {
        return super.onCreateOverlayView();
    }
}
