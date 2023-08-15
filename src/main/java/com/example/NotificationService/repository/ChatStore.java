package com.example.NotificationService.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

public class ChatStore {
	
	private final DynamoDB dynamoDB;
	
	public ChatStore() {
		AmazonDynamoDBClientBuilder clientBuilder= AmazonDynamoDBClientBuilder.standard()
				.withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(System.getenv("DYNAMODB_URI"),System.getenv("API_GATEWAY_REGION")));
		dynamoDB = new DynamoDB(clientBuilder.build());
		
	}

	public void saveChannelConnection(String connectionId, String channelId) {
		try {
			Map<String, AttributeValue> item_values = new HashMap<>();
			item_values.put("channelId", new AttributeValue(channelId));
			item_values.put("connectionId", new AttributeValue(connectionId));
			
			Item item = new Item().withPrimaryKey("channelId", channelId)
					.withString("connectionId", connectionId);
			
			Table table = dynamoDB.getTable("channel-connections");
			table.putItem(item);
			
		}catch (Exception ex) {
			
		}
		
	}

	public List<String> getChannelConnections(String channelId) {
		QuerySpec queryReq = new QuerySpec()
				.withKeyConditionExpression("channelId = :channelId")
				.withValueMap(new ValueMap().withString(":channelId", channelId));
		Table channelConnections = dynamoDB.getTable("channel-connections");
		ItemCollection<QueryOutcome> items = channelConnections.query(queryReq);
		
		Iterator<Item> iterator = items.iterator();
		Item item;
		
		List<String> connections = new ArrayList<>();
		
		while(iterator.hasNext()) {
			item= iterator.next();
			connections.add(item.getString("connectionId"));
		}
		
		return connections;
	}

}
