package com.tvstack.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService;

import com.tvstack.tvinput.session.DtmbTunerSession;

/**
 * Created by cfp on 2021/2/9.
 */
public class DtmbTunerSessionFactory implements TunerSessionFactory{


    @Override
    public TvInputService.Session create(Context context) {
        return new DtmbTunerSession(context);
    }
}
