package com.yash1300.trailblazerr.Models;

import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DaimokuSession {

    private Date date;

    private List<Time> times;

    public DaimokuSession() {
    }

    public DaimokuSession(Date date, List<Time> times) {
        this.date = date;
        this.times = times;
    }

    public Date getDate() {
        return date;
    }

    public List<Time> getTimes() {
        return times;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTimes(List<Time> times) {
        this.times = times;
    }
}
