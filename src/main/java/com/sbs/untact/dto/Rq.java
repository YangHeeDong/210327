package com.sbs.untact.dto;

import java.util.Map;

import com.sbs.untact.util.Util;

import lombok.Getter;

public class Rq {
    private String currentUrl;
    private String currentUri;
    private Member loginedMember;
    private Map<String, String> paramMap;
    @Getter
    private boolean needToChangPassword;

    public Rq(Member loginedMember, String currentUri,Map<String, String> paramMap,boolean needToChangPassword) {
        this.loginedMember = loginedMember;
        this.currentUrl = currentUri.split("\\?")[0];
        this.currentUri = currentUri;
        this.paramMap = paramMap;
        this.needToChangPassword = needToChangPassword;
    }

    public boolean isLogined() {
        return loginedMember != null;
    }

    public boolean isNotLogined() {
        return isLogined() == false;
    }

    public int getLoginedMemberId() {
        if (isNotLogined()) return 0;

        return loginedMember.getId();
    }

    public Member getLoginedMember() {
        return loginedMember;
    }

    public String getLoginedMemberNickname() {
        if (isNotLogined()) return "";

        return loginedMember.getNickname();
    }

    public String getEncodedCurrentUri() {
        return Util.getUriEncoded(getCurrentUri());
    }

    private String getCurrentUri() {
        return currentUri;
    }

    public String getLoginPageUri() {
        String afterLoginUri;

        if (isLoginPage()) {
            afterLoginUri = Util.getUriEncoded(paramMap.get("afterLoginUri"));
        } else {
            afterLoginUri = getEncodedCurrentUri();
        }

        return "../member/login?afterLoginUri=" + afterLoginUri;
    }

    private boolean isLoginPage() {
        return currentUrl.equals("/mpaUsr/member/login");
    }
}