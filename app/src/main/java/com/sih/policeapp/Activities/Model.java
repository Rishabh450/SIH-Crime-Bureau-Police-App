package com.sih.policeapp.Activities;

public class Model {


    String criminal_id,criminal_name,profile_pic_url;



    float rate;
    public Model() {
    }
    public Model(String criminal_id, String criminal_name, String profile_pic_url, float rate) {
        this.criminal_id = criminal_id;
        this.criminal_name = criminal_name;
        this.profile_pic_url = profile_pic_url;
        this.rate=rate;
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

    public String getProfile_pic_url() {
        return profile_pic_url;
    }

    public void setProfile_pic_url(String profile_pic_url) {
        this.profile_pic_url = profile_pic_url;
    }
    public float getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }


}
