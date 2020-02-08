package com.sih.policeapp;

public class PoliceClass {

    String profile_pic,email_id,rating, designation,crimeSolved,policeStationId,posted_city,Notification,police_name,psHead;


    public PoliceClass(String profile_pic, String email_id, String rating, String disignation, String policeStationId, String posted_city, String notification, String police_name, String psHead) {
        this.profile_pic = profile_pic;
        this.email_id = email_id;
        this.rating = rating;
        this.designation = disignation;
        this.policeStationId = policeStationId;
        this.posted_city = posted_city;
        Notification = notification;
        this.police_name = police_name;
        this.psHead = psHead;
    }

    public String getPolice_name() {
        return police_name;
    }

    public void setPolice_name(String police_name) {
        this.police_name = police_name;
    }

    public PoliceClass() {
    }

    public String getPsHead() {
        return psHead;
    }

    public void setPsHead(String psHead) {
        this.psHead = psHead;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getCrimeSolved() {
        return crimeSolved;
    }

    public void setCrimeSolved(String crimeSolved) {
        this.crimeSolved = crimeSolved;
    }

    public String getPoliceStationId() {
        return policeStationId;
    }

    public void setPoliceStationId(String policeStationId) {
        this.policeStationId = policeStationId;
    }

    public String getPosted_city() {
        return posted_city;
    }

    public void setPosted_city(String posted_city) {
        this.posted_city = posted_city;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}
