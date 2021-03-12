package com.tvstack.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService;

/**
 * recording session factory
 */
public interface TunerRecordingSessionFactory {

    /**
     * create recording session
     * @param context
     * @return
     */
    TvInputService.RecordingSession createRecordingSession(Context context);
}
