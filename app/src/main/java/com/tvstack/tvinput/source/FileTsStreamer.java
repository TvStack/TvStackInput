package com.tvstack.tvinput.source;

import android.net.Uri;

import androidx.annotation.Nullable;

import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.tvstack.tvinput.data.TunerChannel;
import com.tvstack.tvinput.scan.ScanChannel;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class FileTsStreamer implements TsStreamer {


    private long mBytesFetched;


    @Override
    public boolean startStream(ScanChannel scanChannel) {
        return false;
    }

    @Override
    public boolean startStream(TunerChannel tunerChannel) {
        return false;
    }

    @Override
    public void stopStream() {

    }

    @Override
    public TsDataSource createDataSource() {
        return null;
    }

    public static class FileDataSource extends TsDataSource{

        private FileTsStreamer mFileTsStreamer;

        private long mStartBufferedPosition;

        private final AtomicLong mLastReadPosition = new AtomicLong(0);

        private Uri mUri;

        private FileDataSource(FileTsStreamer tsStreamer) {
            mFileTsStreamer = tsStreamer;
            mStartBufferedPosition = tsStreamer.getBufferedPosition();
        }

        @Override
        public long getBufferedPosition() {
            return mFileTsStreamer.getBufferedPosition() - mStartBufferedPosition;
        }

        @Override
        public long getLastReadPosition() {
            return mLastReadPosition.get();
        }

        @Override
        public void shiftStartPosition(long offset) {
            mStartBufferedPosition += offset;
        }

        @Override
        public void addTransferListener(TransferListener transferListener) {

        }

        @Override
        public long open(DataSpec dataSpec) throws IOException {
            mUri = dataSpec.uri;
            mLastReadPosition.set(0);
            return com.google.android.exoplayer2.C.LENGTH_UNSET;
        }

        @Nullable
        @Override
        public Uri getUri() {
            return mUri;
        }

        @Override
        public void close() throws IOException {

        }

        @Override
        public int read(byte[] buffer, int offset, int length) throws IOException {

            int ref = mFileTsStreamer.readAt(mStartBufferedPosition + mLastReadPosition.get(),
                    buffer, offset, length);
            if(ref > 0){
                mLastReadPosition.addAndGet(ref);
            }
            return ref;
        }
    }

    private int readAt(long l, byte[] buffer, int offset, int length) {
        return 0;
    }

    private long getBufferedPosition() {
        return 0;
    }
}
