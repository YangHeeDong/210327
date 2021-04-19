package com.sbs.untact.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.MemberService;
import com.sbs.untact.util.Util;

@Controller
public class mpaUsrMemberController {
	
	@Autowired
	MemberService memberService;
	
	@RequestMapping("/mpaUsr/member/login")
	public String showLogin() {
		return("mpaUsr/member/login");
	}
	
	@RequestMapping("/mpaUsr/member/doLogin")
	public String doLogin(HttpServletRequest req,HttpSession session, String loginId,String loginPw ) {
		Member member = memberService.getMemberByLoginId(loginId); 
		
		if(member == null ) {
			return Util.msgAndBack(req, "없는 아이디 입니다.");
		}
		if(member.getLoginPw().equals(loginPw)==false) {
			return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
		}
		session.setAttribute("loginedMemberId", member.getId());
		return Util.msgAndReplace(req, member.getNickname()+"님 환영합니다!", "/") ;
	}
	
	@RequestMapping("/mpaUsr/member/doLogout")
	public String doLogout(HttpServletRequest req,HttpSession session) {
		session.removeAttribute("loginedMemberId");
		return Util.msgAndReplace(req, "로그아웃 되었습니다.", "/") ;
	}
	
	@RequestMapping("/mpaUsr/member/join")
	public String showJoin() {
		return("mpaUsr/member/join");
	}
	
	@RequestMapping("/mpaUsr/member/doJoin")
	public String doJoin(HttpServletRequest req, String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		
		if(Util.isEmpty(loginId)) {
			return Util.msgAndBack(req, "아이디를 입력해 주세요");
		}
		if(Util.isEmpty(loginPw)) {
			return Util.msgAndBack(req, "비밀번호를 입력해 주세요");
		}
		if(Util.isEmpty(name)) {
			return Util.msgAndBack(req, "이름을 입력해 주세요");
		}
		if(Util.isEmpty(nickname)) {
			return Util.msgAndBack(req, "별명을 입력해 주세요");
		}
		if(Util.isEmpty(cellphoneNo)) {
			return Util.msgAndBack(req, "전화번호를 입력해 주세요");
		}
		if(Util.isEmpty(email)) {
			return Util.msgAndBack(req, "이메일을 입력해 주세요");
		}
		
		Member oldMember = memberService.getMemberByLoginId(loginId);
		
		if(oldMember != null) {
			return Util.msgAndBack(req, "중복된 아이디 입니다.");
		}
		
		ResultData rd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		
		if (rd.isFail()) {
            return Util.msgAndBack(req, rd.getMsg());
        }
		
		return Util.msgAndReplace(req, rd.getMsg(), "/") ;
	}
}
