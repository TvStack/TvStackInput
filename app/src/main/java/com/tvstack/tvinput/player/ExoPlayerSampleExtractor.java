package com.tvstack.tvinput.player;

import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.FormatHolder;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderInputBuffer;
import com.google.android.exoplayer2.source.MediaPeriod;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MediaSourceEventListener;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.SampleStream;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.ExoTrackSelection;
import com.google.android.exoplayer2.trackselection.FixedTrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.TransferListener;

import java.io.IOException;

/**
 * A class that extracts samples from a live broadcast stream while storing the sample on the disk.
 * For demux, this class relies on {@link com.google.android.exoplayer.extractor.ts.TsExtractor}.
 */
public class ExoPlayerSampleExtractor implements SampleExtractor {


    private static final String TAG = "PlayerSampleExtractor";

    private HandlerThread mSourceReaderThread;
    private Handler mSourceReaderHandler;

    private final Handler.Callback mSourceReaderWorker;

    /**
     * Factory for ExoPlayerSampleExtractor
     */
    public interface Factory {
        ExoPlayerSampleExtractor create(
                Uri uri,
                DataSource source);
    }

    public ExoPlayerSampleExtractor(Uri uri, DataSource.Factory dataSourceFactory) {
        this.mSourceReaderThread = new HandlerThread("SourceReaderThread");
        this.mSourceReaderWorker = new SourceReaderWorker(new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(new MediaItem.Builder().setUri(uri).build()));
    }

    @Override
    public boolean prepare() throws IOException {
        Log.d(TAG, "prepare");
        if (!mSourceReaderThread.isAlive()) {
            mSourceReaderThread.start();
            mSourceReaderHandler = new Handler(mSourceReaderThread.getLooper(), mSourceReaderWorker);
            mSourceReaderHandler.sendEmptyMessage(SourceReaderWorker.MSG_PREPARE);
        }
        return false;
    }

    private class SourceReaderWorker implements Handler.Callback, MediaPeriod.Callback {

        public static final int MSG_PREPARE = 1;
        private static final int MSG_FETCH_SAMPLES = 2;

        private final MediaSource mSampleSource;
        private MediaPeriod mMediaPeriod;
        private SampleStream[] mStreams;
        private DecoderInputBuffer mDecoderInputBuffer;

        public SourceReaderWorker(MediaSource mSampleSource) {
            this.mSampleSource = mSampleSource;
            mSampleSource.prepareSource((source, timeline) ->
                    Log.d(TAG, "onSourceInfoRefreshed"), null);
            mDecoderInputBuffer =
                    new DecoderInputBuffer(DecoderInputBuffer.BUFFER_REPLACEMENT_MODE_NORMAL);
        }

        @Override
        public boolean handleMessage(@NonNull Message msg) {

            switch (msg.what) {
                case MSG_PREPARE:
                    Log.d(TAG, "MSG_PREPARE createPeriod");
                    mMediaPeriod = mSampleSource.createPeriod(new MediaSource.MediaPeriodId(0),
                            new DefaultAllocator(true, C.DEFAULT_BUFFER_SEGMENT_SIZE), 0);
                    mMediaPeriod.prepare(this, 0);
                    try {
                        mMediaPeriod.maybeThrowPrepareError();
                    } catch (IOException e) {
                        Log.e(TAG, e.getMessage());
                        e.printStackTrace();
                    }
                    break;
                case MSG_FETCH_SAMPLES:
                    int trackCount = mStreams.length;
                    for (int i = 0; i < trackCount; i++) {
                        fetchSample(i);
                    }
                    break;
            }
            return false;
        }


        @Override
        public void onPrepared(MediaPeriod mediaPeriod) {
            Log.d(TAG, "onPrepared");
            if (mMediaPeriod == null) {
                return;
            }
            TrackGroupArray trackGroups = mMediaPeriod.getTrackGroups();
            for (int i = 0; i < trackGroups.length; i++) {
                Log.d(TAG, "trackGroups" + trackGroups.get(i).getFormat(0));
            }
            ExoTrackSelection[] selections = new ExoTrackSelection[trackGroups.length];
            for (int i = 0; i < selections.length; ++i) {
                selections[i] = new FixedTrackSelection(trackGroups.get(i), 0);
            }
            boolean[] retain = new boolean[trackGroups.length];
            boolean[] reset = new boolean[trackGroups.length];
            mStreams = new SampleStream[trackGroups.length];
            mMediaPeriod.selectTracks(selections, retain, mStreams, reset, 0);
            mSourceReaderHandler.sendEmptyMessage(MSG_FETCH_SAMPLES);
        }

        @Override
        public void onContinueLoadingRequested(MediaPeriod source) {
            Log.d(TAG, "onContinueLoadingRequested");
        }

        private void fetchSample(int track) {
            Log.d(TAG, "fetchSample track: " + track);
            FormatHolder dummyFormatHolder = new FormatHolder();
            mDecoderInputBuffer.clear();
            int ret = mStreams[track].readData(dummyFormatHolder, mDecoderInputBuffer, false);
            Log.d(TAG, "fetchSample track: " + track + " readData ret:" + ret + " timeUs" + mDecoderInputBuffer.timeUs);
            Log.d(TAG, "isKeyFrame:" + mDecoderInputBuffer.isKeyFrame());
            queueSample(track);
        }

        private void queueSample(int track) {
        }
    }


}
