package com.sbs.untact.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.sbs.untact.dto.Member;

@Mapper
public interface MemberDao {

	Member getMemberByLoginId(@Param("loginId") String loginId);

	int getMemberLastInsertId();

	void join(@Param("loginId")String loginId,@Param("loginPw") String loginPw,@Param("name") String name,@Param("nickname") String nickname,@Param("cellphoneNo") String cellphoneNo,@Param("email") String email);

	Member getMemberById(@Param("id")int id);

	Member getFindLoginIdByNameAndEmail(@Param("name") String name,@Param("email") String email);
	
    void modify(@Param("id") int id, @Param("loginPw") String loginPw, @Param("name") String name, @Param("nickname") String nickname, @Param("cellphoneNo") String cellphoneNo, @Param("email") String email);

}
