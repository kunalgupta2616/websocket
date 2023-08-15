package com.example.NotificationService.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.example.NotificationService.model.Channel;
import com.example.NotificationService.repository.ChatStore;
import com.google.gson.Gson;




public class JoinChannelLambda implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse>{

	private final Gson gson = new Gson();
	private final ChatStore chatStore= new ChatStore();
	@Override
	public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent input, Context context) {
		System.out.println("Websocket ConnectionId in JoinChannelLambda lambda"+input.getRequestContext().getConnectionId());

		Channel channel= gson.fromJson(input.getBody(), Channel.class);
		saveChannelSunscription(input.getRequestContext().getConnectionId(), channel.getChannelId());
		
		
		APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
		response.setStatusCode(200);
		return response;
	}
	
	public  void saveChannelSunscription(String connectionId, String channelId) {
		chatStore.saveChannelConnection(connectionId, channelId);
		
	}

	
}
