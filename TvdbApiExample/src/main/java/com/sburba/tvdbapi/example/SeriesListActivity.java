package com.sburba.tvdbapi.example;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.sburba.tvdbapi.TvdbApi;
import com.sburba.tvdbapi.TvdbItemAdapter;
import com.sburba.tvdbapi.model.Series;

import java.util.Collection;

public class SeriesListActivity extends ListActivity {
    private static final String TAG = "SeriesListActivity";
    private static String SEARCH_STRING = "";

    private TvdbItemAdapter<Series> mSeriesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        App app = App.getInstance(this);
        ImageLoader imageLoader = app.getImageLoader();
        //20160414 DILSHAN - To change the return layout to tvdb_series_item from tvdb_item
        mSeriesAdapter = new TvdbItemAdapter<Series>(this, imageLoader, R.layout.tvdb_series_item, R.id.title, R.id.image);
        setListAdapter(mSeriesAdapter);

        Bundle userData=getIntent().getExtras();

        if(userData==null){
            return;
        }

        SEARCH_STRING = userData.getString("sString");

        TvdbApi tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());
        tvdbApi.searchSeries(SEARCH_STRING, mSeriesResponseListener, mErrorListener);

        this.setTitle(SEARCH_STRING);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Series series = mSeriesAdapter.getItem(position);

        Intent seasonList = new Intent(this, SeasonListActivity.class);
        seasonList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        seasonList.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        seasonList.putExtra(SeasonListActivity.EXTRA_SERIES, series);
        seasonList.putExtra("sTitle", mSeriesAdapter.getItem(position).getTitleText());

        startActivity(seasonList);
    }

    private Response.Listener<Collection<Series>> mSeriesResponseListener = new Response.Listener<Collection<Series>>() {
        @Override
        public void onResponse(Collection<Series> series) {
            mSeriesAdapter.addAll(series);
        }
    };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(SeriesListActivity.this, "Oh noes! Something has gone awry.", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error fetching series: ", volleyError);
        }
    };
}
