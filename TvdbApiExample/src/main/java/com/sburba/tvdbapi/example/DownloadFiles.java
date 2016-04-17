package com.sburba.tvdbapi.example;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by DILSHAN FERNANDO on 4/17/2016.
 */
public class DownloadFiles  extends AsyncTask<String,Void,Void> {
    private final String PATH = "/data/data/com.sburba.tvdbapi.example/";

    public boolean downloadFromUrl(String surl, String folder, String fileName, String fileExtention) {

        try {


            new DownloadFiles().execute(surl,folder,fileName,fileExtention);

        }

        catch(Exception e5){
            e5.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected Void doInBackground(String... surl) {
        try {
            URL url = new URL(surl[0]);

            URLConnection ucon = url.openConnection();
            HttpURLConnection httpConn = (HttpURLConnection) ucon;

            //InputStream is = httpConn.getInputStream();

            InputStream is;
            if (httpConn.getResponseCode() >= 400) {
                is = httpConn.getErrorStream();
            } else {
                is = httpConn.getInputStream();
            }

            BufferedInputStream bis = new BufferedInputStream(is);

            // ByteArrayBuffer baf = new ByteArrayBuffer(50);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] data = new byte[50];


            int current = 0;
            // while ((current = bis.read()) != -1) {
            //   baf.append((byte) current);
            //}

            while ((current = bis.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, current);
            }
            File dir = new File(PATH +surl[1]+"/");

            if (dir.exists() == false) {
                dir.mkdirs();
            }

            File file = new File(PATH +surl[1]+"/"+surl[2]+"."+surl[3]);


            FileOutputStream fos = new FileOutputStream(file);
            fos.write(buffer.toByteArray());
            fos.flush();
            fos.close();
            bis.close();
            is.close();


        } catch (Exception e5) {
            e5.printStackTrace();
return null;
        }
        return null;
    }
}
