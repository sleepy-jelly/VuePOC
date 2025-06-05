package com.jelly.pb.vuepoc.user.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.jelly.pb.vuepoc.common.base.BaseController;
import com.jelly.pb.vuepoc.common.filter.JwtService;
import com.jelly.pb.vuepoc.user.service.UserService;
import com.jelly.pb.vuepoc.user.service.UserVO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController extends BaseController {

	
	private final UserDetailsService userDetailsService;
	private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
	@RequestMapping(value="/viewLogin", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Void> login(ModelAndView mav) {
		log.info("viewLogin");
		return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:5173/login/login-page")
                .build();
	}
//	
	@PostMapping("/loginProcess")
    public ResponseEntity<?> login(@RequestBody UserVO userVO) {
		
		
		log.info("loginProcess  => UserVO = > {}",userVO);

        Authentication authentication = authenticationManager.authenticate(
		    new UsernamePasswordAuthenticationToken(
		    	userVO.getUserId(), userVO.getUserPw()
		    )
		);
        
		log.info("loginProcess  => authentication = > {}",authentication);

		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String token = jwtService.generateToken(userDetails);

			
		Map<String, Object> result = new HashMap<>();
		result.put("token", token);
		result.put("ReturnMessage", "Successful");
		result.put("Redirect", "/dash-board");
		log.info("loginProcess  => ok = > {}",result);

		return ResponseEntity.ok(result);
    }

	@RequestMapping(value="/loginSuccessful", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Map<String, String>> loginSuccessful() throws Exception {
		log.info("loginSuccessful");
		
		Map<String, String> resultMap = new HashMap<String, String>();
		
		// Get the current authentication
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		log.info("loginSuccessful --> username -> {}", username);
		
		// Get UserDetails from UserDetailsService
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		log.info("loginSuccessful --> userDetails -> {}", userDetails);
		
		resultMap.put("ReturnMessage", "Successful");
		resultMap.put("Redirect", "/dash-board");
		
		String getAuthentication = SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("loginSuccessful --> getAuthentication ->{}",getAuthentication);
		
		return ResponseEntity
			.status(HttpStatus.OK)
			.header("Origin", "http://localhost:5173")
			.body(resultMap);
	}
	

	@RequestMapping(value="/viewPageRegister", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Void> viewPageRegister(ModelAndView mav) throws Exception {
		log.info("viewPageRegister");
		
		return ResponseEntity.status(HttpStatus.FOUND)
			.header("Location", "http://localhost:5173/register")
			.build();
	}
	
	
	@PostMapping(value="/registerProcess")
	public ResponseEntity<Map<String, String>> registerProcess(@RequestBody @Valid UserVO userVO, ModelAndView mav, BindingResult bindingResult)  {
		Map<String, String> responseMap = new HashMap<>();
		
		if (bindingResult.hasErrors()) {
			Map<String, String> errors = new HashMap<>();
	        for(FieldError error : bindingResult.getFieldErrors()) {
	            errors.put(error.getField(), error.getDefaultMessage());
	        }
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);

		}
		
		log.info("registerProcess");
		
		try {
			// Check if user already exists
			if (userService.selectUserByUserId(userVO) != null) {
				responseMap.put("error", "User ID already exists. Please choose a different ID.");
				return ResponseEntity.status(HttpStatus.CONFLICT).body(responseMap);
			}
			
			userService.insertUserByRegiste(userVO);
			responseMap.put("message", "Registration successful");
			return ResponseEntity.ok(responseMap);
			
		} catch (Exception e) {
			log.error("Registration error: ", e);
			responseMap.put("error", "An error occurred during registration. Please try again.");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
		}
	}
	
	 /**
     * check user if Authenticated
     * 
     * @return Authenticated(TRUE / FALSE)
     */
	@GetMapping(value="/checkSession")
    public static ResponseEntity<Map<String, Object>> isAuthenticated(HttpSession session) {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        HashMap<String,Object> responseMap= new HashMap<>();
        
        responseMap.put("authenticated", authentication != null);

        if (Objects.isNull(authentication)) {
        	log.debug("## authentication object is null!!");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }

        String username = authentication.getName();
        if (username.equals("anonymousUser")) {		
        	log.debug("## username is {}", username);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseMap);
        }
		return ResponseEntity.ok(responseMap);
    }
    
}