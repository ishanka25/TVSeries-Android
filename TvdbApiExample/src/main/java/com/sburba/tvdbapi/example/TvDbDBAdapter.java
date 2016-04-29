package com.sburba.tvdbapi.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by DILSHAN FERNANDO on 4/23/2016.
 */
public class TvDbDBAdapter {
    TvDbConnectionHelper helper;
    TvDbDBAdapter(Context context){
        helper= new TvDbConnectionHelper(context);
    }

    public int deleteRow(int sId){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] whereArgs={Integer.toString(sId)};
        int count =db.delete(TvDbConnectionHelper.SERIES_TABLE_NAME,TvDbConnectionHelper.SERIES_ID + "=?",whereArgs);

        return  count;
    }

    public long InsertMySeriesData(int sid,String stitle,String imgurl){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(TvDbConnectionHelper.SERIES_ID,sid);
        cv.put(TvDbConnectionHelper.SERIES_TITLE,stitle);
        cv.put(TvDbConnectionHelper.SERIES_IMG,imgurl);
        cv.put(TvDbConnectionHelper.LAST_UPDATE_DATE,
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

        long id=db.insert(TvDbConnectionHelper.SERIES_TABLE_NAME, null, cv);
        return id;
    }

    public boolean checkSeriesExist(int sid){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] Columns={TvDbConnectionHelper.SERIES_TITLE};
        String title="-99";

        Cursor cursor=db.query(TvDbConnectionHelper.SERIES_TABLE_NAME, Columns, TvDbConnectionHelper.SERIES_ID + " = '" + Integer.toString(sid) + "'", null, null, null, null);
        while(cursor.moveToNext()){
            int index_title=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_TITLE);

            title=cursor.getString(index_title);
        }

        if(!title.equals("-99")){
            return true;
        }
        else{
            return false;
        }
    }

    public String[] getSeriesTitle(int sid){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] Columns={TvDbConnectionHelper.SERIES_TITLE,TvDbConnectionHelper.SERIES_IMG};
        String title="-99";
        String imgurl="-99";
        Cursor cursor=db.query(TvDbConnectionHelper.SERIES_TABLE_NAME, Columns, TvDbConnectionHelper.SERIES_ID + " = '" + Integer.toString(sid) + "'", null, null, null, null);
        while(cursor.moveToNext()){
            int index_title=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_TITLE);
            int index_img=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_IMG);
            title=cursor.getString(index_title);
            imgurl=cursor.getString(index_img);
        }
        String[] result={title,imgurl};
        return result;
    }

    public List<MySeriesInformation> getAllSeries(List<MySeriesInformation> data ){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] Columns={TvDbConnectionHelper.SERIES_ID,TvDbConnectionHelper.SERIES_TITLE};
        int sid=-1;
        String title="";
        Cursor cursor=db.query(TvDbConnectionHelper.SERIES_TABLE_NAME,Columns,null,null,null,null,TvDbConnectionHelper.SERIES_TITLE);
        while(cursor.moveToNext()){
            int index_title=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_TITLE);
            int index_sid=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_ID);
            sid=cursor.getInt(index_sid);
            title=cursor.getString(index_title);

            MySeriesInformation current=new MySeriesInformation();
            current.setsId(sid);
            current.setTitle(title);
            data.add(current);
        }

        return data;
    }

    static class TvDbConnectionHelper extends SQLiteOpenHelper {

        private static final String DB_NMAE="TV_DB_LOCAL_DB";
        private static final int DB_VERSION=1;

        private static final String SERIES_TABLE_NAME="MY_SERIES";
        private static final String SERIES_ID="_sId";
        private static final String SERIES_TITLE="_sTitle";
        private static final String SERIES_IMG="_sImgUrl";
        private static final String LAST_UPDATE_DATE="_uDate";
        private static final String SERIES_TABLE_CREATE="CREATE TABLE "+SERIES_TABLE_NAME+" ("+SERIES_ID+" INTEGER PRIMARY KEY ,"+SERIES_TITLE +" VARCHAR(100) ,"+SERIES_IMG+" VARCHAR(255) ," +LAST_UPDATE_DATE+" DATE);";
        private static final String SERIES_TABLE_DROP="DROP TABLE "+SERIES_TABLE_NAME+" IF EXISTS";


        public TvDbConnectionHelper(Context context){
            super(context,DB_NMAE,null,DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(SERIES_TABLE_CREATE);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try{
                db.execSQL(SERIES_TABLE_DROP);
                onCreate(db);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
