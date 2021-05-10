package com.sbs.untact.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.dto.Rq;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.service.ReplyService;
import com.sbs.untact.util.Util;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MpaUsrReplyController {
	
	@Autowired
	ReplyService replyService;
	@Autowired
	ArticleService articleService;
	
	@RequestMapping("/mpaUsr/reply/modify")
    public String showModify(HttpServletRequest req, int id, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        req.setAttribute("reply", reply);

        String title = "";

        switch ( reply.getRelTypeCode() ) {
            case "article":
                Article article = articleService.getArticleById(reply.getRelId());
                title = article.getTitle();
        }

        req.setAttribute("title", title);

        return "mpaUsr/reply/modify";
    }

    @RequestMapping("/mpaUsr/reply/doModify")
    public String doModify(HttpServletRequest req, int id, String body, String redirectUri) {
        Reply reply = replyService.getReplyById(id);

        if ( reply == null ) {
            return Util.msgAndBack(req, "존재하지 않는 댓글입니다.");
        }

        Rq rq = (Rq)req.getAttribute("rq");

        if ( reply.getMemberId() != rq.getLoginedMemberId() ) {
            return Util.msgAndBack(req, "권한이 없습니다.");
        }

        ResultData modifyResultData = replyService.modify(id, body);

        redirectUri = Util.getNewUri(redirectUri, "focusReplyId", id + "");

        return Util.msgAndReplace(req, modifyResultData.getMsg(), redirectUri);
    }
	
    @RequestMapping("/mpaUsr/reply/doWrite")
    public String doWrite(HttpServletRequest req, String relTypeCode, int relId,String body,String redirectUri) {
    	
    	int memberId = ((Rq)req.getAttribute("rq")).getLoginedMemberId();
    	
    	ResultData replyWriteResultData = replyService.write(relTypeCode,relId,memberId,body);
    	
    	int newReplyId = (int)replyWriteResultData.getBody().get("id");
    	
    	redirectUri = Util.getNewUri(redirectUri, "focusReplyId", newReplyId+"");
    	
    	return Util.msgAndReplace(req, replyWriteResultData.getMsg(), redirectUri);
    }
    
    @RequestMapping("/mpaUsr/reply/doDeleteAjax")
    @ResponseBody
    public ResultData doDeleteAjax(HttpServletRequest req, int id,String redirectUri) {
    	int loginedMemberId = ((Rq)req.getAttribute("rq")).getLoginedMemberId();
    	
    	Reply reply = replyService.getReplyById(id);
    	
    	if(reply.getMemberId() != loginedMemberId) {
    		return new ResultData("F-1","권한이 없습니다.");
    	}
    	
    	if(reply == null) {
    		return new ResultData("F-2","존재하지 않는 댓글입니다.");
    	}
    	
    	ResultData deleteResultData = replyService.delete(id);
    	
    	return deleteResultData;
    }
    
    @RequestMapping("/mpaUsr/reply/doDelete")
    public String doDelete(HttpServletRequest req, int id,String redirectUri) {
    	int loginedMemberId = ((Rq)req.getAttribute("rq")).getLoginedMemberId();
    	
    	Reply reply = replyService.getReplyById(id);
    	
    	if(reply.getMemberId() != loginedMemberId) {
    		return Util.msgAndBack(req, "권한이 없습니다.");
    	}
    	
    	if(reply == null) {
    		return Util.msgAndBack(req, "존재하지 않는 댓글 입니다.");
    	}
    	
    	ResultData deleteResultData = replyService.delete(id);
    	
    	return Util.msgAndReplace(req,deleteResultData.getMsg(),redirectUri);
    }
    
}