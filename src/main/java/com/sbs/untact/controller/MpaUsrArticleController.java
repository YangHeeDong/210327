package com.sbs.untact.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

	//게시물 생성
	@RequestMapping("/mpaUsr/article/doWrite")
	public String doWrite(HttpServletRequest req, int boardId, String title, String body) {
		if(Util.isEmpty(title)) {
			return Util.msgAndBack(req, "제목을 입력해 주세요!");
		}
		if(Util.isEmpty(body)) {
			return Util.msgAndBack(req, "내용을 입력해 주세요!");
		}
		
		ResultData rd = articleService.writeArticle(boardId,title,body);
		
		return Util.msgAndReplace(req, rd.getMsg(), "/mpaUsr/article/detail?id="+rd.getBody().get("id"));
	}
	
	@RequestMapping("/mpaUsr/article/write")
	public String showWrite(HttpServletRequest req ,@RequestParam(defaultValue = "1") int boardId){
		Board board = articleService.getBoardById(boardId);
		
		if(Util.isEmpty(board)) {
			return Util.msgAndBack(req, "없는 게시판 입니다!");
		}
		
		req.setAttribute("board", board);
		return "/mpaUsr/article/write";
	}
	
	@RequestMapping("/mpaUsr/article/detail")
	public String showDetail(HttpServletRequest req , int id){
		Article article = articleService.getForPrintArticleById(id);
		
		Board board = articleService.getBoardById(article.getBoardId());
		
		if(Util.isEmpty(board)) {
			return Util.msgAndBack(req, "없는 게시판 입니다!");
		}
		
		req.setAttribute("article", article);
		req.setAttribute("board", board);
		
		return "/mpaUsr/article/detail";
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
			return Util.msgAndBack(req, "번호를 입력해 주세요");
		}
		
		ResultData rd = articleService.deleteArticleById(id);
		
		if(rd.isFail()) {
			return Util.msgAndBack(req, rd.getMsg());
		}
		
		String replaceUrl = "../article/list?boardId="+rd.getBody().get("boardId");
		
		return Util.msgAndReplace(req, rd.getMsg(),replaceUrl);
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
	public String showList(HttpServletRequest req, int boardId,@RequestParam(defaultValue = "titleAndBody") String searchKeywordType, String searchKeyword,@RequestParam(defaultValue = "1") int  page) {
		Board board = articleService.getBoardById(boardId);
		
		if(Util.isEmpty(board)) {
			return Util.msgAndBack(req, "존재하지 않는 게시판 입니다.");
		}
		req.setAttribute("board", board);
		
		//searchKeyword에 null이거나 ""일 경우 null로 치환
		if(Util.isEmpty(searchKeyword)) {
			searchKeyword = null;
		}
		//게시판에 있는 게시물 총 개수
		int totalItemsCount = articleService.getArticleTotalCount(boardId,searchKeywordType,searchKeyword);
		req.setAttribute("totalItemsCount", totalItemsCount);
		
		//한 페이지당 보여줄 게시물 개수
		int itemsInAPage = 20;
		//게시판 총 페이지 개수
		int totalPage = (int) Math.ceil(totalItemsCount/(double)itemsInAPage);
		req.setAttribute("totalPage", totalPage);
		
		List<Article> articles = articleService.getForPrintArticles(boardId,searchKeywordType,searchKeyword,itemsInAPage,page );
		req.setAttribute("articles", articles);
		req.setAttribute("page", page);

		return "mpaUsr/article/list";
	}
	
}