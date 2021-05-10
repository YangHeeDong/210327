package com.sbs.untact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ArticleDao;
import com.sbs.untact.dao.ReplyDao;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.Reply;
import com.sbs.untact.dto.ResultData;

@Service
public class ReplyService {

	@Autowired
	ReplyDao replyDao;
	
    public ResultData modify(int id, String body) {
        replyDao.modify(id, body);

        return new ResultData("S-1", id + "번 댓글이 수정되었습니다.", "id", id);
    }
	
	public ResultData write(String relTypeCode, int relId, int memberId,String body) {
		
		replyDao.write(relTypeCode,relId,memberId,body);
		
		int id = replyDao.getReplyLastInsertId();
		
		return new ResultData("S-1","댓글을 작성하였습니다","id",id);
	}

	public List<Reply> getForPrintRepliesByRelTypeCodeAndRelId(String relTypeCode, int relId) {
		return replyDao.getForPrintRepliesByRelTypeCodeAndRelId(relTypeCode,relId);
	}

	public Reply getReplyById(int id) {
		return replyDao.getReplyById(id);
	}

	public ResultData delete(int id) {
		replyDao.delete(id);
		return new ResultData("S-1","댓글을 삭제하였습니다.");
	}


	

}