package com.example.NotificationService.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2WebSocketResponse;




public class DefaultLambda implements RequestHandler<APIGatewayV2WebSocketEvent, APIGatewayV2WebSocketResponse>{

	@Override
	public APIGatewayV2WebSocketResponse handleRequest(APIGatewayV2WebSocketEvent input, Context context) {
		System.out.println("Websocket ConnectionId in default lambda"+input.getRequestContext().getConnectionId());

		APIGatewayV2WebSocketResponse response = new APIGatewayV2WebSocketResponse();
		response.setStatusCode(200);
		return response;
	}
	

	
}
