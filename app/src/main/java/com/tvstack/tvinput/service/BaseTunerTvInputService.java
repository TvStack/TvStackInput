package com.tvstack.tvinput.service;

import android.content.Context;
import android.media.tv.TvInputService;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tvstack.tvinput.factory.TunerRecordingSessionFactory;
import com.tvstack.tvinput.factory.TunerSessionFactory;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

import javax.inject.Inject;

/**
 * Created by cfp on 2021/2/9.
 */
public abstract class BaseTunerTvInputService extends TvInputService {

    private static final String TAG = "BaseTunerTvInputService";

    private final Set<Session> mTunerSessions = Collections.newSetFromMap(new WeakHashMap<>());

    @Nullable
    @Override
    public Session onCreateSession(@NonNull String inputId) {
        Session session = getTunerSessionFactory().create(this);
        mTunerSessions.add(session);
        return session;
    }

    @Override
    public void onCreate() {
        if(getApplicationContext().getSystemService(Context.TV_INPUT_SERVICE) == null){
            Log.w(TAG, "Stopping because device dose not support tv");
            stopSelf();
            return;
        }
        super.onCreate();
    }

    @Nullable
    @Override
    public RecordingSession onCreateRecordingSession(@NonNull String inputId, @NonNull String sessionId) {

        RecordingSession recordingSession = getTunerRecordingSessionFactory()
                .createRecordingSession(this);
        return recordingSession;
    }

    /**
     * return TunerSessionFactory
     * @return
     */
    public abstract TunerSessionFactory getTunerSessionFactory();


    /**
     * return recording session factory
     * @return
     */
    public abstract TunerRecordingSessionFactory getTunerRecordingSessionFactory();
}
