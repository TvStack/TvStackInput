package com.tvstack.tvinput.service;

import android.media.tv.TvInputService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tvstack.tvinput.factory.TunerSessionFactory;

import javax.inject.Inject;

/**
 * Created by cfp on 2021/2/9.
 */
public abstract class BaseTunerTvInputService extends TvInputService {


    @Nullable
    @Override
    public Session onCreateSession(@NonNull String inputId) {
        Session session = getTunerSessionFactory().create(this);
        return session;
    }

    /**
     * return TunerSessionFactory
     * @return
     */
    public abstract TunerSessionFactory getTunerSessionFactory();
}
