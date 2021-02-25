package com.tvstack.tvinput.session;

import android.content.Context;
import android.media.tv.TvInputManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.tvstack.tvinput.player.NetPlayer;

/**
 * Created by cfp on 2021/2/9.
 */
public class NetTunerSessionWorker implements Handler.Callback, NetPlayer.Callback {

    private static final String TAG = "NetTunerSessionWorker";

    /*what msg: tune*/
    private static final int MSG_TUNE = 1000;
    /*what msg: release*/
    private static final int MSG_RELEASE = 1001;
    /*what msg: play retry*/
    private static final int MSG_RETRY_PLAYBACK = 1002;
    /*what msg: set surface*/
    private static final int MSG_SET_SURFACE = 1024;
    /*playback retry count */
    private static final int MAX_PLAYBACK_RETRY_COUNT = 5;

    private Handler mHandler;

    private Surface mSurface;

    private NetPlayer mPlayer;

    private Context mContext;

    private NetTunerSession mTunerSession;

    private Uri mUri;
    /* retry playback count*/
    private int mRetryCount;


    public NetTunerSessionWorker(Context context, NetTunerSession tunerSession) {
        this.mContext = context;
        mTunerSession = tunerSession;
        mHandler = new Handler(this);
    }

    public void tune(Uri channelUri) {
        sendMessage(MSG_TUNE, channelUri);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {
        Log.d(TAG, "handleMessage what: " + msg.what);
        switch (msg.what) {
            case MSG_TUNE:
                return handleMessageTune((Uri) msg.obj);
            case MSG_SET_SURFACE:
                return handleMessageSetSurface();
            case MSG_RELEASE:
                return handleMessageRelease();
            case MSG_RETRY_PLAYBACK:
                return handleMessageRetryPlayback();
        }
        return false;
    }

    private boolean handleMessageRetryPlayback() {

        mHandler.removeMessages(MSG_RETRY_PLAYBACK);
        mRetryCount++;
        if (mRetryCount <= MAX_PLAYBACK_RETRY_COUNT) {
            resetPlayback();
        } else {
            stopPlayback();
            mTunerSession.notifyVideoUnavailable(TvInputManager.VIDEO_UNAVAILABLE_REASON_UNKNOWN);
        }
        return true;
    }

    private void resetPlayback() {
        Log.d(TAG, "resetPlayback");
    }

    private boolean handleMessageRelease() {
        stopPlayback();
        return false;
    }

    private void stopPlayback() {
        Log.d(TAG, "stopPlayback");
        if (mPlayer != null) {
            mPlayer.setPlayWhenReady(false);
            mPlayer.release();
            mPlayer = null;
        }
    }

    private boolean handleMessageTune(Uri uri) {

        //如果多个tune request 则跳过中间的tune request
        if (mHandler.hasMessages(MSG_TUNE)) {
            return true;
        }
        stopPlayback();
        mUri = uri;
        preparePlayback();
        startPlayback();
        return false;
    }

    private boolean handleMessageSetSurface() {

        if (mPlayer != null) {
            mPlayer.setSurface(mSurface);
        }
        return true;
    }

    public void sendMessage(int messageType, Object object) {
        mHandler.obtainMessage(messageType, object).sendToTarget();
    }

    public void setSurface(Surface surface) {

        if (surface != null && !surface.isValid()) {
            // TODO: 2021/2/19
            return;
        }
        mSurface = surface;
        mHandler.sendEmptyMessage(MSG_SET_SURFACE);
    }


    private void preparePlayback() {
        Log.d(TAG, "preparePlayback");
        NetPlayer player = createPlayer();
        mPlayer = player;
        mPlayer.prepare(mUri);
    }

    private void startPlayback() {
        Log.d(TAG, "startPlayback");
        Surface surface = mSurface;
        if (surface != null) {
            mPlayer.setSurface(surface);
        }
        mPlayer.setPlayWhenReady(true);
    }

    private NetPlayer createPlayer() {
        return new NetPlayer(mContext, this);
    }

    /**
     * release resource
     */
    public void release() {
        mHandler.sendEmptyMessage(MSG_RELEASE);
    }

    @Override
    public void onStateChanged(int playbackState) {

    }

    @Override
    public void onError(Exception e) {
        mHandler.obtainMessage(MSG_RETRY_PLAYBACK).sendToTarget();
    }

    @Override
    public void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame() {
        mTunerSession.notifyVideoAvailable();
    }

    @Override
    public void onAudioUnplayable() {

    }

    @Override
    public void onSmoothTrickplayForceStopped() {

    }

    public static class Factory {
        NetTunerSessionWorker create(Context context, NetTunerSession tunerSession) {
            return new NetTunerSessionWorker(context, tunerSession);
        }
    }
}
