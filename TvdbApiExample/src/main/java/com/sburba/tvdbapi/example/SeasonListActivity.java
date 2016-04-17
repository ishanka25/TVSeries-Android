package com.sburba.tvdbapi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.sburba.tvdbapi.TvdbApi;
import com.sburba.tvdbapi.TvdbItemAdapter;
import com.sburba.tvdbapi.model.Banner;
import com.sburba.tvdbapi.model.Season;
import com.sburba.tvdbapi.model.Series;

import java.util.Collection;

public class SeasonListActivity extends Activity {

    public static final String EXTRA_SERIES = "series";

    private static final String TAG = "SeasonListActivity";
    int currentSerisId=-1;
    private TvdbItemAdapter<Season> mSeasonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App app = App.getInstance(this);
        //20160414 DILSHAN - To change the return layout to tvdb_season_item from tvdb_item
        mSeasonAdapter = new TvdbItemAdapter<Season>(this, app.getImageLoader(),
                                                     R.layout.tvdb_season_item, R.id.title,
                                                     R.id.image);

        setContentView(R.layout.grid_list);
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        gridView.setOnItemClickListener(mOnSeasonSelectedListener);
        gridView.setAdapter(mSeasonAdapter);

        Intent intent = getIntent();
        Series series = intent.getParcelableExtra(EXTRA_SERIES);
        currentSerisId=series.id;
        if (series != null) {
            TvdbApi tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());
            tvdbApi.getSeasons(series, mSeasonResponseListener, mErrorListener);
        }

        Bundle seriesTitle=getIntent().getExtras();
        if(seriesTitle==null){
            return;
        }

        this.setTitle(seriesTitle.getString("sTitle"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.season_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.add_button:
                onAddClick();

                return true;
            case R.id.action_settings:
                Toast.makeText(SeasonListActivity.this, "Settings",
                        Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void onAddClick(){
        if(currentSerisId!=-1){
            App app = App.getInstance(this);
            TvdbApi tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());
            String zipUrl=tvdbApi.getSeriesZipFilePath(currentSerisId);



            if (zipUrl!=null){
                DownloadFiles x =new DownloadFiles();
                boolean y =x.downloadFromUrl(zipUrl, Integer.toString(currentSerisId).toString(),"en","zip");

                if(y)
                {
                    Toast.makeText(SeasonListActivity.this, "Added Susscessfully",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SeasonListActivity.this, "Error!!!",
                            Toast.LENGTH_SHORT).show();
                }

            }


        }

    }

    private AdapterView.OnItemClickListener mOnSeasonSelectedListener =
            new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent episodeList =
                            new Intent(SeasonListActivity.this, EpisodeListActivity.class);
                    episodeList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    episodeList.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                    episodeList.putExtra(EpisodeListActivity.EXTRA_SEASON,
                                         mSeasonAdapter.getItem(position));
                    episodeList.putExtra("sTitle", mSeasonAdapter.getItem(position).getTitleText());

                    startActivity(episodeList);
                }
            };

    private Response.Listener<Collection<Season>> mSeasonResponseListener =
            new Response.Listener<Collection<Season>>() {
                @Override
                public void onResponse(Collection<Season> seasons) {
                    for (Season season : seasons) {
                        for (Banner banner : season.banners) {
                            Log.d(TAG, "type1: " + banner.type + " type2: " + banner.type2);
                        }
                    }
                    mSeasonAdapter.addAll(seasons);
                }
            };

    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(SeasonListActivity.this, "Oh noes! Something has gone awry.",
                           Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error fetching seasons: ", volleyError);
        }
    };
}
