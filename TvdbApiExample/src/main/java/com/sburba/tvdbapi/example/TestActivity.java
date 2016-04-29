package com.sburba.tvdbapi.example;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.sburba.tvdbapi.model.Episode;
import com.sburba.tvdbapi.parser.EpisodeParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class TestActivity extends Activity {
    private final String PATH = "/data/data/com.sburba.tvdbapi.example/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        getData(259765);

        Toast.makeText(TestActivity.this, "Started.", Toast.LENGTH_SHORT).show();

    }


    public void getData(int x) {
        EpisodeParser episodeParser = new EpisodeParser("en", 4);
        String filePath = PATH + Integer.toString(x) + "/en.zipen.xml";
        try {

            BufferedReader br = null;
            StringBuilder text = new StringBuilder();
            br = new BufferedReader(new FileReader(filePath));
            String line = "";
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();

            Collection<Episode> episodeList = episodeParser.parseListFromXmlString(text.toString());

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            Date airdate;
            Date today = new Date();
            Date todayWithZeroTime = formatter.parse(formatter.format(today));

            for (int i = 0; i < episodeList.size(); i++) {
                Episode episode = (Episode) episodeList.toArray()[i];
                airdate = episode.firstAired;

                int dateMargin = -1;
                if (airdate==null) {
                    dateMargin = -1;
                } else {
                    dateMargin = todayWithZeroTime.compareTo(airdate);
                }

                if (dateMargin == 0 || dateMargin<14) {
                    // write to database
                }
            }

        } catch (Exception e) {
            Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
