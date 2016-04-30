package com.sburba.tvdbapi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MyTVSeriesActivity extends Activity {
    private RecyclerView recyclerView;
    private MySeriesAdapter adapter;
    List<MySeriesInformation> data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_tvseries);

        recyclerView = (RecyclerView) findViewById(R.id.my_tv_recycler_view);

        adapter =new MySeriesAdapter(this,getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Log.e("Clicked Position : ", "" + position);
                        if (position >= 0) {
                            int sID = data.get(position).getsId();

                            showSeasons(sID);
                        }
                    }
                })
        );

        Intent intent=new Intent(this,MyService.class);
        startService(intent);

        Log.v("Example", "onCreate");
        getIntent().setAction("Already created");
    }

    private void showSeasons(int sID){
        Intent seasonList = new Intent(this, SeasonListActivity.class);
        seasonList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        seasonList.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        seasonList.putExtra("sID", sID);
        startActivity(seasonList);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_series_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.tv_search:
                Intent i=new Intent(MyTVSeriesActivity.this, search_TV.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(i);
                return true;
            case R.id.myseries_settings:
                 RefreshAirDatesDB redb=new RefreshAirDatesDB(this);
                redb.RefreshAllDates();

              //  RefreshZips rezip=new RefreshZips(this);
              //  rezip.RefreshAllZips();
                return true;

            case R.id.about_my_series:
                startActivity(new Intent(this,AboutActivity.class));
                return true;
            
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public  List<MySeriesInformation> getData() {
         data = new ArrayList<>();
        int[] icons = {};
        String[] titles={};

        TvDbDBAdapter dbadapter = new TvDbDBAdapter(this);
        data=dbadapter.getAllSeries(data);

        return data;
    }

    @Override
    protected void onResume() {
        Log.v("Example", "onResume");

        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            Log.v("Example", "Force restart");
            Intent intent = new Intent(this, MyTVSeriesActivity.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else
            getIntent().setAction(null);

        super.onResume();
    }

}
