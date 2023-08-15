package com.example.NotificationService.lambda;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsync;
import com.amazonaws.services.apigatewaymanagementapi.AmazonApiGatewayManagementApiAsyncClientBuilder;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionRequest;
import com.amazonaws.services.apigatewaymanagementapi.model.PostToConnectionResult;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;
import com.example.NotificationService.model.Publish;
import com.example.NotificationService.model.PushData;
import com.example.NotificationService.repository.ChatStore;
import com.google.gson.Gson;




public class PublishDataToChannelLambda implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse>{

	private final Gson gson = new Gson();
	private final ChatStore chatStore= new ChatStore();
	private final AmazonApiGatewayManagementApiAsync apiAsync;
	
	
	
	
	
	public  PublishDataToChannelLambda() {
		 AwsClientBuilder.EndpointConfiguration endPointConfig = 
				 new AwsClientBuilder.EndpointConfiguration(System.getenv("API_GATEWAY_URL"), System.getenv("API_GATEWAY_REGION"));
		this.apiAsync =AmazonApiGatewayManagementApiAsyncClientBuilder.standard().withEndpointConfiguration(endPointConfig).build();
	}

	@Override
	public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent input, Context context) {
		System.out.println("Websocket ConnectionId in JoinChannelLambda lambda"+input.getRequestContext().getConnectionId());

		Publish publish= gson.fromJson(input.getBody(), Publish.class);
		List<String> connectionIds = getChannelConnections(publish.getChannelId());
		try {
			publishMessageToConnections(connectionIds, new PushData(publish.getData()));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		
		
		
		APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
		response.setStatusCode(200);
		return response;
	}
	
	public  void publishMessageToConnections(List<String> connectionIds, PushData data) throws CharacterCodingException {
		
		
		if (connectionIds != null && connectionIds.size()>0) {
			for (String connectionId: connectionIds) {
				pushDataToConnection(connectionId, data);
			}
		}		
	}
	
	private void pushDataToConnection(String connectionId, PushData data) throws CharacterCodingException {
		
		PostToConnectionRequest postToConnectionRequest = new PostToConnectionRequest()
				.withConnectionId(connectionId)
				.withData(StandardCharsets.UTF_8.newEncoder().encode(CharBuffer.wrap(this.gson.toJson(data))));
		
		PostToConnectionResult postToConnectionResult = apiAsync.postToConnection(postToConnectionRequest);
		
		
	}
	
	private List<String> getChannelConnections(String channelId){
		return chatStore.getChannelConnections(channelId);
	}

	
}
