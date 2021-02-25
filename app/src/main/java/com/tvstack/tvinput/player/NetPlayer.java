package com.tvstack.tvinput.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.decoder.DecoderReuseEvaluation;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.ModelUtils;

import java.util.List;

/**
 * Player for network source
 */
public class NetPlayer implements Player.EventListener,
        VideoListener,
        AudioListener,
        TextOutput,
        VideoRendererEventListener {

    private static final String TAG = "NetPlayer";

    private Context mContext;
    private SimpleExoPlayer mPlayer;

    public NetPlayer(Context context) {
        this.mContext = context;
        this.mPlayer = new SimpleExoPlayer.Builder(context).build();
    }

    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {

    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onStaticMetadataChanged(List<Metadata> metadataList) {

    }

    @Override
    public void onIsLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlaybackStateChanged(int state) {

    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {

    }

    @Override
    public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {

    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onExperimentalOffloadSchedulingEnabledChanged(boolean offloadSchedulingEnabled) {

    }

    @Override
    public void onExperimentalSleepingForOffloadChanged(boolean sleepingForOffload) {

    }

    @Override
    public void onEvents(Player player, Player.Events events) {

    }

    @Override
    public void onCues(List<Cue> cues) {

    }

    @Override
    public void onAudioSessionIdChanged(int audioSessionId) {

    }

    @Override
    public void onAudioAttributesChanged(AudioAttributes audioAttributes) {

    }

    @Override
    public void onVolumeChanged(float volume) {

    }

    @Override
    public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {

    }

    @Override
    public void onVideoEnabled(DecoderCounters counters) {

    }

    @Override
    public void onVideoDecoderInitialized(String decoderName, long initializedTimestampMs, long initializationDurationMs) {

    }

    @Override
    public void onVideoInputFormatChanged(Format format, @Nullable DecoderReuseEvaluation decoderReuseEvaluation) {

    }

    @Override
    public void onDroppedFrames(int count, long elapsedMs) {

    }

    @Override
    public void onVideoFrameProcessingOffset(long totalProcessingOffsetUs, int frameCount) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onRenderedFirstFrame(@Nullable Surface surface) {

    }

    @Override
    public void onVideoDecoderReleased(String decoderName) {

    }

    @Override
    public void onVideoDisabled(DecoderCounters counters) {

    }

    @Override
    public void onSurfaceSizeChanged(int width, int height) {

    }

    @Override
    public void onRenderedFirstFrame() {

    }


    /**
     * Sets the surface for the player.
     *
     * @param surface the {@link Surface} to render video
     */
    public void setSurface(Surface surface) {
        mPlayer.setVideoSurface(surface);
    }


    public boolean prepare(Uri uri) {
        Log.d(TAG, "prepare uri:" + uri.toString());
        Channel channel = ModelUtils.getChannel(mContext.getContentResolver(), uri);
        MediaItem mediaItem = MediaItem.fromUri(channel.getInternalProviderData().getVideoUrl());
        Log.d(TAG, "prepare real url:" + channel.getInternalProviderData().getVideoUrl());
        mPlayer.addMediaItem(mediaItem);
        mPlayer.prepare();
        return false;
    }

    /**
     * Sets whether playback should proceed when {@link Player#getPlaybackState()} == {@link Player#STATE_READY}.
     *
     * <p>If the player is already in the ready state then this method pauses and resumes playback.
     *
     * @param playWhenReady Whether playback should proceed when ready.
     */
    public void setPlayWhenReady(boolean playWhenReady) {
        mPlayer.setPlayWhenReady(playWhenReady);
    }

    /**
     * release player
     */
    public void release() {
        mPlayer.release();
    }
}
