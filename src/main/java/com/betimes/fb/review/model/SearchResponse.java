package com.betimes.fb.review.model;

import java.util.List;

public class SearchResponse {
    private String q;
    private List<SearchResult> profileList;

    public List<SearchResult> getProfileList() {
        return profileList;
    }

    public void setProfileList(List<SearchResult> profileList) {
        this.profileList = profileList;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }
}
