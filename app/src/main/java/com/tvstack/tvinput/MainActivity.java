package com.tvstack.tvinput;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.video.VideoListener;
import com.tvstack.tvinput.session.NetTunerRecordingSessionWorker;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    private static final String URL = "asset:///test_bj.ts";

    private NetTunerRecordingSessionWorker mSessionWorker;

    private StyledPlayerView mStylePlayerView;

    private SimpleExoPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSessionWorker = NetTunerRecordingSessionWorker.Factory.create(this);
        mSessionWorker.tune(Uri.parse(URL));
        mSessionWorker.startRecording(Uri.parse(URL));

        mStylePlayerView = findViewById(R.id.styled_player_view);
        mPlayer = new SimpleExoPlayer.Builder(this).build();
        mPlayer.setMediaItem(new MediaItem.Builder().setUri(URL).build());
        mPlayer.addVideoListener(new VideoListener() {
            @Override
            public void onRenderedFirstFrame() {
                Log.d(TAG, "onRenderedFirstFrame");
                TrackSelectionArray currentTrackSelections = mPlayer.getCurrentTrackSelections();
                for (int i = 0; i < currentTrackSelections.length; i++) {
                    TrackSelection trackSelection = currentTrackSelections.get(i);
                }
            }
        });
        mStylePlayerView.setPlayer(mPlayer);
        mPlayer.play();
    }
}