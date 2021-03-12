package com.tvstack.tvinput.service;

import com.tvstack.tvinput.factory.NetTunerSessionFactory;
import com.tvstack.tvinput.factory.TunerRecordingSessionFactory;
import com.tvstack.tvinput.factory.TunerSessionFactory;

/**
 * Created by cfp on 2021/2/9.
 */
public class NetTunerTvInputService extends BaseTunerTvInputService{

    @Override
    public TunerSessionFactory getTunerSessionFactory() {
        return new NetTunerSessionFactory();
    }

    @Override
    public TunerRecordingSessionFactory getTunerRecordingSessionFactory() {
        return null;
    }
}
