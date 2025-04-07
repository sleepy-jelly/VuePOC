package com.jelly.pb.vuepoc.user.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jelly.pb.vuepoc.user.service.UserService;
import com.jelly.pb.vuepoc.user.service.UserVO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserVO selectUserByUserId(UserVO userVO) throws Exception {
        UserVO foundUserVO = userMapper.selectUserByUserId(userVO);
        log.info("selectUserByUserId ->  foundedUserVO {}", foundUserVO);
        return foundUserVO;
    }

    @Override
    public void insertUserByRegiste(UserVO userVO) throws Exception {
        userVO.setUserPw(passwordEncoder.encode(userVO.getUserPw()));
        log.info("insertUserByRegiste => {}", userVO);
        userMapper.insertUserByRegiste(userVO);
    }
}
