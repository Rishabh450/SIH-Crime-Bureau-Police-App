package com.sih.policeapp;

public class User {

    private String name, email, imageURL, userId, gender, fathername, address, age, pincode, phone, fax , aadhaar, notificationId;


    public User() {
    }


    public User(String name, String email, String imageURL, String userId) {
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.userId = userId;
    }


    public User(String name, String email, String imageURL, String userId, String gender, String age, String fathername, String address,
                String pincode, String phone, String  fax, String aadhaar) {
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.userId = userId;
        this.gender = gender;
        this.age = age;
        this.fathername = fathername;
        this.address = address;
        this.pincode = pincode;
        this.phone = phone;
        this.fax = fax;
        this.aadhaar = aadhaar;
    }

    public User(String name, String email, String imageURL, String userId, String gender, String fathername, String address,
                String age, String pincode, String phone, String fax, String aadhaar, String notificationId) {
        this.name = name;
        this.email = email;
        this.imageURL = imageURL;
        this.userId = userId;
        this.gender = gender;
        this.fathername = fathername;
        this.address = address;
        this.age = age;
        this.pincode = pincode;
        this.phone = phone;
        this.fax = fax;
        this.aadhaar = aadhaar;
        this.notificationId = notificationId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFathername() {
        return fathername;
    }

    public void setFathername(String fathername) {
        this.fathername = fathername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAadhaar() {
        return aadhaar;
    }

    public void setAadhaar(String aadhaar) {
        this.aadhaar = aadhaar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }
}
