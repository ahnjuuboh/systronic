package com.betimes.fb.review.service;

import com.betimes.fb.review.external.Json;
import com.betimes.fb.review.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component("instagramService")
public class InstagramService {
    @Value("${graph.user.account}")
    private String userAccountUrl;

    @Value("${graph.user.instagram}")
    private String userInstagramUrl;

    @Value("${graph.user.info}")
    private String userInfoUrl;

    @Value("${graph.instagram.media}")
    private String instagramMediaUrl;

    @Value("${graph.ig.user.insight.url}")
    private String userInsightUrl;

    @Value("${graph.ig.user.audience.url}")
    private String userAudienceUrl;

    @Autowired
    private Json json;

    public List<IgUserInfo> getInstagramProfile(String id, String token) {
        List<IgUserInfo> userInfoList = new ArrayList<>();
        try {
            String accountPageUrl = this.userAccountUrl.replaceAll("@token", token);
            JSONObject response = json.readJsonFromUrl(accountPageUrl);
            JSONArray pages = response.getJSONArray("data");

            for (int i = 0; i < pages.length(); i++) {
                String pageId = pages.getJSONObject(i).getString("id");
                String pageToken = pages.getJSONObject(i).getString("access_token");

                String instagramUrl = this.userInstagramUrl.replaceAll("@id", pageId)
                        .replaceAll("@token", pageToken);

                JSONObject pageBusinessAccount = json.readJsonFromUrl(instagramUrl);
                if (!pageBusinessAccount.isNull("instagram_business_account")) {
                    String instagramId = pageBusinessAccount.getJSONObject("instagram_business_account").getString("id");

                    String infoUrl = this.userInfoUrl.replaceAll("@id", instagramId)
                            .replaceAll("@token", token);
                    JSONObject userInfoResponse = json.readJsonFromUrl(infoUrl);

                    IgUserInfo userInfo = new IgUserInfo();
                    userInfo.setId(userInfoResponse.getString("id"));
                    userInfo.setName(userInfoResponse.getString("name"));
                    userInfo.setUsername(userInfoResponse.getString("username"));
                    userInfo.setMedia_count(userInfoResponse.getInt("media_count"));
                    userInfo.setFollowers_count(userInfoResponse.getInt("followers_count"));
                    userInfo.setFollows_count(userInfoResponse.getInt("follows_count"));
                    userInfo.setBiography(userInfoResponse.isNull("biography") ? null : userInfoResponse.getString("biography"));
                    userInfo.setProfile_picture_url(userInfoResponse.isNull("profile_picture_url") ? null : userInfoResponse.getString("profile_picture_url"));
                    userInfo.setWebsite(userInfoResponse.isNull("website") ? null : userInfoResponse.getString("website"));


                    String mediaUrl = this.instagramMediaUrl.replaceAll("@id", instagramId)
                            .replaceAll("@token", token);

                    JSONObject mediaResponse = json.readJsonFromUrl(mediaUrl);
                    JSONArray medialArr = mediaResponse.getJSONArray("data");

                    List<IgMedia> mediaList = new ArrayList<>();
                    for (int j = 0; j < medialArr.length(); j++) {
                        JSONObject m = medialArr.getJSONObject(j);
                        IgMedia media = new IgMedia();
                        media.setId(m.getString("id"));
                        media.setCaption(m.isNull("caption") ? null : m.getString("caption"));
                        media.setComments_count(m.getInt("comments_count"));
                        media.setLike_count(m.getInt("like_count"));
                        media.setMedia_type(m.getString("media_type"));
                        media.setMedia_url(m.getString("media_url"));
                        media.setPermalink(m.getString("permalink"));
                        media.setTimestamp(m.getString("timestamp"));

                        mediaList.add(media);
                    }

                    userInfo.setMedia(mediaList);

                    Audience audience = this.getAudience(instagramId, token);
                    userInfo.setAudience(audience);

                    userInfoList.add(userInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  userInfoList;
    }

    public Audience getAudience(String id, String token) {
        Audience audience = new Audience();
        String url = this.userAudienceUrl.replaceAll("@id", id).replaceAll("@token", token);
        try {
            JSONObject response = json.readJsonFromUrl(url);
            JSONArray dataAry = response.getJSONArray("data");

            for (int i = 0; i < dataAry.length(); i++) {
                JSONObject data = dataAry.getJSONObject(i);
                String dataType = data.getString("name");
                JSONObject values = data.getJSONArray("values").getJSONObject(0).getJSONObject("value");
                if (dataType.equals("audience_city")) {
                    List<AudienceCity> audienceCities = new ArrayList<>();

                    Iterator<?> keys = values.keys();
                    AudienceCity audienceCity;
                    while(keys.hasNext()) {
                        String key = (String) keys.next();
                        audienceCity = new AudienceCity(key, values.optInt(key));
                        audienceCities.add(audienceCity);
                    }
                    audienceCity = null;
                    audienceCities = audienceCities.stream()
                            .sorted(Comparator.comparing(AudienceCity::getValue).reversed())
                            .collect(Collectors.toList());
//                    audienceCities = audienceCities.subList()
                    audience.setAudienceCityList(audienceCities);
                }

                if (dataType.equals("audience_gender_age")) {
                    List<AudienceGenderAge> audienceGenderAges = new ArrayList<>();

                    Iterator<?> keys = values.keys();
                    AudienceGenderAge audienceGenderAge;
                    Integer male = 0;
                    Integer female = 0;
                    while(keys.hasNext()) {
                        String key = (String) keys.next();

                        audienceGenderAge = new AudienceGenderAge(key, values.optInt(key));
                        if (key.startsWith("M.")) male += audienceGenderAge.getValue();
                        if (key.startsWith("F.")) female += audienceGenderAge.getValue();
                        audienceGenderAges.add(audienceGenderAge);
                    }
                    audienceGenderAge = null;
                    audienceGenderAges = audienceGenderAges.stream()
                            .sorted(Comparator.comparing(AudienceGenderAge::getValue).reversed())
                            .collect(Collectors.toList());


                    int[] maleAge = { values.getInt("M.13-17"), values.getInt("M.18-24"), values.getInt("M.25-34"),
                            values.getInt("M.35-44"), values.getInt("M.45-54"), values.getInt("M.55-64"),
                            values.getInt("M.65+")};
                    int[] femaleAge = { values.getInt("F.13-17"), values.getInt("F.18-24"), values.getInt("F.25-34"),
                            values.getInt("F.35-44"), values.getInt("F.45-54"), values.getInt("F.55-64"),
                            values.getInt("F.65+")};

                    double totalAudience = (double)male + (double)female;

                    int malePercent = (int)Math.round((male / totalAudience) * 100);
                    int femalePercent = (int)Math.round((female / totalAudience) * 100);

                    audience.setMale(malePercent + "%");
                    audience.setFemale(femalePercent + "%");
                    audience.setAudienceGenderAgeList(audienceGenderAges);
                    audience.setMaleAge(maleAge);
                    audience.setFemaleAge(femaleAge);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return audience;
    }
}
