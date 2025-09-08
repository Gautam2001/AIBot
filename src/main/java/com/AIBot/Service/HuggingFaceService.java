package com.AIBot.Service;

import java.util.List;

import com.AIBot.DTO.MessageDTO;

public interface HuggingFaceService {

	public String chat(List<MessageDTO> history, String currentMessage);

}
