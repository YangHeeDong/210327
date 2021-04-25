package com.sbs.untact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.MemberDao;
import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.util.Util;

@Service
public class MemberService {
	
	@Autowired
    private MailService mailService;

    @Value("${custom.siteMainUri}")
    private String siteMainUri;
    @Value("${custom.siteName}")
    private String siteName;
	
	@Autowired
	MemberDao memberDao;
	
    public ResultData notifyTempLoginPwByEmail(Member actor) {
        String title = "[" + siteName + "] 임시 패스워드 발송";
        String tempPassword = Util.getTempPassword(6);
        String body = "<h1>임시 패스워드 : " + tempPassword + "</h1>";
        body += "<a href=\"" + siteMainUri + "/mpaUsr/member/login\" target=\"_blank\">로그인 하러가기</a>";

        ResultData sendResultData = mailService.send(actor.getEmail(), title, body);

        if (sendResultData.isFail()) {
            return sendResultData;
        }
        
        tempPassword = Util.sha256(tempPassword);
        
        setTempPassword(actor, tempPassword);

        return new ResultData("S-1", "계정의 이메일주소로 임시 패스워드가 발송되었습니다.");
    }

    private void setTempPassword(Member actor, String tempPassword) {
        memberDao.modify(actor.getId(), tempPassword, null, null, null, null);
    }

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

	public Member getFindLoginIdByNameAndEmail(String name, String email) {
		
		return memberDao.getFindLoginIdByNameAndEmail(name,email);
	}

	public ResultData doModify(int id, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		
		memberDao.modify(id, loginPw, name, nickname, cellphoneNo, email);
		
		return new ResultData("S-1", "수정하였습니다.");
	}

}
