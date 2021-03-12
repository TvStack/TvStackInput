package com.tvstack.tvinput.session;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.tvstack.tvinput.player.ExoPlayerExtractorsFactory;
import com.tvstack.tvinput.player.ExoPlayerSampleExtractor;
import com.tvstack.tvinput.player.SampleExtractor;
import com.tvstack.tvinput.source.TsDataSource;
import com.tvstack.tvinput.source.TsDataSourceManager;

import java.io.IOException;

public class NetTunerRecordingSessionWorker implements Handler.Callback {

    private static final String TAG = "RecordingSessionWorker";

    private static final int MSG_TUNE = 1;
    private static final int MSG_START_RECORDING = 2;
    private static final int MSG_PREPARE_RECORDER = 3;
    private static final int MSG_STOP_RECORDING = 4;

    private Handler mHandler;
    /*recording storage dir */
    private String mStorageDir;
    private Context mContext;
    private DataSource mTunerSource;
    private long mRecordingStartTime;
    private SampleExtractor mRecorder;
    private TsDataSourceManager mDataSourceManager;
    private ExoPlayerSampleExtractor.Factory mExoPlayerExtractorsFactory;

    public NetTunerRecordingSessionWorker(Context context, TsDataSourceManager dataSourceManager,
                                          ExoPlayerSampleExtractor.Factory extractorsFactory) {
        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mContext = context;
        mHandler = new Handler(handlerThread.getLooper(), this);
        mDataSourceManager = dataSourceManager;
        mExoPlayerExtractorsFactory = extractorsFactory;
    }

    public void tune(Uri channelUri) {
        Log.d(TAG, "tune channelUri:" + channelUri.toString());
        mHandler.removeCallbacksAndMessages(null);
        mHandler.obtainMessage(MSG_TUNE, channelUri).sendToTarget();
    }

    public void onStopRecording() {

    }

    public void startRecording(Uri programUri) {
        mHandler.obtainMessage(MSG_START_RECORDING, programUri).sendToTarget();
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {

        switch (msg.what) {
            case MSG_TUNE:
                Uri uri = (Uri) msg.obj;
                onTune(uri.toString());
                break;
            case MSG_START_RECORDING:
                doStartRecording((Uri) msg.obj);
                break;
            case MSG_PREPARE_RECORDER:
                Log.d(TAG, "handleMessage MSG_PREPARE_RECORDER");
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return false;
    }

    private void onTune(String url) {
        mTunerSource = new DefaultDataSource(mContext, true);
    }

    private void doStartRecording(Uri uri) {
        Log.d(TAG, "doStartRecording" + uri.toString());
        mStorageDir = "";
        // TODO: 21-3-10 这句话怎么理解
//        mTunerSource.shiftStartPosition(mTunerSource.getBufferedPosition());
        mRecordingStartTime = System.currentTimeMillis();
        mRecorder =
                mExoPlayerExtractorsFactory.create(
                        uri, mTunerSource);
        mHandler.sendEmptyMessage(MSG_PREPARE_RECORDER);
    }

    public static class Factory {
        public static NetTunerRecordingSessionWorker create(Context context) {
            return new NetTunerRecordingSessionWorker(context, new TsDataSourceManager(), new ExoPlayerSampleExtractor.Factory() {
                @Override
                public ExoPlayerSampleExtractor create(Uri uri, DataSource source) {
                    return new ExoPlayerSampleExtractor(uri, new DataSource.Factory() {
                        @Override
                        public DataSource createDataSource() {
                            return source;
                        }
                    });
                }
            });
        }
    }
}
