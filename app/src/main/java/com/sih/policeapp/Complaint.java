package com.sih.policeapp;

import java.util.Map;

public class Complaint {

    String level;
    String details, userId;
    long timeStamp;

    public Complaint() {
    }

    public Complaint(String level, String details, String userId, long timeStamp) {
        this.level = level;
        this.details = details;
        this.userId = userId;
        this.timeStamp = timeStamp;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

