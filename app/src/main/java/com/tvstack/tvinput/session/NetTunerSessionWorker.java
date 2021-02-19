package com.tvstack.tvinput.session;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;

/**
 * Created by cfp on 2021/2/9.
 */
public class NetTunerSessionWorker implements Handler.Callback{

    private static final String TAG = "NetTunerSessionWorker";

    /*what msg: tune*/
    private static final int MSG_TUNE = 1000;
    /*what msg: set surface*/
    private static final int MSG_SET_SURFACE = 1024;

    private Handler mHandler;

    private Surface mSurface;

    private SimpleExoPlayer mPlayer;

    private Context mContext;

    private Uri mUri;


    public NetTunerSessionWorker(Context context){
        this.mContext = context;
        mHandler = new Handler(this);
    }

    public void tune(Uri channelUri) {
        sendMessage(MSG_TUNE, channelUri);
    }

    @Override
    public boolean handleMessage(@NonNull Message msg) {

        switch (msg.what){
            case MSG_TUNE:
                return handleMessageTune((Uri) msg.obj);
            case MSG_SET_SURFACE:
                return handleMessageSetSurface();
        }
        return false;
    }

    private boolean handleMessageTune(Uri uri) {

        //如果多个tune request 则跳过中间的tune request
        if(mHandler.hasMessages(MSG_TUNE)){
            return true;
        }
        mUri = uri;
        preparePlayback();
        return false;
    }

    private boolean handleMessageSetSurface() {

        if(mPlayer != null){
            mPlayer.setVideoSurface(mSurface);
        }else{
            resetPlayback();
        }
        return true;
    }

    public void sendMessage(int messageType, Object object) {
        mHandler.obtainMessage(messageType, object).sendToTarget();
    }

    public void setSurface(Surface surface) {

        if(surface != null && !surface.isValid()){
            // TODO: 2021/2/19
            return;
        }
        mSurface = surface;
    }

    private void resetPlayback(){
        preparePlayback();
        startPlayback();
    }

    private void preparePlayback(){
        SimpleExoPlayer player = createPlayer();
        MediaItem mediaItem = MediaItem.fromUri(mUri);
        player.setMediaItem(mediaItem);
        mPlayer = player;
        mPlayer.prepare();
    }

    private void startPlayback(){
        mPlayer.play();
    }

    private SimpleExoPlayer createPlayer() {
        return new SimpleExoPlayer.Builder(mContext).build();
    }

    public static class Factory{
        NetTunerSessionWorker create(Context context){
            return new NetTunerSessionWorker(context);
        }
    }
}
