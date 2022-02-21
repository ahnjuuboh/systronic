package com.betimes.fb.review.model;

public class FacebookRequest {
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String gender;
    private Picture picture;
    private FacebookAuth authResponse;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public FacebookAuth getAuthResponse() {
        return authResponse;
    }

    public void setAuthResponse(FacebookAuth authResponse) {
        this.authResponse = authResponse;
    }
}
