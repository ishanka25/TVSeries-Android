package com.sburba.tvdbapi.example;

/**
 * Created by DILSHAN FERNANDO on 4/29/2016.
 */
public class EpisodeAirdateModel {
    int sid;
    int sesid;
    int epid;
    int sesNo;
    int epno;
    String eptitle;

    public EpisodeAirdateModel(){
         sid=-1;
         sesid=-1;
         epid=-1;
         sesNo=-1;
         epno=-1;
         eptitle="";
    }

    public int getSid() {
        return sid;
    }

    public int getSesid() {
        return sesid;
    }

    public int getEpid() {
        return epid;
    }

    public int getSesNo() {
        return sesNo;
    }

    public int getEpno() {
        return epno;
    }

    public String getEptitle() {
        return eptitle;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setSesid(int sesid) {
        this.sesid = sesid;
    }

    public void setEpid(int epid) {
        this.epid = epid;
    }

    public void setSesNo(int sesNo) {
        this.sesNo = sesNo;
    }

    public void setEpno(int epno) {
        this.epno = epno;
    }

    public void setEptitle(String eptitle) {
        this.eptitle = eptitle;
    }
}
