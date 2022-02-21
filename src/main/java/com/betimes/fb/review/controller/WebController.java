package com.betimes.fb.review.controller;

import com.betimes.fb.review.model.*;
import com.betimes.fb.review.service.FacebookService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class WebController {
    @Value("${url.path}")
    private String url;

    @Autowired
    private FacebookService facebookService;

    @RequestMapping(value = {"/index", "/"})
    public String index(HttpServletRequest request, Model model) {
        String sessionId = getCookie(request, "session_id");
        String userId = getCookie(request, "_a7u");
        String token = getCookie(request, "_hz9");

        if (sessionId.equals("") && userId.equals("")) return "redirect:" + url + "/_login";

        FbUserProfile userProfile = facebookService.getUserProfile(userId, token);
        System.out.println(userProfile);
        model.addAttribute("userProfile", userProfile);

        List<PageInfo> pageInfoList = facebookService.getAllPages(userId, token);
        //List<PageInfo> pageInfoList = new ArrayList<>();
        model.addAttribute("pageList", pageInfoList);

        return "index";
    }

    @RequestMapping(value = "/_login")
    public String login(HttpServletRequest request) {
        return "login";
    }

    @RequestMapping(value = {"/search"})
    public String search(HttpServletRequest request, Model model) {
        String sessionId = getCookie(request, "session_id");
        String userId = getCookie(request, "_a7u");
        String token = getCookie(request, "_hz9");

        if (sessionId.equals("") && userId.equals("")) return "redirect:" + url + "/_login";

        FbUserProfile userProfile = facebookService.getUserProfile(userId, token);
        model.addAttribute("userProfile", userProfile);

        String searchKey = request.getParameter("q");

        SearchResponse searchResponse = new SearchResponse();
        if (searchKey != null && !searchKey.equals("")) {
            List<SearchResult> searchResultList = facebookService.searchByName(searchKey, userId, token);
            searchResponse.setQ(searchKey);
            searchResponse.setProfileList(searchResultList);
        }

        model.addAttribute("searchResponse", searchResponse);

        return "search";
    }

    @RequestMapping(value = "discovery")
    public String discovery(HttpServletRequest request, Model model) {
        String sessionId = getCookie(request, "session_id");
        String userId = getCookie(request, "_a7u");
        String token = getCookie(request, "_hz9");

        if (sessionId.equals("") || userId.equals("")) return "redirect:" + url + "/_login";

        FbUserProfile userProfile = facebookService.getUserProfile(userId, token);
        model.addAttribute("userProfile", userProfile);

        String pageId = request.getParameter("q");

        if (pageId == null || pageId.equals("")) return "redirect:" + url;
        PageInfo pageInfo = facebookService.getPageInfo(pageId, token);
        if (pageInfo.getId() == null || pageInfo.getId().equals("")) return "redirect:" + url;
        model.addAttribute("pageInfo", pageInfo);
        List<PostData> postDataList = facebookService.getPagePosts(pageId, token);
        model.addAttribute("postList", postDataList);

        return "discovery";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String authentication(HttpServletResponse response, @Valid @RequestBody FacebookRequest request) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put("status", false);
        if (request.getAuthResponse().getAccessToken() != null) {
            obj.put("status", true);
            UUID uuid = UUID.randomUUID();
            Cookie cSessionId = new Cookie("session_id", uuid.toString());
            Cookie cUserId = new Cookie("_a7u", request.getId());
            Cookie cToken = new Cookie("_hz9", request.getAuthResponse().getAccessToken());

            response.addCookie(cSessionId);
            response.addCookie(cUserId);
            response.addCookie(cToken);
        }

        System.out.println(obj);

        return obj.toString();
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                cookie.setValue(null);
                response.addCookie(cookie);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:" + url;
    }

    @RequestMapping("/privacy")
    public String privacy(HttpServletRequest request) {
        return "privacy";
    }

    private String getCookie(HttpServletRequest request, String key) {
        String ret = "";

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(key)) ret = cookie.getValue();
            }
        }

        return ret;
    }
}
