package com.tvstack.tvinput.source;

public class TsDataSourceManager {


    /**
     * create data source
     * @return
     */
    public TsDataSource createDataSource(){
        TsDataSource tsDataSource = new NetDataSource();
        return tsDataSource;
    }

}
