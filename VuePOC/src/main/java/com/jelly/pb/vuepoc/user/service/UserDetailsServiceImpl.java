package com.jelly.pb.vuepoc.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.jelly.pb.vuepoc.user.UserRole;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private final UserService userService;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		log.info("loadUserByUsername => userId => {}",userId);
		UserVO userVO = new UserVO();
		userVO.setUserId(userId);
		log.info("userVO => userVO => {}",userVO);

		try {
			userVO = userService.selectUserByUserId(userVO);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new UsernameNotFoundException("Exception occurred while getting user Id -> "+userId);	
		}
		if(ObjectUtils.isEmpty(userVO)) {
			log.info("ObjectUtils => ObjectUtils => {}",userVO);
			throw new UsernameNotFoundException("userId is not found -> "+userId);	
		}
		
		log.info("userVO2 => userVO2 => {}",userVO);
		
		if(ObjectUtils.isEmpty(userVO.getSecurityAuthList())) {
			List<SimpleGrantedAuthority> basicAuthorityList = new ArrayList<SimpleGrantedAuthority>();
			SimpleGrantedAuthority basicAuth = new SimpleGrantedAuthority(UserRole.USER);
			basicAuthorityList.add(basicAuth);
			userVO.setSecurityAuthList(basicAuthorityList);
		}
		log.info("userVO3 => userVO3 => {}",userVO);

		return new org.springframework.security.core.userdetails.User(
			 userVO.getUserId(),
			 userVO.getUserPw(),
			 userVO.getSecurityAuthList()
		);
	}

}
