package com.example.NotificationService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Channel {
	
	private String channelId;
	private String route;

}
