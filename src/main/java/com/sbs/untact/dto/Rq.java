package com.sbs.untact.dto;

import java.util.Map;

import com.sbs.untact.util.Util;

import lombok.Getter;

public class Rq {
	@Getter
	private boolean isAjax;
    private String currentUrl;
    private String currentUri;
    private Member loginedMember;
    private Map<String, String> paramMap;
    @Getter
    private boolean needToChangPassword;

    public Rq(boolean isAjax, Member loginedMember, String currentUri,Map<String, String> paramMap,boolean needToChangPassword) {
        this.isAjax = isAjax;
    	this.loginedMember = loginedMember;
        this.currentUrl = currentUri.split("\\?")[0];
        this.currentUri = currentUri;
        this.paramMap = paramMap;
        this.needToChangPassword = needToChangPassword;
    }
    
    public String getParamJsonStr() {
    	return Util.toJsonStr(paramMap);
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

    public String getCurrentUri() {
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