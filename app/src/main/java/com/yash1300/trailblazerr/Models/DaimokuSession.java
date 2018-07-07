package com.yash1300.trailblazerr.Models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DaimokuSession extends RealmObject {

    @PrimaryKey
    private String date;

    private long duration_in_secs;

    public DaimokuSession() {
    }

    public DaimokuSession(String date, long duration_in_secs) {
        this.date = date;
        this.duration_in_secs = duration_in_secs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getDuration_in_secs() {
        return duration_in_secs;
    }

    public void setDuration_in_secs(long duration_in_secs) {
        this.duration_in_secs = duration_in_secs;
    }
}
