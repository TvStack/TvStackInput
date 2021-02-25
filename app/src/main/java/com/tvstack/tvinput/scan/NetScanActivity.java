package com.tvstack.tvinput.scan;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.media.tv.TvContract;
import android.os.Bundle;

import com.google.android.media.tv.companionlibrary.model.Channel;
import com.tvstack.tvinput.R;
import com.tvstack.tvinput.constants.Constants;

import java.util.concurrent.Executors;

public class NetScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_net_scan);
        IPTVFetchTask iptvFetchTask = new IPTVFetchTask(this, Executors.newSingleThreadExecutor());
        iptvFetchTask.execute(Constants.IPTV_REPO_URL);
    }

}