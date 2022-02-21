package com.betimes.fb.review.model;

import java.util.List;

public class IgUserInfo {
    private String id;
    private String name;
    private String username;
    private Integer media_count;
    private Integer followers_count;
    private Integer follows_count;
    private String biography;
    private String profile_picture_url;
    private String website;
    private List<IgMedia> media;
    private Audience audience;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getMedia_count() {
        return media_count;
    }

    public void setMedia_count(Integer media_count) {
        this.media_count = media_count;
    }

    public Integer getFollowers_count() {
        return followers_count;
    }

    public void setFollowers_count(Integer followers_count) {
        this.followers_count = followers_count;
    }

    public Integer getFollows_count() {
        return follows_count;
    }

    public void setFollows_count(Integer follows_count) {
        this.follows_count = follows_count;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getProfile_picture_url() {
        return profile_picture_url;
    }

    public void setProfile_picture_url(String profile_picture_url) {
        this.profile_picture_url = profile_picture_url;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<IgMedia> getMedia() {
        return media;
    }

    public void setMedia(List<IgMedia> media) {
        this.media = media;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }
}
