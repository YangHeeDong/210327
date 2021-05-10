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
    
    private boolean isAjax(HttpServletRequest req) {
        String[] pathBits = req.getRequestURI().split("/");

        String controllerTypeCode = "";
        String controllerSubject = "";
        String controllerActName = "";

        if (pathBits.length > 1) {
            controllerTypeCode = pathBits[1];
        }

        if (pathBits.length > 2) {
            controllerSubject = pathBits[2];
        }

        if (pathBits.length > 3) {
            controllerActName = pathBits[3];
        }

        boolean isAjax = false;

        String isAjaxParameter = req.getParameter("isAjax");

        if ( isAjax == false ) {
            if ( controllerActName.startsWith("get") ) {
                isAjax = true;
            }
        }

        if ( isAjax == false ) {
            if (controllerActName.endsWith("Ajax")) {
                isAjax = true;
            }
        }

        if ( isAjax == false ) {
            if (isAjaxParameter != null && isAjaxParameter.equals("Y")) {
                isAjax = true;
            }
        }

        return isAjax;
    }

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
        
        req.setAttribute("rq", new Rq(isAjax(req),loginedMember,currentUrl,paramMap,needToChangPassword));

        return HandlerInterceptor.super.preHandle(req, resp, handler);
    }
}