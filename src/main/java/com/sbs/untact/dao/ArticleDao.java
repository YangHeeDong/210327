package com.sbs.untact.dao;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.sbs.untact.dto.Article;
import com.sbs.untact.util.Util;

@Component
public class ArticleDao {
	private int articleLastId;
	private ArrayList<Article> articles;
	
	public ArticleDao(){
		articles = new ArrayList<Article>();
		makeTastData("제목","내용");
		articleLastId = 0;
	}
	
	//번호로 게시물 가져오기
	public Article getArticleById(int id) {
		for(Article article :articles) {
			if(article.getId()==id) {
				return article;
			}
			
		}
		return null;
	}
	
	//게시물 추가
	public int writeArticle(String title, String body) {
		articleLastId++;
		
		String regDate = Util.getNowDateStr();
		String updateDate = Util.getNowDateStr();
		Article article = new Article(articleLastId, title, body, regDate,updateDate);
		
		articles.add(article);
		return article.getId();
	}
	
	//테스트 데이터
	private void makeTastData(String title, String body) {
		for(int i =0; i<3;i++) {
			writeArticle(title,body);
		}
	}
	
	//게시물 삭제
	public void deleteArticle(Article article) {
		 articles.remove(article);
		
	}

	//게시물 수정
	public void modifyArticle(Article article,String title,String body) {
		article.setTitle(title);
		article.setBody(body);
		article.setUpdateDate(Util.getNowDateStr());
	}

}
