package com.jelly.pb.vuepoc.user.service;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import common.base.web.BaseVO;
import jakarta.validation.constraints.NotBlank;
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
 */
@Builder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserVO extends BaseVO{
	
    @NotBlank(message = "Username(user-id) can't be empty.")
	private String userId;

    @NotBlank(message = "Password can't be empty.")
//    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "password have to be 8~16 length , can't use special characters")
    private String userPw;
   
    @NotBlank(message = "Your name can't be empty.")
//    @Pattern(regexp = "^[a-zA-Za-z0-9-_]{5,13}$", message = "Your name Can't use special characters, have to be 5~13 length.")
    private String userNm;
    
    @NotBlank(message = "Your Email can't be empty.")
//  @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$")
    private String userEmail;
    
    private String userDc;
    
    private String userPhone;
    
    private String userCmpny;
    
    private String userGithubLink;
    
    private String userPermission;
    
    private String userStatus;
    
    private long fileGrpSn;
    
    private String user2FactorYn;
    
    private String user2FactorKey;
    
    // Represents an authority granted to an Authentication object. 
    private List<SimpleGrantedAuthority> securityAuthList;
    
    

}
