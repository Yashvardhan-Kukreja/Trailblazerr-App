package com.yash1300.trailblazerr.Models;

public class Time {

    private int sec_duration, minute_duration, hour_duration;

    public Time(int sec_duration, int minute_duration, int hour_duration) {
        this.sec_duration = sec_duration;
        this.minute_duration = minute_duration;
        this.hour_duration = hour_duration;
    }

    public int getSec_duration() {
        return sec_duration;
    }

    public int getMinute_duration() {
        return minute_duration;
    }

    public int getHour_duration() {
        return hour_duration;
    }
}
