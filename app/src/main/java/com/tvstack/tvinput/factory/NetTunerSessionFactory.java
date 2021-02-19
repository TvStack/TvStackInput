package com.tvstack.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService;

import com.tvstack.tvinput.session.NetTunerSession;
import com.tvstack.tvinput.session.NetTunerSessionWorker;

/**
 * Created by cfp on 2021/2/9.
 */
public class NetTunerSessionFactory implements TunerSessionFactory{


    @Override
    public TvInputService.Session create(Context context) {
        return new NetTunerSession(context, new NetTunerSessionWorker.Factory());
    }
}
