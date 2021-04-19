package com.sbs.untact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Member {
	int id;
    String regDate;
    String updateDate;
    String loginId;
    String loginPw;
    String name;
    String nickname;
    String email;
    int cellphoneNo;
    boolean delStatus;
    String delDate;
}
