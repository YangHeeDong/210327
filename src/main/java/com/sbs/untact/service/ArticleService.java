package com.sbs.untact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbs.untact.dao.ArticleDao;
import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.ResultData;

@Service
public class ArticleService {
	@Autowired
	private ArticleDao articleDao;
	
	//게시물 번호로 Article 리턴
	public Article getArticleById(int id) {
		return articleDao.getArticleById(id);
	}	
	
	//게시물 생성
	public ResultData writeArticle(String title, String body) {
		int id= articleDao.writeArticle(title, body);
		return new ResultData("S-1", "게시물을 추가했습니다.", "id",id);
	}
	
	//삭제시도 성공 true 실패 false로 리턴
	public ResultData deleteArticleById(int id) {
		Article article = getArticleById(id);
		if(article==null) {
			return new ResultData("F-1", id+"번의 게시물은 존재하지 않습니다.");
		}
		articleDao.deleteArticle(article);
		return new ResultData("S-1", "게시물을 삭제했습니다.");
	}
	
	//게시물 수정
	public ResultData modifyArticle(int id, String title, String body) {
		Article article = getArticleById(id);
		
		if(article ==null) {
			return new ResultData("F-1", id+"번의 게시물은 존재하지 않습니다.");
		}
		
		articleDao.modifyArticle(article,title,body);
		return new ResultData("S-1", id+"번의 게시물을 수정하였습니다.");
	}

}
