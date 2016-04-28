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

import java.util.Collection;

public class SeasonListActivity extends Activity {

    private static final String TAG = "SeasonListActivity";

    int currentSerisId=-1;

    private String current_img_path="drawable-xhdpi/ab_bottom_solid_light_holo.9.png";

    private String current_series_title="";

    private TvdbItemAdapter<Season> mSeasonAdapter;

    private Menu thismenu;

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

        Bundle seriesTitle = getIntent().getExtras();
        if (seriesTitle == null) {
            return;
        }

        currentSerisId = seriesTitle.getInt("sID");

        //20160429 ISHANKA RANATUNGA - removed so much of shits

            if (currentSerisId!=-1){
                TvdbApi tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());
                tvdbApi.getSeasons(currentSerisId, mSeasonResponseListener, mErrorListener);
            }

            this.current_img_path = seriesTitle.getString("sImgPath");
            this.current_series_title = seriesTitle.getString("sTitle");
            this.setTitle(seriesTitle.getString("sTitle"));

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        addButtoneEnabler(menu);
        return true;
    }

    public void addButtoneEnabler(Menu menu){
        MenuItem item = menu.findItem(R.id.add_button);
        TvDbDBAdapter dbadapter = new TvDbDBAdapter( this);
        boolean myItemExist=dbadapter.checkSeriesExist(currentSerisId);
        if (!myItemExist) {
            item.setEnabled(true);
            item.getIcon().setAlpha(255);
        } else {
            // disabled
            item.setEnabled(false);
            item.getIcon().setAlpha(130);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.season_list, menu);
        thismenu=menu;
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
                boolean zipCheck =x.downloadFromUrl(zipUrl, Integer.toString(currentSerisId).toString(),"en","zip");

                if(zipCheck) {

                    boolean imgCheck=x.downloadFromUrl(this.current_img_path,Integer.toString(currentSerisId).toString(),"Banner","jpg");
                    if(imgCheck){

                        TvDbDBAdapter dbadapter = new TvDbDBAdapter( this);
                        long id=dbadapter.InsertMySeriesData(currentSerisId,current_series_title,this.current_img_path);
                        if(id>0) {

                            addButtoneEnabler(thismenu);
                            Toast.makeText(SeasonListActivity.this,"Success",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(SeasonListActivity.this, "DB error!!!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(SeasonListActivity.this, "Img Connection Error!!!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(SeasonListActivity.this, "Connection Error!!!",
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
