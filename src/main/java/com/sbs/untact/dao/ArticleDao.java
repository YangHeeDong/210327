package com.sbs.untact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;

@Mapper
public interface ArticleDao {
	boolean modifyArticle(@Param("id") int id, @Param("title") String title, @Param("body") String body);

	void writeArticle(@Param("boardId") int boardId, @Param("memberId") int memberId, @Param("title") String title, @Param("body") String body);

	Article getArticleById(@Param("id") int id);

	void deleteArticleById(@Param("id") int id);
	
	int getArticldLastInsertId();

	Board getBoardById(@Param("id") int boardId);

	int getArticleTotalCount(@Param("boardId") int boardId,@Param("searchKeywordType")String searchKeywordType,@Param("searchKeyword") String searchKeyword);

	List<Article> getForPrintArticles(@Param("boardId") int boardId,@Param("searchKeywordType")String searchKeywordType,@Param("searchKeyword") String searchKeyword, @Param("limitFrom") int limitFrom,@Param("limitTake") int limitTake);
}
