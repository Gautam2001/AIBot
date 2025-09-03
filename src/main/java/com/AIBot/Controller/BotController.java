package com.AIBot.Controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AIBot.DTO.BotRequestDTO;
import com.AIBot.Utility.CommonUtils;

@RestController
@RequestMapping("/bot")
public class BotController {

	@GetMapping("/ping")
	public ResponseEntity<HashMap<String, Object>> ping() {
		CommonUtils.logMethodEntry(this);

		HashMap<String, Object> response = new HashMap<>();

		return ResponseEntity.ok(CommonUtils.prepareResponse(response, "pong", true));
	}
	
	@PostMapping("/generic")
	public ResponseEntity<HashMap<String, Object>> getGenericReply(@RequestBody BotRequestDTO request) {
	    CommonUtils.logMethodEntry(this, "Received bot request");

	    // Extract incoming data
	    String message = request.getMessage();
	    // In future: pass history + message to AI model here

	    String reply = "You said: " + message;

	    HashMap<String, Object> response = new HashMap<>();
	    response.put("reply", reply);

	    return ResponseEntity.ok(CommonUtils.prepareResponse(response, "Bot reply generated", true));
	}

	
}
