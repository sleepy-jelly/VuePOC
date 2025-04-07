package com.jelly.pb.vuepoc.user.service.impl;

import org.apache.ibatis.annotations.Mapper;

import com.jelly.pb.vuepoc.user.service.UserVO;

@Mapper
public interface UserMapper {

	public UserVO selectUserByUserId(UserVO userVO);
	
	public int insertUserByRegiste(UserVO userVO);
	
		
	
}
