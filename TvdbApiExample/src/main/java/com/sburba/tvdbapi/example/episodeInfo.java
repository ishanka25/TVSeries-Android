package com.sburba.tvdbapi.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class episodeInfo extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode_info);

        App app = App.getInstance(this);
        ImageLoader imageLoader = app.getImageLoader();

        Bundle userData=getIntent().getExtras();

        RatingBar ratingBar=(RatingBar)findViewById(R.id.ratingBar);
        ratingBar.setRating(userData.getFloat("rating"));

        TextView ratingTxt=(TextView)findViewById(R.id.textView_rating);
        ratingTxt.setText(Float.toString(userData.getFloat("rating")));

        TextView textView=(TextView)findViewById(R.id.ei_plot);
        textView.setText(userData.getString("plot"));

        TextView title=(TextView)findViewById(R.id.ei_title);
        title.setText(userData.getString("title"));

        NetworkImageView imageView=(NetworkImageView)findViewById(R.id.ei_image);
        imageView.setImageUrl(userData.getString("imgURL"), imageLoader);

        TextView seas=(TextView)findViewById(R.id.ei_season);
        seas.setText("Season : " + Integer.toString(userData.getInt("season")));

        TextView epi=(TextView)findViewById(R.id.ei_episode);
        epi.setText("Episode : " + Integer.toString(userData.getInt("epNo")));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episode_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.about_epi) {
            startActivity(new Intent(this,AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
