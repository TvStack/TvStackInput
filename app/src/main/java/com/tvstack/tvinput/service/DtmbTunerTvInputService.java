package com.tvstack.tvinput.service;

import com.tvstack.tvinput.factory.DtmbTunerSessionFactory;
import com.tvstack.tvinput.factory.TunerSessionFactory;

/**
 * Created by cfp on 2021/2/9.
 */
public class DtmbTunerTvInputService extends BaseTunerTvInputService{

    @Override
    public TunerSessionFactory getTunerSessionFactory() {
        return new DtmbTunerSessionFactory();
    }
}
