package com.jelly.pb.vuepoc.user.service;

public interface UserService {
	
    UserVO selectUserByUserId(UserVO userVO)throws Exception;
    
    
    void insertUserByRegiste(UserVO userVO)throws Exception;


}
