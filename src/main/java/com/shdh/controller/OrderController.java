package com.shdh.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shdh.dto.OrderDto;
import com.shdh.jpa.OrderEntity;
import com.shdh.messagequeue.KafkaProducer;
import com.shdh.messagequeue.OrderProducer;
import com.shdh.service.OrderService;
import com.shdh.vo.RequestOrder;
import com.shdh.vo.ResponseOrder;

@RestController
@RequestMapping(value = "/order-service/**")
public class OrderController {
	Environment env;
	OrderService orderService;
	KafkaProducer kafkaProducer;
	OrderProducer orderProducer;
	
	@Autowired
	public OrderController(Environment env, OrderService orderService, KafkaProducer kafkaProducer, OrderProducer orderProducer) {
		this.env = env;
		this.orderService = orderService;
		this.kafkaProducer = kafkaProducer;
		this.orderProducer = orderProducer;
	}
	
	@GetMapping("/health_check")
	public String healthCheck() {
		return String.format("It's Working in Order Servie on PORT %s", env.getProperty("local.server.port"));
	}
	
	@PostMapping("/{userId}/orders")
	public ResponseEntity<ResponseOrder> createOrder(@RequestBody RequestOrder requestOrder, @PathVariable("userId") String userId) {
		
		
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		
		OrderDto orderDto = mapper.map(requestOrder, OrderDto.class);
		orderDto.setUserId(userId);
		
		// jpa 주문 등록 
		OrderDto createOrder = orderService.createOrder(orderDto);		
		ResponseOrder responseOrder = mapper.map(createOrder, ResponseOrder.class);
		
		// kafka
		orderDto.setOrderId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(requestOrder.getUnitPrice()*requestOrder.getQty());
		
		// send this order to the kafka (send topic)
//		kafkaProducer.send("example-catalog-topic", orderDto);
//		orderProducer.send("orders", orderDto);
		
		// sink connect 추가 
		// kafka connect 실행  ./bin/connect-distributed ./etc/kafka/connect-distributed.properties
		
//		ResponseOrder responseOrder = mapper.map(orderDto, ResponseOrder.class);

		//responseUser를 responseEntity body에 넣어서 반환.
		return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
	}
	

	@GetMapping("/{userId}/orders")
	public ResponseEntity<List<ResponseOrder>> getUsers(@PathVariable("userId") String userId){
		Iterable<OrderEntity> userList = orderService.getOrdersByUserId(userId);
		
		List<ResponseOrder> result = new ArrayList<>();
		userList.forEach(item -> {
			result.add(new ModelMapper().map(item, ResponseOrder.class));
		});
		
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
	
	
}
