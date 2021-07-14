package com.sbs.untact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Member {
	private int id;
	private String regDate;
	private String updateDate;
	private String loginId;
	private String loginPw;
	private String name;
	private String nickname;
	private String email;
	private String cellphoneNo;
	private boolean delStatus;
	private String delDate;
    
    public String getAuthLevelName() {
    	return "일반회원";
    }
    
    public String getProfileImgUri() {
        return "/common/genFile/file/member/" + id + "/extra/profileImg/1";
    }

    public String getProfileFallbackImgUri() {
        return "https://via.placeholder.com/300?text=^_^";
    }

    public String getProfileFallbackImgOnErrorHtmlAttr() {
        return "this.src = '" + getProfileFallbackImgUri() + "'";
    }
    
    public String getRemoveProfileImgIfNotExistsOnErrorHtmlAttr() {
        return "$(this).remove();";
    }
    
}
