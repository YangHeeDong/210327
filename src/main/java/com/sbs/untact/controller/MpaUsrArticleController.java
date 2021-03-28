package com.sbs.untact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;
import com.sbs.untact.service.ArticleService;
import com.sbs.untact.util.Util;

@Controller
public class MpaUsrArticleController {
	
	@Autowired
	ArticleService articleService;

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
	@ResponseBody
	public ResultData doDelete(Integer id) {
		
		if(Util.isEmpty(id)) {
			return new ResultData("F-1", "ID를 입력해 주세요");
		}
		
		return articleService.deleteArticleById(id);
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
	
}