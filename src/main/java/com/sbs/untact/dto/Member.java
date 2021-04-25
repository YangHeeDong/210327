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
	private int cellphoneNo;
	private boolean delStatus;
	private String delDate;
    
    public String getAuthLevelName() {
    	return "일반회원";
    }
}
