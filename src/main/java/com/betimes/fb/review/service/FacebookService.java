package com.betimes.fb.review.service;

import com.betimes.fb.review.external.Json;
import com.betimes.fb.review.model.*;
import com.betimes.fb.review.util.FormatUtil;
import com.betimes.fb.review.util.JsonUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component("facebookService")
public class FacebookService {
    @Value("${graph.user.url}")
    private String userUrl;

    @Value("${graph.user.account}")
    private String userAccountUrl;

    @Value("${graph.page.post.url}")
    private String pagePostUrl;

    @Value("${graph.post.url}")
    private String postUrl;

    @Value("${graph.page.info}")
    private String pageInfoUrl;

    @Autowired
    private Json json;

    public FbUserProfile getUserProfile(String id, String token) {
        FbUserProfile fbUserProfile = new FbUserProfile();
        try {
            String url = userUrl.replaceAll("@token", token);
            JSONObject response = json.readJsonFromUrl(url);

            fbUserProfile.setId(id);
            fbUserProfile.setName(JsonUtil.getString(response, "name"));
//            fbUserProfile.setUsername(response.isNull("username") ? null : response.getString("username"));
            fbUserProfile.setEmail(JsonUtil.getString(response, "email"));
            fbUserProfile.setGender(JsonUtil.getString(response, "gender"));
            fbUserProfile.setBirthDate(JsonUtil.getString(response, "birthday"));
            fbUserProfile.setLink(JsonUtil.getString(response, "link"));
            fbUserProfile.setImage(JsonUtil.getString(response.getJSONObject("picture").getJSONObject("data"), "url"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return fbUserProfile;
    }

    public List<SearchResult> searchByName(String q, String id, String token) {
        List<SearchResult> searchResultList = new ArrayList<>();

        try {
            String url = userAccountUrl.replaceAll("@id", id).replaceAll("@token", token);
            JSONObject responseData = json.readJsonFromUrl(url);
            JSONArray dataArr = responseData.getJSONArray("data");

            for (int i = 0; i < dataArr.length(); i++) {
                String pageId = dataArr.getJSONObject(i).getString("id");
                String pageToken = dataArr.getJSONObject(i).getString("access_token");
                String urlPage = pageInfoUrl.replaceAll("@id", pageId).replaceAll("@token", pageToken);
                JSONObject response = json.readJsonFromUrl(urlPage);

                if (response != null) {
                    SearchResult searchResult = new SearchResult();
                    searchResult.setId(JsonUtil.getString(response, "id"));
                    searchResult.setName(JsonUtil.getString(response, "name"));
                    searchResult.setUsername(JsonUtil.getString(response, "username"));
                    searchResult.setAbout(JsonUtil.getString(response, "about"));
                    searchResult.setFans(FormatUtil.format(JsonUtil.getLong(response, "fan_count")));

                    PictureData pictureData = new PictureData();
                    pictureData.setUrl(JsonUtil.getString(response.getJSONObject("picture").getJSONObject("data"), "url"));
                    pictureData.setWidth(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "width").intValue());
                    pictureData.setHeight(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "height").intValue());
                    pictureData.setIs_silhouette(response.getJSONObject("picture").getJSONObject("data").getBoolean("is_silhouette"));

                    Picture picture = new Picture();
                    picture.setData(pictureData);

                    searchResult.setPicture(picture);

                    searchResultList.add(searchResult);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  searchResultList;
    }

    public List<PageInfo> getAllPages(String id, String token) {
        List<PageInfo> pageInfoList = new ArrayList<>();

        try {
            String url = userAccountUrl.replaceAll("@id", id).replaceAll("@token", token);
            JSONObject responseData = json.readJsonFromUrl(url);
            JSONArray dataArr = responseData.getJSONArray("data");

            for (int i = 0; i < dataArr.length(); i++) {
                String pageId = dataArr.getJSONObject(i).getString("id");
                String pageToken = dataArr.getJSONObject(i).getString("access_token");
                PageInfo pageInfo = getPageInfo(pageId, pageToken);
                pageInfoList.add(pageInfo);
//                String urlPage = pageInfoUrl.replaceAll("@id", pageId).replaceAll("@token", pageToken);
//                JSONObject response = json.readJsonFromUrl(urlPage);
//
//                if (response != null) {
//                    PageInfo pageInfo = new PageInfo();
//                    pageInfo.setId(JsonUtil.getString(response, "id"));
//                    pageInfo.setName(JsonUtil.getString(response, "name"));
//                    pageInfo.setUsername(JsonUtil.getString(response, "username"));
//                    pageInfo.setAbout(JsonUtil.getString(response, "about"));
//                    pageInfo.setFans(FormatUtil.format(JsonUtil.getLong(response, "fan_count")));
//                    pageInfo.setCategory(JsonUtil.getString(response, "category"));
//                    pageInfo.setLink(JsonUtil.getString(response, "link"));
//
//                    PictureData pictureData = new PictureData();
//                    pictureData.setUrl(JsonUtil.getString(response.getJSONObject("picture").getJSONObject("data"), "url"));
//                    pictureData.setWidth(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "width").intValue());
//                    pictureData.setHeight(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "height").intValue());
//                    pictureData.setIs_silhouette(response.getJSONObject("picture").getJSONObject("data").getBoolean("is_silhouette"));
//
//                    Picture picture = new Picture();
//                    picture.setData(pictureData);
//
//                    pageInfo.setPicture(picture);
//
//                    pageInfoList.add(pageInfo);
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pageInfoList;
    }

    public List<PostData> getPagePosts(String id, String token) {
        List<PostData> postList = new ArrayList<>();

        try {
            String url = pagePostUrl.replaceAll("@id", id).replaceAll("@token", token);
            System.out.println(url);
            JSONObject responseData = json.readJsonFromUrl(url);
            JSONArray dataArr = responseData.getJSONArray("data");

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            for (int i = 0; i < dataArr.length(); i++) {
                String postId = dataArr.getJSONObject(i).getString("id");
                String urlPost = postUrl.replaceAll("@id", postId).replaceAll("@token", token);
                JSONObject response = json.readJsonFromUrl(urlPost);

                if (response != null) {
                    PostData post = new PostData();
                    post.setId(postId);
                    post.setMessage(JsonUtil.getString(response, "message"));

                    if (!response.isNull("from")) {
                        post.setPageId(JsonUtil.getString(response.getJSONObject("from"), "id"));
                    } else {
                        String ref = postId.split("_")[0];
                        post.setPageId(ref);
                    }

                    if (!response.isNull("actions")) {
                        if (response.getJSONArray("actions").getJSONObject(0).getString("name").equals("Share")) {
                            post.setActions(JsonUtil.getString(response.getJSONArray("actions").getJSONObject(0), "name"));
                        }
                    }

                    if (!response.isNull("attachments")) {
                        post.setMediaType(JsonUtil.getString(response.getJSONObject("attachments").getJSONArray("data")
                                .getJSONObject(0),"media_type"));
                        if (!response.getJSONObject("attachments").getJSONArray("data")
                                .getJSONObject(0).isNull("url")) {
                            post.setLink(JsonUtil.getString(response.getJSONObject("attachments").getJSONArray("data")
                                    .getJSONObject(0), "url"));
                        }

                        if (!response.getJSONObject("attachments").getJSONArray("data")
                                .getJSONObject(0).isNull("media")) {
                            if (!response.getJSONObject("attachments").getJSONArray("data")
                                    .getJSONObject(0).getJSONObject("media").isNull("source")) {
                                post.setSource(JsonUtil.getString(response.getJSONObject("attachments").getJSONArray("data")
                                        .getJSONObject(0).getJSONObject("media"),"source"));
                            }
                        }
                    }

                    post.setPicture(JsonUtil.getString(response, "full_picture"));
                    post.setPermalinkUrl(JsonUtil.getString(response, "permalink_url"));

                    String createdTimeStr = JsonUtil.getString(response, "created_time");
                    Date createdTime = formatter.parse(createdTimeStr);
                    post.setCreatedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(createdTime));

                    String updatedTimeStr = JsonUtil.getString(response, "updated_time");
                    Date updatedTime = formatter.parse(updatedTimeStr);
                    post.setUpdatedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(updatedTime));

                    if (!response.isNull("reactions_like")) {
                        post.setLike(response.getJSONObject("reactions_like").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("reactions_love")) {
                        post.setLove(response.getJSONObject("reactions_love").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("reactions_wow")) {
                        post.setWow(response.getJSONObject("reactions_wow").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("reactions_haha")) {
                        post.setHaha(response.getJSONObject("reactions_haha").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("reactions_sad")) {
                        post.setSorry(response.getJSONObject("reactions_sad").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("reactions_angry")) {
                        post.setAnger(response.getJSONObject("reactions_angry").getJSONObject("summary").getInt("total_count"));
                    }

                    if (!response.isNull("shares")) {
                        post.setShares(response.getJSONObject("shares").getInt("count"));
                    }

                    if (!response.isNull("comments")) {
                        post.setComments(response.getJSONObject("comments").getJSONObject("summary").getInt("total_count"));
                    }

                    postList.add(post);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postList;
    }

    public PageInfo getPageInfo(String id, String token) {
        PageInfo pageInfo = new PageInfo();

        try {
            String url = pageInfoUrl.replaceAll("@id", id).replaceAll("@token", token);
            JSONObject response = json.readJsonFromUrl(url);

            if (response != null) {
                pageInfo.setId(JsonUtil.getString(response, "id"));
                pageInfo.setName(JsonUtil.getString(response, "name"));
                pageInfo.setUsername(JsonUtil.getString(response, "username"));
                pageInfo.setAbout(JsonUtil.getString(response, "about"));
                pageInfo.setFans(FormatUtil.format(JsonUtil.getLong(response, "fan_count")));
                pageInfo.setCategory(JsonUtil.getString(response, "category"));
                pageInfo.setLink(JsonUtil.getString(response, "link"));

                PictureData pictureData = new PictureData();
                pictureData.setUrl(JsonUtil.getString(response.getJSONObject("picture").getJSONObject("data"), "url"));
                pictureData.setWidth(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "width").intValue());
                pictureData.setHeight(JsonUtil.getLong(response.getJSONObject("picture").getJSONObject("data"), "height").intValue());
                pictureData.setIs_silhouette(response.getJSONObject("picture").getJSONObject("data").getBoolean("is_silhouette"));

                Picture picture = new Picture();
                picture.setData(pictureData);

                pageInfo.setPicture(picture);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pageInfo;
    }
}
