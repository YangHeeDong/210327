package com.sbs.untact.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Article;
import com.sbs.untact.dto.Board;
import com.sbs.untact.dto.Reply;

@Mapper
public interface ReplyDao {
	
	int getReplyLastInsertId();

	void write(@Param("relTypeCode") String relTypeCode,@Param("relId") int relId,@Param("memberId") int memberId,@Param("body")String body);

	List<Reply> getForPrintRepliesByRelTypeCodeAndRelId(@Param("relTypeCode")String relTypeCode,@Param("relId") int relId);

	Reply getReplyById(@Param("id") int id);

	void delete(@Param("id") int id);
	
    void modify(@Param("id") int id, @Param("body") String body);

	
}
