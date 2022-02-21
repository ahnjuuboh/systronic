package com.betimes.fb.review.model;

public class AudienceGenderAge implements Comparable<AudienceGenderAge> {
    private String ageRange;
    private Integer value;

    public AudienceGenderAge(String ageRange, Integer value) {
        this.ageRange = ageRange;
        this.value = value;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(AudienceGenderAge u) {
        if (getValue() == null || u.getValue() == null) {
            return 0;
        }
        return getValue().compareTo(u.getValue());
    }
}
