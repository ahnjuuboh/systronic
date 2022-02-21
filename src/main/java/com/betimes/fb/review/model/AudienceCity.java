package com.betimes.fb.review.model;

public class AudienceCity implements Comparable<AudienceCity> {
    private String city;
    private Integer value;

    public AudienceCity(String city, Integer value) {
        this.city = city;
        this.value = value;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(AudienceCity u) {
        if (getValue() == null || u.getValue() == null) {
            return 0;
        }
        return getValue().compareTo(u.getValue());
    }
}
