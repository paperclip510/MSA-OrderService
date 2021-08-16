package com.shdh.messagequeue;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shdh.dto.Field;
import com.shdh.dto.KafkaOrderDto;
import com.shdh.dto.OrderDto;
import com.shdh.dto.Payload;
import com.shdh.dto.Schema;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderProducer {
	private KafkaTemplate<String, String> kafkaTemplate;
	
	List<Field> fields = Arrays.asList(
			new Field("String", true, "order_id"),
			new Field("String", true, "user_id"),
			new Field("String", true, "product_id"),
			new Field("int32", true, "qty"),
			new Field("int32", true, "unit_price"),
			new Field("int32", true, "total_price")
			) ;
	
	Schema schema = Schema.builder()
			.type("struct")
			.fields(fields)
			.optional(false)
			.name("orders")
			.build();
	
	@Autowired
	public OrderProducer(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	

	public OrderDto send(String topic, OrderDto orderDto) {
		Payload payload = Payload.builder()
				.order_id(orderDto.getOrderId())
				.user_id(orderDto.getUserId())
				.product_id(orderDto.getProductId())
				.qty(orderDto.getQty())
				.unit_price(orderDto.getUnitPrice())
				.build();
		
		KafkaOrderDto kafkaorderDto = new KafkaOrderDto(schema, payload);
		
		
		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = "";
		try {
			jsonInString = mapper.writeValueAsString(kafkaorderDto);
		}catch (JsonProcessingException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		
		kafkaTemplate.send(topic,jsonInString);
		log.info("Order Producer sent data from the Order microservice" + kafkaorderDto);
		
		return orderDto; 
	}
}
