package com.AIBot.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageDTO {
	private String role;
	private String content;
	private String timestamp;
}
