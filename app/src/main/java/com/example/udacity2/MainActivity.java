package com.example.udacity2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>> {
    private earthquakeAdapter mAdapter;
    private TextView emptyText;
    boolean isConnected;
    ArrayList<EarthQuake> earthQuakes= new ArrayList<>();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    /**
     * Constant value for the earthquake loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int EARTHQUAKE_LOADER_ID = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity","Created");
        ListView listView=findViewById(R.id.list);
        emptyText=(TextView)findViewById(R.id.empty_view);
        mAdapter=new earthquakeAdapter(this,earthQuakes);
        listView.setAdapter(mAdapter);

        /**
         * adding an implicit intent to fetch the location of earthquake data
         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake erth= earthQuakes.get(position);
                String url=erth.getmURL();
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);

            }
        });

        getSupportLoaderManager().initLoader(EARTHQUAKE_LOADER_ID, null, this).forceLoad();
        Log.i("Loader","initLoader called");

        /**
         * How to execute asynctask
         */

//        EarthquakeAsyncTask earthquakeAsyncTask=new EarthquakeAsyncTask();
//        earthquakeAsyncTask.execute(USGS_REQUEST_URL);

        /**
         * Checking connection Availability
         */
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        Log.i("Internet Connection",Boolean.toString(isConnected));
    }

    @NonNull
    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int id, @Nullable Bundle args) {
        // Create a new loader for the given URL
        Log.i("Loader","onCreateLoader() called");
       // return new EarthquakeLoader(this, USGS_REQUEST_URL);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", "time");

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> data) {
        /**
         * Clearing the data from previous loader
         */
        Log.i("Loader","onLoadFinished() called");
        View loadingIndicator=findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mAdapter.clear();
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if(!isConnected){
            emptyText.setText(R.string.no_internet);
            return;
        }
        if (data!= null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }else{
            emptyText.setText(R.string.no_data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<EarthQuake>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i("Loader","onLoaderReset() called");
        mAdapter.clear();

    }

    /**
     * below the commented method shows how to fetch data from http server
     * by using background thread on ASYNCTASK
     */

//    private class EarthquakeAsyncTask extends AsyncTask<String,Void,ArrayList<EarthQuake> >{
//
//        @Override
//        protected ArrayList<EarthQuake> doInBackground(String... strings) {
//            ArrayList<EarthQuake> earthQuakes = new ArrayList<>();
//            if(strings[0]==null){
//                return null;
//            }
//            try {
//                earthQuakes=Queryutils.fetchData(strings[0]);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//                Log.i("Error","data fetching error");
//            }
//            return earthQuakes;
//        }
//        /**
//         * This method is invoked on the main UI thread after the background work has been
//         * completed.
//         *
//         * It IS okay to modify the UI within this method. We take the {@link EarthQuake} object
//         * (which was returned from the doInBackground() method) and update the views on the screen.
//         */
//
//        @Override
//        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
//             // Clear the adapter of previous earthquake data
//             mAdapter.clear();
//
//             // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
//             // data set. This will trigger the ListView to update.
//             if (data != null && !data.isEmpty()) {
//                 mAdapter.addAll(data);
//             }
//        }
//    }
//
    /**
     *
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
