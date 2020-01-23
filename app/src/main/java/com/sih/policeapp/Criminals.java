package com.sih.policeapp;

import java.util.ArrayList;

public class Criminals {
    String criminal_id,criminal_name,criminal_address,criminals_DOB,criminal_BodyMark , profile_pic_url ,criminal_rating;

    public Criminals() {
    }

    public Criminals(String criminal_id, String criminal_name, String criminal_address, String criminals_DOB, String criminal_BodyMark, String profile_pic_url, String criminal_rating) {
        this.criminal_id = criminal_id;
        this.criminal_name = criminal_name;
        this.criminal_address = criminal_address;
        this.criminals_DOB = criminals_DOB;
        this.criminal_BodyMark = criminal_BodyMark;
        this.profile_pic_url = profile_pic_url;
        this.criminal_rating = criminal_rating;

    }

    public Criminals(String criminal_id, String criminal_name, String criminal_address, String criminals_DOB, String criminal_BodyMark, String criminal_rating) {
        this.criminal_id = criminal_id;
        this.criminal_name = criminal_name;
        this.criminal_address = criminal_address;
        this.criminals_DOB = criminals_DOB;
        this.criminal_BodyMark = criminal_BodyMark;
        this.criminal_rating = criminal_rating;

    }

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }

    public String getCriminal_id() {
        return criminal_id;
    }

    public void setCriminal_id(String criminal_id) {
        this.criminal_id = criminal_id;
    }

    public String getCriminal_name() {
        return criminal_name;
    }

    public void setCriminal_name(String criminal_name) {
        this.criminal_name = criminal_name;
    }

    public String getCriminal_address() {
        return criminal_address;
    }

    public void setCriminal_address(String criminal_address) {
        this.criminal_address = criminal_address;
    }

    public String getCriminals_DOB() {
        return criminals_DOB;
    }

    public void setCriminals_DOB(String criminals_DOB) {
        this.criminals_DOB = criminals_DOB;
    }

    public String getCriminal_BodyMark() {
        return criminal_BodyMark;
    }

    public void setCriminal_BodyMark(String criminal_BodyMark) {
        this.criminal_BodyMark = criminal_BodyMark;
    }

    public String getCriminal_rating() {
        return criminal_rating;
    }

    public void setCriminal_rating(String criminal_rating) {
        this.criminal_rating = criminal_rating;
    }

}
