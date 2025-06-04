package com.jelly.pb.vuepoc.openai.service.impl;

import org.springframework.web.multipart.MultipartFile;

import com.jelly.pb.vuepoc.common.base.BaseVO;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * VO for the Request AudioTranscription
 * with open ai 
 */
@Getter
@Setter
@ToString
public class AudioRequestVO extends BaseVO {

	
	private MultipartFile userInputFile;
	
	private String userPrompt;
	
	private String historyPrompt;
	
	
	
	
	
	
}
