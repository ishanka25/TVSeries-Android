package com.sburba.tvdbapi.example;

import android.content.Context;

import com.sburba.tvdbapi.TvdbApi;

/**
 * Created by DILSHAN FERNANDO on 4/29/2016.
 */
public class RefreshZips{
    Context context;
    App app;
    TvdbApi tvdbApi;
    public RefreshZips(Context ctx){
        context=ctx;

         app = App.getInstance(context);
         tvdbApi = new TvdbApi(App.TVDB_API_KEY, "en", app.getRequestQueue());

    }

    public void RefreshAllZips(){

        try {
            TvDbDBAdapter dbadapter = new TvDbDBAdapter(context);
            int[] data = dbadapter.getAllSeriesId();
            String zipUrl;

            for (int i = 0; i < data.length; i++) {

                 zipUrl=tvdbApi.getSeriesZipFilePath(data[i]);
                DownloadFiles x =new DownloadFiles();
                boolean zipCheck =x.downloadFromUrl(zipUrl, Integer.toString(data[i]).toString(),"en","zip");

               // getSeriesAirDatesToDb(data[i]);
            }

            //Toast.makeText(context,
             //       "DONE REFRESH",
              //      Toast.LENGTH_SHORT).show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



}
