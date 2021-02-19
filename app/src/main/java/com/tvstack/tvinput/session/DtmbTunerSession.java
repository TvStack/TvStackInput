package com.tvstack.tvinput.session;

import android.content.Context;
import android.media.tv.TvInputService;
import android.net.Uri;
import android.view.Surface;

import androidx.annotation.Nullable;

/**
 * Created by cfp on 2021/2/9.
 */
public class DtmbTunerSession extends TvInputService.Session {

    /**
     * Creates a new Session.
     *
     * @param context The context of the application
     */
    public DtmbTunerSession(Context context) {
        super(context);
    }

    @Override
    public void onRelease() {

    }

    @Override
    public boolean onSetSurface(@Nullable Surface surface) {
        return false;
    }

    @Override
    public void onSetStreamVolume(float volume) {

    }

    @Override
    public boolean onTune(Uri channelUri) {
        return false;
    }

    @Override
    public void onSetCaptionEnabled(boolean enabled) {

    }
}
