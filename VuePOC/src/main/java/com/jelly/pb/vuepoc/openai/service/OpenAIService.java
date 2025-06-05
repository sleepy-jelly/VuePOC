package com.jelly.pb.vuepoc.openai.service;

import org.springframework.web.multipart.MultipartFile;

import com.jelly.pb.vuepoc.openai.service.impl.AudioSummaryHistoryVO;
import com.openai.models.chat.completions.ChatCompletion;

import reactor.core.publisher.Flux;

public interface OpenAIService {
	
	ChatCompletion getSimpleChatResponse(String prompt);

	String getSimpleStrResponse(String prompt);

	Flux<String> getStreamChatResponse(String prompt, String historyJson);
	
	Flux<String> getStreamAudioSummaryResponse(MultipartFile file, String prompt, String historyJson)throws Exception;
	
	//추후 히스토리 저장 기능
//	void insertAudioSummaryHistory(AudioSummaryHistoryVO ashVO);
	
}
