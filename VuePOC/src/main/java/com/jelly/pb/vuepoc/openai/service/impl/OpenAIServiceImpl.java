package com.jelly.pb.vuepoc.openai.service.impl;


import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jelly.pb.vuepoc.openai.service.OpenAIService;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.core.http.AsyncStreamResponse;
import com.openai.models.ChatModel;
import com.openai.models.audio.AudioModel;
import com.openai.models.audio.transcriptions.TranscriptionCreateParams;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionChunk;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Slf4j
@Service
public class OpenAIServiceImpl implements OpenAIService {
	 
	//@TODO change this OpenAI key 
//    @Value("${api.key.openai}")
	private String OPENAI_API_KEY ="sk-YOUR OPENAI_API_KEY";
    
	final double TEMPERATURE = 0.2;
	final int    MAX_TOKEN = 500;
	
	private OpenAIClient client = OpenAIOkHttpClient.builder().apiKey(OPENAI_API_KEY).build();
	
	@Override
	public ChatCompletion getSimpleChatResponse(String prompt) {
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	            .model(ChatModel.GPT_4O_MINI)
	            .addSystemMessage("You are a kind AI CHAT BOT. respond to user short message in korean")
	            .addUserMessage(prompt)
	            .build();

		ChatCompletion chatCompletion = client.chat().completions().create(params);
		return chatCompletion;
    }


	@Override
	public String getSimpleStrResponse(String prompt) {
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	            .model(ChatModel.GPT_4O_MINI)
	            .addSystemMessage("You are a kind AI CHAT BOT. respond to user short message in korean, kind way and slightly cute way and trying to not send heart if they are not ask")
	            .addUserMessage(prompt)
	            .build();

		ChatCompletion chatCompletion = client.chat().completions().create(params);
        String reply = chatCompletion.choices().get(0).message().content().orElse("ERROR");

		return reply;
	}


	/**
	 * 유저 챗봇 대화 (flux 리턴)
	 * 동적 UI 사용시 필요
	 * OpenAI의 권장 방식
	 */
	@Override
	public Flux<String> getStreamChatResponse(String prompt, String historyJson) {
		
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
	            .model(ChatModel.GPT_4O_MINI)
	            .addUserMessage(historyJson) //유저 대화 기록도 같이 보내줘야함
	            .addSystemMessage("You are a kind AI CHAT BOT."
	            		+ " respond to user short message in korean, "
	            		+ "kind way and slightly cute way "
	            		+ "and trying to not send heart if they are not ask")  //idk why but this ai keep sending some heart emoji :C
                .addUserMessage(prompt)		 // 새로운 프롬포트	
	            .build();
		
		return Flux.create(emitter -> {
	        client.async().chat().completions().createStreaming(params)
	            .subscribe(new AsyncStreamResponse.Handler<>() {
	                @Override
	                public void onNext(ChatCompletionChunk chunk) {
	                    chunk.choices().forEach(choice -> {
	                        choice.delta().content().ifPresent(content ->{
		                        log.info("OpenAI response by chunk -> {}", chunk);
					            //somehow the blank are just disappear in front end when i use raw " " so i replaced with HTML
		                            String formattedContent = content
		                                .replace(" ", "&nbsp;")				
	                                .replace(".", "<br>");
	                            emitter.next(formattedContent);
	                        });
	                    });
	                }
	                @Override
	                public void onComplete(Optional<Throwable> error) {
	                    if (error.isPresent()) {
	                        emitter.error(error.get());
	                    } else {
	                        emitter.complete();
	                    }
	                }
	            });
	    }, FluxSink.OverflowStrategy.BUFFER);
	}
	
	/**
	 * 음성요약 기능
	 * 우선 file 만 있 으면 작동되지만, 
	 * User Prompt, historyJson을 추가 하고 싶으면 추가해서 사용
	 */
	@Override
	public Flux<String> getStreamAudioSummaryResponse(MultipartFile file, String prompt, String historyJson) throws Exception {
		
//		String filename = file.getOriginalFilename();  // Get filename
//		String contentType = file.getContentType();    // Get content type
		
		// 1) Save upload to temp file
		TranscriptionCreateParams transParams = TranscriptionCreateParams.builder()
		            .model(AudioModel.GPT_4O_MINI_TRANSCRIBE)
		            .file(file.getInputStream())
		            .build();
			
		log.info("transParams-->{}",transParams);
				
		byte[] fileBytes = file.getBytes();
		
		// 개발 당시 OpenAI SDK의 버그가 있어서
		// File처리가 좀 이상했음 (InputStream 처리가 이상했음) 그래서 https 방식으로 걍 연동해버림
		// Prepare the API endpoint and headers 
		String apiUrl = "https://api.openai.com/v1/audio/transcriptions";
		HttpHeaders headers = new HttpHeaders();
		 
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.set("Authorization", "Bearer " + OPENAI_API_KEY);
		
		// Create a multipart form
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new ByteArrayResource(fileBytes) {
		     @Override
		     public String getFilename() {
		         return file.getOriginalFilename();
		     }
		 });
		
		// 2025-05-27 기준 가장 저렴하면서 오디오 요약 가능한 모델
		// Specify the model for transcription
		body.add("model", "gpt-4o-mini-transcribe"); 
		
		// Set up the HTTP request
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		RestTemplate restTemplate = new RestTemplate();
		
		// Send the request
		ResponseEntity<String> response = restTemplate.exchange(
		         apiUrl, HttpMethod.POST, requestEntity, String.class);
		
		log.info("asdasda P{}",response);
		
		String transcriptionResult;
		
		// 음성 내용 저장
		if (response.getStatusCode() == HttpStatus.OK) {
		    // Parse the response JSON to extract the "text" field
		    transcriptionResult = extractTranscriptionText(response.getBody());
		} else {
		   throw new Exception("ERROR WHILE USING OPEN-AI API");
		}
		
		ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
		        .model(ChatModel.GPT_4O_MINI)
		        // 영어로 쓰면 토큰 절약이 가능함
		        // 추출한 음성을 요약
		        .addSystemMessage("Summarize the contents in Korean. Please format the summarized content properly")
		        .addUserMessage(transcriptionResult)
		        .build();
		
		AtomicReference<StringBuilder> fullContentForLog = new AtomicReference<>(new StringBuilder());

		
		
		return Flux.create(emitter -> {
		    client.async().chat().completions().createStreaming(params)
		        .subscribe(new AsyncStreamResponse.Handler<>() {
		            @Override
		            public void onNext(ChatCompletionChunk chunk) {
		                // Extract delta content (if present)
					    chunk.choices().forEach(choice -> {
					        choice.delta().content().ifPresent(content ->{
					            log.info("ai 답은 이거에용  -> {}", chunk);
					            //somehow the blank are just disappear in front end when i use raw " " so i replaced with HTML
					            String formattedContent = content
					                .replace(" ", "&nbsp;")				
					                .replace(".", "<br>");
					            
					            fullContentForLog.get().append(formattedContent);	//로깅용이에용

		                        emitter.next(formattedContent);
		                    });
		                });
		            }
	                @Override
	                public void onComplete(Optional<Throwable> error) {
	                    if (error.isPresent()) {
	                        emitter.error(error.get());
	                    } else {
	                    	String rspsnContext = fullContentForLog.get().toString();
	                    	
	                    	
	                    	
	                        log.info("Full response after complete: rspsnContext {}", rspsnContext);
	                        // Insert into DB (non-reactive)
//	                        myRepository.save( (rspsnContext));
	                        emitter.complete();
                    }
                }
            });
						
		}, FluxSink.OverflowStrategy.BUFFER);
	}

	
	
	
	


	// Helper method to extract transcription text from the JSON response
    private String extractTranscriptionText(String jsonResponse) {
        try {
            // Parse the JSON string using Jackson
            JsonNode rootNode = new ObjectMapper().readTree(jsonResponse);
            return rootNode.path("text").asText(); // Extract "text" field
        } catch (IOException e) {
            return "Error extracting text from response: " + e.getMessage();
        }
    }
	


}
