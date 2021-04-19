package com.sbs.untact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.MemberDao;
import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;

@Service
public class MemberService {
	
	@Autowired
	MemberDao memberDao;

	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		
		memberDao.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		
		int id = memberDao.getMemberLastInsertId();
		return new ResultData("S-1", "회원가입을 성공하였습니다.","id",id);
	}

	public Member getMemberByLoginId(String loginId) {
		return memberDao.getMemberByLoginId(loginId);
	}
	
	public int getMemberLastInsertId() {
		return memberDao.getMemberLastInsertId();
	}

	public Member getMemberById(int id) {
		return memberDao.getMemberById(id);
	}

}
