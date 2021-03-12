package com.tvstack.tvinput.source;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.FileDataSource;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NetDataSource extends TsDataSource {

    private static final String TAG = "NetDataSource";

    private Uri mUri;
    private long mStartBufferedPosition;

    private FileDataSource mHttpDataSource;

    public NetDataSource() {
        this.mStartBufferedPosition = mStartBufferedPosition;
        mHttpDataSource = new FileDataSource.Factory().createDataSource();
    }

    @Override
    public void addTransferListener(TransferListener transferListener) {
        mHttpDataSource.addTransferListener(transferListener);
    }

    @Override
    public long open(DataSpec dataSpec) throws IOException {
        Log.d(TAG, "open" + dataSpec.uri);
        mUri = dataSpec.uri;
        return mHttpDataSource.open(dataSpec);
    }

    @Nullable
    @Override
    public Uri getUri() {
        Log.d(TAG, "getUri");
        return mUri;
    }

    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return mHttpDataSource.getResponseHeaders();
    }

    @Override
    public void close() throws IOException {
        Log.d(TAG, "close");
        mHttpDataSource.close();
    }

    @Override
    public int read(byte[] target, int offset, int length) throws IOException {
        Log.d(TAG, "read target:" + new String(target) + " offset:" + offset + " length:" +length);
        return mHttpDataSource.read(target, offset, length);
    }
}
