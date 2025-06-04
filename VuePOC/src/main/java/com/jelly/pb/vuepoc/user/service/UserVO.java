package com.jelly.pb.vuepoc.user.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.jelly.pb.vuepoc.common.base.BaseVO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * UserVO 
 * 
 * @TODO jelly
 * add more validation like @Pattern
 * add language toggle setting for default messages
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVO extends BaseVO{
	
	
	//override SpringSecurity setting 'Username' to 'userId' in SecurityConfig.java
	
	
	
	
	///////////////////
	//	English setting
	///////////////////
	
//    @NotBlank(message = "Username(user-id) can't be empty.")
//    private String userId;
//
//    @NotBlank(message = "Password can't be empty.")
//    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,.?/~_+\\-=|\\\\]).{8,32}$", 
//    		message = "Password have to At least one digit [0-9] At least one lowercase character [a-z] At least one uppercase character [A-Z] At least one special character")
//    private String userPw;
//    
//    @NotBlank(message = "Your affiliation name can't be empty.")
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_]{2,33}$", message = "Your affiliation name Can't use special characters, have to be 2~33 length.")
//    private String userAfltNm;
//    
//    
//    @NotBlank(message = "Your name can't be empty.")
//    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_]{5,13}$", message = "Your name Can't use special characters, have to be 5~13 length.")
//    private String userNm;
//    
//    @NotBlank(message = "Your Telephone number can't be empty.")
//    @Pattern(regexp = "^\\d{3}-\\d{3,4}-\\d{4}$",  message = "Make sure your telephone number is Korea phone number.")
//    private String userMblTelno;
//    
//    
//    @NotBlank(message = " You must agree before create account.")
//    private String untyTrmsAgreYn;

    
	///////////////////
	//	Korean setting
	///////////////////
	///
    @NotBlank(message = "Username(유저 아이디)는 공백일 수 없습니다.")
    private String userId;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}\\[\\]:;<>,.?/~_+\\-=|\\\\]).{8,32}$", 
    		message = "비밀번호는 숫자, 영어 소문자, 영어 대문자, 특수문자를 포함한 8~32글자 문자여야 합니다.")
    private String userPw;
    
    @NotBlank(message = "조직이름은 공백일 수 없습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_]{2,33}$", message = "Your affiliation name Can't use special characters, have to be 2~33 length.")
    private String userAfltNm;
    
    
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_]{5,13}$", message = "Your name Can't use special characters, have to be 5~13 length.")
    private String userNm;
    
    @NotBlank(message = "전화번호는 공백일 수 없습니다.")
    @Pattern( regexp = "^\\d{3}-\\d{3,4}-\\d{4}$", message = "전화번호 형식을 다시 확인 해주세요. XXX-XXXX-XXXX")
    private String userMblTelno;
    
    
    @NotBlank(message = "회원 가입 하기전에 통합 이용약관에 동의 해주세요.")
    private String untyTrmsAgreYn;
    
    
    private String userTelno;
    private String userEmlAddr;
    private String userSttsCd;
    private String userDvsnCd;
    private String userZip;
    private String userBaseAddr;
    private String userDtlAddr;
    private String gndrCd;
    private String userDvsnTokn;
    private String joinYmd;
    private String lockYn;
    private String lockNbtm;
    private String lastLockDt;
    private String pwChgYmd;
    private String lastSttsChgDt;

    
    // Represents an authority granted to an Authentication object. 
    private List<SimpleGrantedAuthority> securityAuthList;
    
    ////////
    //these are inside of BaseVO
    ////////
//  private String frstRegrId; 
//  private String frstRegDt;
//  private String lastChgrId;
//  private String lastChgDt;

}


