package com.jelly.pb.vuepoc.openai.web;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jelly.pb.vuepoc.common.base.BaseController;
import com.jelly.pb.vuepoc.openai.service.OpenAIService;
import com.openai.models.chat.completions.ChatCompletion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@RestController 
@RequestMapping("/NeoAI")
@RequiredArgsConstructor
public class OpenAIController extends BaseController {

    private final OpenAIService openAIService;
    
	@RequestMapping(value="/viewGeneralQuestion", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Void> viewGeneralQuestion(ModelAndView mav) {
		log.info("viewGeneralQuestion");
		return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:5173/OpenAI/general-question")
                .build();
	}
	
	
	@RequestMapping(value="/viewAudioSummary", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Void> viewAudioSummary(ModelAndView mav) {
		log.info("viewGeneralQuestion");
		return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:5173/OpenAI/Audio-summary")	
                .build();
	}
	
	
	@PostMapping("/chat")
    public ResponseEntity<ChatCompletion> chat(@RequestBody Map<String, String> request){
		log.info("chat  => request = > {}",request);
		
		String prompt = request.get("userPrompt");
		ChatCompletion reply = openAIService.getSimpleChatResponse(prompt);
		
		log.info("chat  => ok = > {}",reply);
		return ResponseEntity.ok(reply);
    }
	
	
	@PostMapping(value="/StreamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream(@RequestBody Map<String, String> request ) throws JsonProcessingException {
		log.info("chat  => request = > {}",request);
		
		String prompt = request.get("userPrompt");
		String historyJson = request.get("historyJson");
        return openAIService.getStreamChatResponse(prompt, historyJson);
        
    }

	@PostMapping(value="/StreamAudioSum", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> AudioSumStream(@RequestParam("file") MultipartFile file ) throws Exception {
		
		log.info("chat  => request filegetSize= > {}",file.getSize());
		log.info("chat  => request filegetName= > {}",file.getName());
		log.info("chat  => request filegetContentType= > {}",file.getContentType());

        return openAIService.getStreamAudioSummaryResponse(file,null,null);
        
    }
	
	 /**
     * check user if Authenticated
     * 
     * @return Authenticated(TRUE / FALSE)
     */
    public static Boolean isAuthenticated() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (Objects.isNull(authentication)) {
        	log.debug("## authentication object is null!!");
            return Boolean.FALSE;
        }

        String username = authentication.getName();
        if (username.equals("anonymousUser")) {		
        	log.debug("## username is {}", username);
            return Boolean.FALSE;
        }

	    Object principal = authentication.getPrincipal();
	
	    return (Boolean.valueOf(!Objects.isNull(principal))); 
    }
    
}