package com.tvstack.tvinput.player;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.audio.AudioListener;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.text.TextOutput;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.ModelUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

/**
 * Player for network source
 */
public class NetPlayer implements Player.EventListener,
        VideoListener,
        AudioListener,
        TextOutput {

    private static final String TAG = "NetPlayer";

    private Context mContext;
    private SimpleExoPlayer mPlayer;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({STATE_IDLE, STATE_BUFFERING, STATE_READY, STATE_ENDED})
    public @interface PlayerState {
    }

    public static final int STATE_IDLE = Player.STATE_IDLE;
    public static final int STATE_BUFFERING = Player.STATE_BUFFERING;
    public static final int STATE_READY = Player.STATE_READY;
    public static final int STATE_ENDED = Player.STATE_ENDED;

    private Callback mCallback;

    public NetPlayer(Context context, Callback callback) {
        mContext = context;
        mPlayer = new SimpleExoPlayer.Builder(context).build();
        mPlayer.addListener(this);
        mPlayer.addVideoListener(this);
        mPlayer.addAudioListener(this);
        mPlayer.addTextOutput(this);
        mCallback = callback;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, int reason) {
        Log.d(TAG, "onTimelineChanged reason:" + reason);
    }

    @Override
    public void onMediaItemTransition(@Nullable MediaItem mediaItem, int reason) {
        Log.d(TAG, "onMediaItemTransition MediaItem:" + mediaItem + " reason:" + reason);
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d(TAG, "onTracksChanged");
    }

    @Override
    public void onStaticMetadataChanged(List<Metadata> metadataList) {
        Log.d(TAG, "onStaticMetadataChanged");
    }

    @Override
    public void onIsLoadingChanged(boolean isLoading) {
        Log.d(TAG, "onIsLoadingChanged isLoading:" + isLoading);
    }

    @Override
    public void onPlaybackStateChanged(int state) {
        Log.d(TAG, "onPlaybackStateChanged state:" + state);
    }

    @Override
    public void onPlayWhenReadyChanged(boolean playWhenReady, int reason) {
        Log.d(TAG, "onPlayWhenReadyChanged playWhenReady:" + playWhenReady + " reason:" + reason);
    }

    @Override
    public void onPlaybackSuppressionReasonChanged(int playbackSuppressionReason) {
        Log.d(TAG, "onPlaybackSuppressionReasonChanged playbackSuppressionReason:" + playbackSuppressionReason);
    }

    @Override
    public void onIsPlayingChanged(boolean isPlaying) {
        Log.d(TAG, "onIsPlayingChanged isPlaying:" + isPlaying);
    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {
        Log.d(TAG, "onRepeatModeChanged repeatMode:" + repeatMode);
    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
        Log.d(TAG, "onShuffleModeEnabledChanged shuffleModeEnabled:" + shuffleModeEnabled);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.e(TAG, "onPlayerError error:" + error.toString());
        if (mCallback != null) {
            mCallback.onError(error);
        }
    }

    @Override
    public void onPositionDiscontinuity(int reason) {
        Log.d(TAG, "onPositionDiscontinuity reason:" + reason);
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        Log.d(TAG, "onPlaybackParametersChanged playbackParameters:" + playbackParameters.toString());
    }

    @Override
    public void onExperimentalOffloadSchedulingEnabledChanged(boolean offloadSchedulingEnabled) {
        Log.d(TAG, "onExperimentalOffloadSchedulingEnabledChanged offloadSchedulingEnabled:" + offloadSchedulingEnabled);
    }

    @Override
    public void onExperimentalSleepingForOffloadChanged(boolean sleepingForOffload) {
        Log.d(TAG, "onExperimentalSleepingForOffloadChanged sleepingForOffload:" + sleepingForOffload);
    }

    @Override
    public void onEvents(Player player, Player.Events events) {
        Log.d(TAG, "onEvents:" + events.toString());
    }

    @Override
    public void onCues(List<Cue> cues) {
        Log.d(TAG, "onCues");
    }

    @Override
    public void onAudioSessionIdChanged(int audioSessionId) {
        Log.d(TAG, "onAudioSessionIdChanged audioSessionId:" + audioSessionId);
    }

    @Override
    public void onAudioAttributesChanged(AudioAttributes audioAttributes) {
        Log.d(TAG, "onAudioAttributesChanged audioAttributes:" + audioAttributes.toString());
    }

    @Override
    public void onVolumeChanged(float volume) {
        Log.d(TAG, "onVolumeChanged volume:" + volume);
    }

    @Override
    public void onSkipSilenceEnabledChanged(boolean skipSilenceEnabled) {
        Log.d(TAG, "onSkipSilenceEnabledChanged skipSilenceEnabled:" + skipSilenceEnabled);
    }


    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
        Log.d(TAG, "onVideoSizeChanged");
    }

    @Override
    public void onSurfaceSizeChanged(int width, int height) {
        Log.d(TAG, "onSurfaceSizeChanged");
    }

    @Override
    public void onRenderedFirstFrame() {
        Log.d(TAG, "onRenderedFirstFrame");
        if (mCallback != null) {
            mCallback.onRenderedFirstFrame();
        }
    }

    /**
     * set player info callback
     *
     * @param callback
     */
    public void setCallback(Callback callback) {
        this.mCallback = callback;
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

    /**
     * Interface definition for a callback to be notified of changes in player state.
     */
    public interface Callback {
        /**
         * Called when player state changes.
         *
         * @param playbackState notifies the updated player state.
         */
        void onStateChanged(@PlayerState int playbackState);

        /**
         * Called when player has ended with an error.
         */
        void onError(Exception e);

        /**
         * Called when size of input video to the player changes.
         */
        void onVideoSizeChanged(int width, int height, float pixelWidthHeightRatio);

        /**
         * Called when player rendered its first frame.
         */
        void onRenderedFirstFrame();

        /**
         * Called when audio stream is unplayable.
         */
        void onAudioUnplayable();

        /**
         * Called when player drops some frames.
         */
        void onSmoothTrickplayForceStopped();
    }
}
