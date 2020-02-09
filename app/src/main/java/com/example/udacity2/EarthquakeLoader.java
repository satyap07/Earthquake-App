package com.example.udacity2;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class EarthquakeLoader extends AsyncTaskLoader<ArrayList<EarthQuake>> {

    private String mURL;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        mURL=url;
        Log.i("Loader","EarthquakeLoader extending AsyncTaskLoader");
    }

    @Override
    protected void onStartLoading() {

        Log.i("Loader","onStartLoading() called");
        forceLoad();
    }

    @Nullable
    @Override
    public ArrayList<EarthQuake> loadInBackground() {
        if(mURL==null){
            return null;
        }
        ArrayList<EarthQuake>earthQuakes = null;
        try {
            earthQuakes = Queryutils.fetchData(mURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return earthQuakes;
    }
}
