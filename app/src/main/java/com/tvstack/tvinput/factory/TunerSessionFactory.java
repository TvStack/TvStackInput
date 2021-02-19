package com.tvstack.tvinput.factory;

import android.content.Context;
import android.media.tv.TvInputService;

/**
 * Created by cfp on 2021/2/9.
 */
public interface TunerSessionFactory {

    /**
     * create session
     * @param context
     * @return
     */
    TvInputService.Session create(Context context);
}
