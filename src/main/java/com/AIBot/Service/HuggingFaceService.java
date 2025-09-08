package com.AIBot.Service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.AIBot.DTO.MessageDTO;

public interface HuggingFaceService {

	public String chat(List<MessageDTO> history, String currentMessage);

}
