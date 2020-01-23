package com.sih.policeapp;

public class Crime {
    String crime_id,criminal_id,crime_adder_authority_Id,district_of_crime,
            state_of_crime,date_of_crime,case_status,crime_type,address_of_crime
            ,rating_of_crime ;
    String time_when_crime_added;

    public Crime() {

    }

    public Crime(String crime_id, String criminal_id, String crime_adder_authority_Id, String district_of_crime, String state_of_crime, String date_of_crime, String case_status, String crime_type, String address_of_crime, String time_when_crime_added, String rating_of_crime) {
        this.crime_id = crime_id;
        this.criminal_id = criminal_id;
        this.crime_adder_authority_Id = crime_adder_authority_Id;
        this.district_of_crime = district_of_crime;
        this.state_of_crime = state_of_crime;
        this.date_of_crime = date_of_crime;
        this.case_status = case_status;
        this.crime_type = crime_type;
        this.address_of_crime = address_of_crime;
        this.time_when_crime_added = time_when_crime_added;
        this.rating_of_crime = rating_of_crime;
    }

    public String getCrime_id() {
        return crime_id;
    }

    public void setCrime_id(String crime_id) {
        this.crime_id = crime_id;
    }

    public String getCriminal_id() {
        return criminal_id;
    }

    public void setCriminal_id(String criminal_id) {
        this.criminal_id = criminal_id;
    }

    public String getCrime_adder_authority_Id() {
        return crime_adder_authority_Id;
    }

    public void setCrime_adder_authority_Id(String crime_adder_authority_Id) {
        this.crime_adder_authority_Id = crime_adder_authority_Id;
    }

    public String getDistrict_of_crime() {
        return district_of_crime;
    }

    public void setDistrict_of_crime(String district_of_crime) {
        this.district_of_crime = district_of_crime;
    }

    public String getState_of_crime() {
        return state_of_crime;
    }

    public void setState_of_crime(String state_of_crime) {
        this.state_of_crime = state_of_crime;
    }

    public String getDate_of_crime() {
        return date_of_crime;
    }

    public void setDate_of_crime(String date_of_crime) {
        this.date_of_crime = date_of_crime;
    }

    public String getCase_status() {
        return case_status;
    }

    public void setCase_status(String case_status) {
        this.case_status = case_status;
    }

    public String getCrime_type() {
        return crime_type;
    }

    public void setCrime_type(String crime_type) {
        this.crime_type = crime_type;
    }

    public String getAddress_of_crime() {
        return address_of_crime;
    }

    public void setAddress_of_crime(String address_of_crime) {
        this.address_of_crime = address_of_crime;
    }

    public String gettime_when_crime_added() {
        return time_when_crime_added;
    }

    public void setTime_of_crime(String time_of_crime) {
        this.time_when_crime_added = time_when_crime_added;
    }

    public String getRating_of_crime() {
        return rating_of_crime;
    }

    public void setRating_of_crime(String rating_of_crime) {
        this.rating_of_crime = rating_of_crime;
    }
}
