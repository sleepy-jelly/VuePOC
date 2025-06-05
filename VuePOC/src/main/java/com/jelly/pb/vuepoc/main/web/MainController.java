package com.jelly.pb.vuepoc.main.web;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jelly.pb.vuepoc.common.base.BaseController;


@Controller
public class MainController extends BaseController{
	
	@ResponseBody
    @GetMapping("/test/ContextHolder")
	public String testContextHolder() {

		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		Object dtl = SecurityContextHolder.getContext().getAuthentication().getCredentials();

		return "Main Controller : "+name+ "details: " + dtl;
		
	}
}
