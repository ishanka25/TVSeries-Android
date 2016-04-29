package com.sburba.tvdbapi.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


    public int deletellAirDates(){
        SQLiteDatabase db=helper.getWritableDatabase();
        this.InsertAirDates(99,99,99,99,99,"no plot",new Date());
        int count =db.delete(TvDbConnectionHelper.AIRDATES_TABLE_NAME,null,null);
 //db.close();
        return  count;
    }

    public int deleteAirDates(int sId){

        SQLiteDatabase db=helper.getWritableDatabase();
        this.InsertAirDates(99,99,99,99,99,"no plot",new Date());
        String[] whereArgs={Integer.toString(sId)};
        int count =db.delete(TvDbConnectionHelper.AIRDATES_TABLE_NAME,TvDbConnectionHelper.SERIES_ID + "=?",whereArgs);
 //db.close();
        return  count;
    }

    public long InsertAirDates(int sid,int seasonid,int eid,int seasonno,int episodeno,String etitle,Date airDate){
        SQLiteDatabase db=helper.getWritableDatabase();
        ContentValues cv=new ContentValues();

        cv.put(TvDbConnectionHelper.SERIES_ID,sid);
        cv.put(TvDbConnectionHelper.SEASON_ID,seasonid);
        cv.put(TvDbConnectionHelper.EPISODE_ID,eid);
        cv.put(TvDbConnectionHelper.SEASON_NO,seasonno);
        cv.put(TvDbConnectionHelper.EPISODE_NO,episodeno);
        cv.put(TvDbConnectionHelper.EPISODE_TITLE,etitle);

        cv.put(TvDbConnectionHelper.AIR_DATE,

                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(airDate));

        long id=db.insert(TvDbConnectionHelper.AIRDATES_TABLE_NAME, null, cv);
         //db.close();
        return id;
    }




    public int deleteRow(int sId){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] whereArgs={Integer.toString(sId)};
        int count =db.delete(TvDbConnectionHelper.SERIES_TABLE_NAME, TvDbConnectionHelper.SERIES_ID + "=?", whereArgs);
 //db.close();
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
         //db.close();
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
         //cursor.close();
 //db.close();
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
        // //cursor.close();
         //db.close();
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
         //cursor.close();
         //db.close();

        return data;
    }


    public List<EpisodeAirdateModel> getTodayEpisodes(){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] Columns
                ={TvDbConnectionHelper.SERIES_ID,TvDbConnectionHelper.SEASON_ID,
                TvDbConnectionHelper.EPISODE_ID,TvDbConnectionHelper.SEASON_NO,TvDbConnectionHelper.EPISODE_NO,
                TvDbConnectionHelper.EPISODE_TITLE,TvDbConnectionHelper.AIR_DATE};

        List<EpisodeAirdateModel> data=new ArrayList<EpisodeAirdateModel>();
        int sid=-1;
        int sesid=-1;
        int epid=-1;
        int sesNo=-1;
        int epno=-1;
        String eptitle="";


        Cursor cursor=db.query(TvDbConnectionHelper.AIRDATES_TABLE_NAME,Columns,"date("+TvDbConnectionHelper.AIR_DATE+") =date('now')",null,null,null,null);
        while(cursor.moveToNext()){

            int index_sid=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_ID);
            int index_sesid=cursor.getColumnIndex(TvDbConnectionHelper.SEASON_ID);
            int index_epid=cursor.getColumnIndex(TvDbConnectionHelper.EPISODE_ID);
            int index_sesno=cursor.getColumnIndex(TvDbConnectionHelper.SEASON_NO);
            int index_epno=cursor.getColumnIndex(TvDbConnectionHelper.EPISODE_NO);
            int index_eptitle=cursor.getColumnIndex(TvDbConnectionHelper.EPISODE_TITLE);

            sid=cursor.getInt(index_sid);
             sesid=cursor.getInt(index_sesid);
             epid=cursor.getInt(index_epid);
             sesNo=cursor.getInt(index_sesno);
             epno=cursor.getInt(index_epno);
            eptitle=cursor.getString(index_eptitle);

            EpisodeAirdateModel current=new EpisodeAirdateModel();

            current.sid=sid;
            current.sesid=sesid;
            current.epid=epid;
            current.sesNo=sesNo;
            current.epno=epno;
            current.eptitle=eptitle;

            data.add(current);
        }
         //cursor.close();
         //db.close();
        return data;
    }


    public int[] getAllSeriesId( ){
        SQLiteDatabase db=helper.getWritableDatabase();
        String[] Columns={TvDbConnectionHelper.SERIES_ID};
        int sid=-1;
        Cursor cursor=db.query(TvDbConnectionHelper.SERIES_TABLE_NAME,Columns,null,null,null,null,null);
        int[] data=new int[cursor.getCount()];
        int index=0;
        while(cursor.moveToNext()){
            int index_sid=cursor.getColumnIndex(TvDbConnectionHelper.SERIES_ID);
            sid=cursor.getInt(index_sid);
            data[index]=sid;
            index++;

        }
         //cursor.close();
         //db.close();
        return data;
    }



    static class TvDbConnectionHelper extends SQLiteOpenHelper {

        private static final String DB_NMAE="TV_DB_LOCAL_DB";
        private static final int DB_VERSION=1;

        private static final String SERIES_TABLE_NAME="MY_SERIES";
        private static final String AIRDATES_TABLE_NAME="EPISODE_AIRDATES";


        private static final String SERIES_ID="_sId";
        private static final String SERIES_TITLE="_sTitle";
        private static final String SERIES_IMG="_sImgUrl";
        private static final String LAST_UPDATE_DATE="_uDate";
        private static final String SEASON_ID="_seasonId";
        private static final String EPISODE_ID="_episodeId";
        private static final String SEASON_NO="_seasonNo";
        private static final String EPISODE_NO="_episodeNo";
        private static final String AIR_DATE="_airDate";
        private static final String EPISODE_TITLE="_eTitle";

        private static final String SERIES_TABLE_CREATE="CREATE TABLE "+SERIES_TABLE_NAME+" ("+SERIES_ID+" INTEGER PRIMARY KEY ,"+SERIES_TITLE +" VARCHAR(100) ,"+SERIES_IMG+" VARCHAR(255) ," +LAST_UPDATE_DATE+" DATE);";
        private static final String SERIES_TABLE_DROP="DROP TABLE "+SERIES_TABLE_NAME+" IF EXISTS";

        private static final String AIR_DATES_TABLE_CREATE="CREATE TABLE "+AIRDATES_TABLE_NAME+
                " ("+SERIES_ID+" INTEGER ,"+ SEASON_ID+" INTEGER ,"+EPISODE_ID+" INTEGER ,"+SEASON_NO+" INTEGER ,"+EPISODE_NO+" INTEGER ,"+
                EPISODE_TITLE +" VARCHAR(100) ," +AIR_DATE+" DATE);";

        private static final String AIR_DATES_TABLE_DROP="DROP TABLE "+AIRDATES_TABLE_NAME+" IF EXISTS";


        public TvDbConnectionHelper(Context context){
            super(context,DB_NMAE,null,DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(SERIES_TABLE_CREATE);
                db.execSQL(AIR_DATES_TABLE_CREATE);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            try{
                db.execSQL(SERIES_TABLE_DROP);
                db.execSQL(AIR_DATES_TABLE_DROP);
                onCreate(db);
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}
