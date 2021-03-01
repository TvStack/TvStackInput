package com.tvstack.tvinput.source;

import com.google.android.exoplayer2.upstream.DataSource;

public abstract class TsDataSource implements DataSource {

    /**
     * 返回被buffered的ｂytes数
     * @return
     */
    public long getBufferedPosition() {
        return 0;
    }

    /**
     * 返回DataSource读取的最后的位置
     * @return
     */
    public long getLastReadPosition() {
        return 0;
    }

    /**
     * Shifts start position by the specified offset. Do not call this method when the class already
     * provided MPEG-TS stream to the extractor.
     *
     * @param offset 0 <= offset <= buffered position
     */
    public void shiftStartPosition(long offset) {}

    /**
     * 返回信号强度
     * @return
     */
    public int getSignalStrength() {
        return 0;
    }
}
