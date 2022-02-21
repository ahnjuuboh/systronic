package com.betimes.fb.review.model;

import java.util.List;

public class Audience {
    private List<AudienceCity> audienceCityList;
    private List<AudienceGenderAge> audienceGenderAgeList;
    private String male;
    private String female;
    private int[] maleAge;
    private int[] femaleAge;

    public List<AudienceCity> getAudienceCityList() {
        return audienceCityList;
    }

    public void setAudienceCityList(List<AudienceCity> audienceCityList) {
        this.audienceCityList = audienceCityList;
    }

    public List<AudienceGenderAge> getAudienceGenderAgeList() {
        return audienceGenderAgeList;
    }

    public void setAudienceGenderAgeList(List<AudienceGenderAge> audienceGenderAgeList) {
        this.audienceGenderAgeList = audienceGenderAgeList;
    }

    public String getMale() {
        return male;
    }

    public void setMale(String male) {
        this.male = male;
    }

    public String getFemale() {
        return female;
    }

    public void setFemale(String female) {
        this.female = female;
    }

    public int[] getMaleAge() {
        return maleAge;
    }

    public void setMaleAge(int[] maleAge) {
        this.maleAge = maleAge;
    }

    public int[] getFemaleAge() {
        return femaleAge;
    }

    public void setFemaleAge(int[] femaleAge) {
        this.femaleAge = femaleAge;
    }
}
