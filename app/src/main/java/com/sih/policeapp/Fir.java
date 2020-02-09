package com.sih.policeapp;

import java.util.Map;

public class Fir {

    private String complainantId, state, district, place, type, subject, details,ts;
    private String status, reportingDate , reportingPlace , correspondent;
    private Map<String, String> timeStamp;

    public Fir() {

    }


    public Fir(String complainantId, String state, String district, String place, String type, String subject, String details
            , Map<String, String> timeStamp, String status, String reportingDate, String reportingPlace, String correspondent) {
        this.complainantId = complainantId;
        this.state = state;
        this.district = district;
        this.place = place;
        this.type = type;
        this.subject = subject;
        this.details = details;
        this.timeStamp = timeStamp;
        this.status = status;
        this.reportingDate = reportingDate;
        this.reportingPlace = reportingPlace;
        this.correspondent = correspondent;
    }

    public Fir(String complainantId, String state, String district, String place, String type, String subject, String details
            , String ts, String status, String reportingDate, String reportingPlace, String correspondent) {
        this.complainantId = complainantId;
        this.state = state;
        this.district = district;
        this.place = place;
        this.type = type;
        this.subject = subject;
        this.details = details;
        this.ts = ts;
        this.status = status;
        this.reportingDate = reportingDate;
        this.reportingPlace = reportingPlace;
        this.correspondent = correspondent;
    }


    public String getComplainantId() {
        return complainantId;
    }

    public void setComplainantId(String complainantId) {
        this.complainantId = complainantId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


    public Map<String, String> getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Map<String, String> timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReportingDate() {
        return reportingDate;
    }

    public void setReportingDate(String reportingDate) {
        this.reportingDate = reportingDate;
    }

    public String getReportingPlace() {
        return reportingPlace;
    }

    public void setReportingPlace(String reportingPlace) {
        this.reportingPlace = reportingPlace;
    }

    public String getCorrespondent() {
        return correspondent;
    }

    public void setCorrespondent(String correspondent) {
        this.correspondent = correspondent;
    }
}
