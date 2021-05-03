package com.sbs.untact.interceptor;

import com.sbs.untact.dto.Member;
import com.sbs.untact.dto.Rq;
import com.sbs.untact.service.MemberService;
import com.sbs.untact.util.Util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class BeforeActionInterceptor implements HandlerInterceptor {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        
    	Map<String, String> paramMap = Util.getParamMap(req);
    	
    	HttpSession session = req.getSession();

        Member loginedMember = null;
        int loginedMemberId = 0;

        if (session.getAttribute("loginedMemberId") != null) {
            loginedMemberId = (int) session.getAttribute("loginedMemberId");
        }

        if (loginedMemberId != 0) {
            loginedMember = memberService.getMemberById(loginedMemberId);
        }
        
        String currentUrl = req.getRequestURI();
        String queryString = req.getQueryString();
        
        if(queryString !=null && queryString.length() > 0) {
        	currentUrl = currentUrl+"?"+queryString;
        }
        
        boolean needToChangPassword = false;
        
        if(loginedMember!=null) {
        	needToChangPassword = memberService.needToChangPassword(loginedMember.getId());
        }
        
        req.setAttribute("rq", new Rq(loginedMember,currentUrl,paramMap,needToChangPassword));

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
}