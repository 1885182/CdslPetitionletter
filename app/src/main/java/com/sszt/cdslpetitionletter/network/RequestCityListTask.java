package com.sszt.cdslpetitionletter.network;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sszt.cdslpetitionletter.bean.CityBean;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * <p>请求区域列表，这里是从asset读取，实际上可以从服务器读取。</p>
 * Created by
 */
public class RequestCityListTask extends AsyncTask<Void, Void, List<CityBean>> {

    private Context mContext;
    private Callback mCallback;

    public RequestCityListTask(Context context, Callback callback) {
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onPostExecute(List<CityBean> cities) {
        if (mCallback != null)
            mCallback.callback(cities);
    }

    @Override
    protected List<CityBean> doInBackground(Void... params) {
        try {
            InputStream inputStream = mContext.getAssets().open("city_lists.json");
            ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                arrayOutputStream.write(buffer, 0, len);
            }
            arrayOutputStream.flush();
            arrayOutputStream.close();
            inputStream.close();

            String json = new String(arrayOutputStream.toByteArray());
            Gson remarkGson = new Gson();
            return remarkGson.fromJson(json, new TypeToken<List<CityBean>>() {
            }.getType());
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public interface Callback {
        void callback(List<CityBean> cities);
    }
}
