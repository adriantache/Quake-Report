package com.adriantache.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
    private MainActivity mainActivity;
    private String url;

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    EarthquakeLoader(MainActivity mainActivity, @NonNull Context context, String url) {
        super(context);
        this.mainActivity = mainActivity;
        this.url = url;
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if (TextUtils.isEmpty(url)) return null;
        return mainActivity.extractQuakes(Utils.makeHTTPConnection(url));
    }
}
