package com.shdh.service;

import com.shdh.dto.OrderDto;
import com.shdh.jpa.OrderEntity;

public interface OrderService {
	
	OrderDto createOrder(OrderDto orderDto);

	OrderDto getOrderByOrderId(String orderId);
	
	Iterable<OrderEntity> getOrdersByUserId(String userId);
}
