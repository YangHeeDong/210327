package com.sbs.untact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.util.Util;

@Controller
public class MpaUsrArticleController {
	
	@Autowired
	ArticleService articleService;
	
	private String msgAndBack(HttpServletRequest req, String msg) {
		req.setAttribute("msg", msg);
		req.setAttribute("historyBack", true);
		return "common/redirect";
	}

	private String magAndReplace(HttpServletRequest req, String msg, String replaceUrl) {
		req.setAttribute("msg", msg);
		req.setAttribute("replaceUrl", replaceUrl);
		return "common/redirect";
	}

	//게시물 생성
	@RequestMapping("/mpaUsr/article/doWrite")
	@ResponseBody
	public ResultData doWrite(String title, String body) {
		if(Util.isEmpty(title)) {
			return new ResultData("F-1", "제목를 입력해 주세요");
		}
		if(Util.isEmpty(body)) {
			return new ResultData("F-2", "내용를 입력해 주세요");
		}
		
		return articleService.writeArticle(title,body);
	}

	//게시물 번호로 가져오기
	@RequestMapping("/mpaUsr/article/getArticle")
	@ResponseBody
	public ResultData getArticle(Integer id) {
		if(Util.isEmpty(id)) {
			return new ResultData("F-1", "ID를 입력해 주세요");
		}
		
		Article article = articleService.getArticleById(id);
		
		if(article==null) {
			return new ResultData("F-1", "존재하지 않는 게시물 입니다.");
		}
		
		return new ResultData("S-1", article.getId()+"번 글입니다.","article",article);
	}
	
	//게시물 번호로 삭제
	@RequestMapping("/mpaUsr/article/doDelete")
	public String doDelete(HttpServletRequest req, Integer id) {
		
		if(Util.isEmpty(id)) {
			return msgAndBack(req, "번호를 입력해 주세요");
		}
		
		ResultData rd = articleService.deleteArticleById(id);
		
		if(rd.isFail()) {
			return msgAndBack(req, rd.getMsg());
		}
		
		String replaceUrl = "../article/list?boardId="+rd.getBody().get("boardId");
		
		return magAndReplace(req, rd.getMsg(),replaceUrl);
	}

	//게시물 번호로 수정
	@RequestMapping("/mpaUsr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id,String title,String body) {
		
		if(Util.isEmpty(id)) {
			return new ResultData("F-1", "ID를 입력해 주세요");
		}
		if(Util.isEmpty(title)) {
			return new ResultData("F-2", "제목를 입력해 주세요");
		}
		if(Util.isEmpty(body)) {
			return new ResultData("F-3", "내용를 입력해 주세요");
		}
		
		return articleService.modifyArticle(id,title,body);
	}
	
	//어느 게시판인지
	@RequestMapping("/mpaUsr/article/list")
	public String showList(HttpServletRequest req, int boardId,int page) {
		Board board = articleService.getBoardById(boardId);
		
		if(board == null) {
			return msgAndBack(req, "존재하지 않는 게시판 입니다.");
		}
		req.setAttribute("board", board);
		
		//게시판에 있는 게시물 총 개수
		int totalItemsCount = articleService.getArticleTotalCount(boardId);
		req.setAttribute("totalItemsCount", totalItemsCount);
		
		//한 페이지당 보여줄 게시물 개수
		int itemsInAPage = 20;
		//게시판 총 페이지 개수
		int totalPage = (int) Math.ceil(totalItemsCount/(double)itemsInAPage);
		req.setAttribute("totalPage", totalPage);
		
		List<Article> articles = articleService.getForPrintArticles(boardId,itemsInAPage,page );
		req.setAttribute("articles", articles);
		req.setAttribute("page", page);

		return "mpaUsr/article/list";
	}
	
}