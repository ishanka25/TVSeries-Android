package com.sburba.tvdbapi.example;

import android.content.Context;

import com.sburba.tvdbapi.model.Episode;
import com.sburba.tvdbapi.parser.EpisodeParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by DILSHAN FERNANDO on 4/29/2016.
 */
 class RefreshAirDatesDB {
    private final String PATH = "/data/data/com.sburba.tvdbapi.example/";
    Context context;
    public RefreshAirDatesDB(Context ctx){
        context=ctx;


    }
    public void RefreshAllDates(){

        try {
            TvDbDBAdapter dbadapter = new TvDbDBAdapter(context);
            int[] data = dbadapter.getAllSeriesId();
            dbadapter.deletellAirDates();
            for (int i = 0; i < data.length; i++) {
                getSeriesAirDatesToDb(data[i]);
            }

            //Toast.makeText(context,
              //      "DONE REFRESH",
                //    Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public void getSeriesAirDatesToDb(int x) {
        EpisodeParser episodeParser = new EpisodeParser("en");
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

                if (dateMargin == 0 || (dateMargin<0 && dateMargin<14)) {
                   String etitle=episode.getTitleText();
                    if(etitle.equals(null)){
                        etitle="No Plot Found!";
                    }
                    if (airdate!=null) {
                    TvDbDBAdapter dbadapter = new TvDbDBAdapter( context);
                    long id=dbadapter.InsertAirDates(episode.seriesId,episode.seasonId,episode.id,episode.seasonNumber,episode.number,episode.getTitleText(),episode.firstAired);
                }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }




}
