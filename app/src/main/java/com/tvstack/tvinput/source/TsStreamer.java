package com.tvstack.tvinput.source;


import com.tvstack.tvinput.data.TunerChannel;
import com.tvstack.tvinput.scan.ScanChannel;

/**
 * 定义ts流生成器，为扫台和播放提供数据
 */
public interface TsStreamer {


    /**
     * 开始推送扫台数据流
     * @return
     */
    boolean startStream(ScanChannel scanChannel);

    /**
     * 开始播放数据流
     * @param tunerChannel
     * @return
     */
    boolean startStream(TunerChannel tunerChannel);

    /**
     * 结束推流
     */
    void stopStream();

    /**
     *  创建提供MPEG-2 TS流的DataSource
     * @return
     */
    TsDataSource createDataSource();

}
