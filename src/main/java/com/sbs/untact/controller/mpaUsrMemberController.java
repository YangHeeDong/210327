package com.sbs.untact.controller;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.dto.Rq;
import com.sbs.untact.service.MemberService;
import com.sbs.untact.util.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class mpaUsrMemberController {
    @Autowired
    private MemberService memberService;
    
    @RequestMapping("/mpaUsr/member/checkPassword")
    public String showCheckPassword(HttpServletRequest req) {
    	return "/mpaUsr/member/checkPassword";
    }
    
    @RequestMapping("/mpaUsr/member/doCheckPassword")
    public String doCheckPassword(HttpServletRequest req,int id,String loginPw,String redirectUri) {
    	
    	Member member = memberService.getMemberById(id);
    	
    	if(member.getLoginPw().equals(loginPw)==false) {
    		return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다");
    	}
    	
    	String authCode = memberService.genCheckPasswordAuthCode(member.getId());
    	
    	redirectUri = Util.getNewUri(redirectUri, "checkPasswordAuthCode", authCode);
    	
    	return Util.msgAndReplace(req, "비밀번호가 확인 되었습니다.", redirectUri);
    }
    
    @RequestMapping("/mpaUsr/member/modify")
    public String showModify(HttpServletRequest req, String checkPasswordAuthCode ) {
    	Member loginedMember = ((Rq)req.getAttribute("rq")).getLoginedMember();
    	
    	ResultData checkValidCheckPasswordAuthCodeResultData = memberService
    			.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);
    	
    	if(checkValidCheckPasswordAuthCodeResultData.isFail()) {
    		return Util.msgAndBack(req, checkValidCheckPasswordAuthCodeResultData.getMsg());
    	}
    	
    	return "/mpaUsr/member/modify";
    }
    
    @RequestMapping("/mpaUsr/member/doModify")
    public String doModify(HttpServletRequest req, String loginPw, String name, String nickname, String cellphoneNo, String email, String checkPasswordAuthCode) {
    	
    	Member loginedMember = ((Rq)req.getAttribute("rq")).getLoginedMember();
    	
    	ResultData checkValidCheckPasswordAuthCodeResultData = memberService
    			.checkValidCheckPasswordAuthCode(loginedMember.getId(), checkPasswordAuthCode);
    	
    	if(checkValidCheckPasswordAuthCodeResultData.isFail()) {
    		return Util.msgAndBack(req, checkValidCheckPasswordAuthCodeResultData.getMsg());
    	}
    	
    	int id = ((Rq)req.getAttribute("rq")).getLoginedMemberId();
    	
    	if(loginPw != null && loginPw.length()==0) {
    		loginPw = null;
    	}

        ResultData modifyRd = memberService.doModify(id, loginPw, name, nickname, cellphoneNo, email);

        if (modifyRd.isFail()) {
            return Util.msgAndBack(req, modifyRd.getMsg());
        }

        return Util.msgAndReplace(req, modifyRd.getMsg(), "/");
    }
    
    @RequestMapping("/mpaUsr/member/mypage")
    public String showMypage(HttpServletRequest req) {
    	return "/mpaUsr/member/mypage";
    }
    
    @RequestMapping("/mpaUsr/member/findLoginId")
    public String showFindLoginId(HttpServletRequest req) {
        return "/mpaUsr/member/findLoginId";
    }
    
    @RequestMapping("/mpaUsr/member/findLoginPw")
    public String showFindLoginPw(HttpServletRequest req) {
        return "mpaUsr/member/findLoginPw";
    }

    @RequestMapping("/mpaUsr/member/doFindLoginPw")
    public String doFindLoginPw(HttpServletRequest req, String loginId, String name, String email, String redirectUri) {
        if (Util.isEmpty(redirectUri)) {
            redirectUri = "/";
        }

        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        if (member.getName().equals(name) == false) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        if (member.getEmail().equals(email) == false) {
            return Util.msgAndBack(req, "일치하는 회원이 존재하지 않습니다.");
        }

        ResultData notifyTempLoginPwByEmailRs = memberService.notifyTempLoginPwByEmail(member);

        return Util.msgAndReplace(req, notifyTempLoginPwByEmailRs.getMsg(), redirectUri);
    }
    
    @RequestMapping("/mpaUsr/member/doFindLoginId")
    public String doFindLoginId(HttpServletRequest req,String name, String email,String redirectUri) {
    	if ( Util.isEmpty(redirectUri) ) {
            redirectUri = "/";
        }
    	
    	Member member = memberService.getFindLoginIdByNameAndEmail(name,email);
    	
    	if(member==null) {
    		return Util.msgAndBack(req, "일치하는 회원의 정보가 없습니다.");
    	}
    	
        return Util.msgAndReplace(req, "회원님의 아이디는 "+member.getLoginId()+" 입니다.", redirectUri);
    }

    @RequestMapping("/mpaUsr/member/login")
    public String showLogin(HttpServletRequest req) {
        return "mpaUsr/member/login";
    }

    @RequestMapping("/mpaUsr/member/doLogin")
    public String doLogin(HttpServletRequest req, HttpSession session, String loginId, String loginPw, String redirectUri) {
        if ( Util.isEmpty(redirectUri) ) {
            redirectUri = "/";
        }

        Member member = memberService.getMemberByLoginId(loginId);

        if (member == null) {
            return Util.msgAndBack(req, loginId + "(은)는 존재하지 않는 로그인아이디 입니다.");
        }

        if (member.getLoginPw().equals(loginPw) == false) {
            return Util.msgAndBack(req, "비밀번호가 일치하지 않습니다.");
        }

        //HttpSession session = req.getSession();
        session.setAttribute("loginedMemberId", member.getId());
        boolean usingTempPassword = memberService.usingTempPassword(member.getId());
        boolean needToChangPassword = memberService.needToChangPassword(member.getId());
        
        if(needToChangPassword) {
        	return Util.msgAndReplace(req, "현재 비밀번호를 사용한지 "+memberService.getNeedToChangePasswordFreeDays()+"일이 지났습니다. 비밀번호를 변경해 주세요", "/mpaUsr/member/mypage");
        }
        
        if(usingTempPassword) {
        	return Util.msgAndReplace(req, "임시 비밀번호를 사용중입니다 비밀번호를 변경해 주세요", "/mpaUsr/member/mypage");
        }
        
        String msg = "환영합니다.";
        return Util.msgAndReplace(req, msg, redirectUri);
    }

    @RequestMapping("/mpaUsr/member/join")
    public String showJoin(HttpServletRequest req) {
        return "mpaUsr/member/join";
    }

    @RequestMapping("/mpaUsr/member/doJoin")
    public String doJoin(HttpServletRequest req, String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
        Member oldMember = memberService.getMemberByLoginId(loginId);

        if (oldMember != null) {
            return Util.msgAndBack(req, loginId + "(은)는 이미 사용중인 로그인아이디 입니다.");
        }

        ResultData joinRd = memberService.join(loginId, loginPw, name, nickname, cellphoneNo, email);

        if (joinRd.isFail()) {
            return Util.msgAndBack(req, joinRd.getMsg());
        }

        return Util.msgAndReplace(req, joinRd.getMsg(), "/");
    }
    
    @RequestMapping("/mpaUsr/member/doLogout")
    public String doLogout(HttpServletRequest req, HttpSession session) {
        session.removeAttribute("loginedMemberId");

        String msg = "로그아웃 되었습니다.";
        return Util.msgAndReplace(req, msg, "/");
    }
}