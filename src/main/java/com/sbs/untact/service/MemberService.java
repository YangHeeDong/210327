package com.sbs.untact.service;

import java.util.UUID;

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
	@Autowired
    private AttrService attrService;

    @Value("${custom.siteMainUri}")
    private String siteMainUri;
    @Value("${custom.siteName}")
    private String siteName;
    @Value("${custom.needToChangePasswordFreeDays}")
    private int needToChangePasswordFreeDays;
	
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
    	
    	attrService.setValue("member",actor.getId() , "extra", "useTempPassword", "1", null);
    	
        memberDao.modify(actor.getId(), tempPassword, null, null, null, null);
    }

	public ResultData join(String loginId, String loginPw, String name, String nickname, String cellphoneNo, String email) {
		
		memberDao.join(loginId, loginPw, name, nickname, cellphoneNo, email);
		
		int id = memberDao.getMemberLastInsertId();
		
		setNeedToChangePasswordLater(id);
		
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
		
		if(loginPw!=null) {
			attrService.remove("member",id , "extra", "useTempPassword");
			setNeedToChangePasswordLater(id);
		}
		
		return new ResultData("S-1", "수정하였습니다.");
	}

	public ResultData checkValidCheckPasswordAuthCode(int id, String checkPasswordAuthCode) {
		if(attrService.getValue("member", id, "extra", "checkPasswordAuthCode").equals(checkPasswordAuthCode)) {
			return new ResultData("S-1","유효한 키 입니다.");
		}
		return new ResultData("F-1","유효하지 않은 키입니다.");
	}

	public String genCheckPasswordAuthCode(int id) {
		String AuthCode = UUID.randomUUID().toString();
		String expireDate = Util.getDateStrLater(60*60);
		attrService.setValue("member", id, "extra", "checkPasswordAuthCode", AuthCode,expireDate);
		return AuthCode;
	}

	public boolean usingTempPassword(int id) {
		return attrService.getValue("member", id, "extra", "useTempPassword").equals("1");
	}

	public boolean needToChangPassword(int id) {
		return attrService.getValue("member", id, "extra", "needToChangePassword").equals("0")==false;
	}
	
	private void setNeedToChangePasswordLater(int id) {
		attrService.setValue("member", id, "extra", "needToChangePassword", "0", Util.getDateStrLater(60*60*24*needToChangePasswordFreeDays));
	}

	public int getNeedToChangePasswordFreeDays() {
		return needToChangePasswordFreeDays;
	}

}
