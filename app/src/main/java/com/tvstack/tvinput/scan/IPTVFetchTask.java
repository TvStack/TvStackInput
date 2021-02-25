package com.tvstack.tvinput.scan;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.media.tv.TvContract;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.media.tv.companionlibrary.model.Channel;
import com.google.android.media.tv.companionlibrary.model.InternalProviderData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.tvstack.tvinput.constants.Constants;
import com.tvstack.tvinput.entity.IPTVEntity;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Fetch IPTV DATA from https://github.com/iptv-org/iptv
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 */
public class IPTVFetchTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {


    private static final String TAG = "IPTVFetchTask";
    private final Context mContext;
    /*fetch iptv list executor */
    private final Executor mExecutor;
    /*iptv data url */
    private String mUrl;

    protected IPTVFetchTask(Context context, Executor mExecutor) {
        this.mContext = context;
        this.mExecutor = mExecutor;
    }

    @Override
    protected Result doInBackground(Params... params) {
        mUrl = (String) params[0];
        mExecutor.execute(new FetchIPTV());
        return null;
    }

    /**
     * fetch iptv data runnable
     */
    class FetchIPTV implements Runnable {

        @Override
        public void run() {
            Log.d(TAG, "FetchIPTV mUrl: " + mUrl);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(mUrl)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String json = response.body().string();
                Gson gson = new GsonBuilder().create();
                List<IPTVEntity> iptvEntities = gson.fromJson(json, new TypeToken<List<IPTVEntity>>() {
                }.getType());
                insertIPTVToDB(iptvEntities);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * insert iptv data to provider
     * @param iptvEntities
     */
    private void insertIPTVToDB(List<IPTVEntity> iptvEntities) {
        Log.d(TAG, "insertIPTVToDB iptvEntities list:" + iptvEntities.size());
        for (int i = 0; i < iptvEntities.size(); i++) {
            IPTVEntity iptvEntity = iptvEntities.get(i);
            InternalProviderData internalProviderData = new InternalProviderData();
            internalProviderData.setVideoUrl(iptvEntity.url);
            Channel channel = new Channel.Builder()
                    .setDisplayNumber(String.valueOf(i))
                    .setDisplayName(iptvEntity.name)
                    .setInputId(Constants.IPTV_INPUT_ID)
                    .setOriginalNetworkId(i)
                    .setInternalProviderData(internalProviderData)
                    .build();
            Log.d(TAG, "channel:" + channel.toString());
            ContentResolver cr = mContext.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(TvContract.Channels.COLUMN_DISPLAY_NUMBER, channel.getDisplayNumber());
            values.put(TvContract.Channels.COLUMN_DISPLAY_NAME, channel.getDisplayName());
            values.put(TvContract.Channels.COLUMN_INPUT_ID, channel.getInputId());
            values.put(TvContract.Channels.COLUMN_ORIGINAL_NETWORK_ID, channel.getOriginalNetworkId());
            values.put(TvContract.Channels.COLUMN_TRANSPORT_STREAM_ID, channel.getTransportStreamId());
            values.put(TvContract.Channels.COLUMN_SERVICE_ID, channel.getServiceId());
            values.put(TvContract.Channels.COLUMN_INTERNAL_PROVIDER_DATA, channel.getInternalProviderDataByteArray());
        }
    }
}
