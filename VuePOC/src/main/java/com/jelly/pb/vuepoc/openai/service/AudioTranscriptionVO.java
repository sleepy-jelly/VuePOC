package com.jelly.pb.vuepoc.openai.service;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioTranscriptionVO implements Serializable{
	private static final long serialVersionUID = -5638097453371686113L;
	
	private MultipartFile file;
	private String prompt;
	private String model;
	private String historyJson;
	
}