package com.shdh.service;

import com.shdh.dto.OrderDto;
import com.shdh.jpa.OrderEntity;

public interface OrderService {
	
	OrderDto createOrder(OrderDto orderDto);

	OrderDto getOrderByOrderid(String orderId);
	
	Iterable<OrderEntity> getOrderByUserId(String userId);
}
