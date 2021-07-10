package com.shdh.service;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shdh.dto.OrderDto;
import com.shdh.dto.OrderDto;
import com.shdh.jpa.OrderEntity;
import com.shdh.jpa.OrderRepository;
import com.shdh.jpa.OrderEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService{
	
	OrderRepository orderRepository;
	
	@Autowired
	public OrderServiceImpl(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}
	 

	@Override
	public OrderDto createOrder(OrderDto orderDto) {
		orderDto.setUserId(UUID.randomUUID().toString());
		orderDto.setTotalPrice(orderDto.getUnitPrice()*orderDto.getQty());
		
		// 데이터 베이스에 저장하기 위해 OrderEntity가 필요함.
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		OrderEntity orderEntity = mapper.map(orderDto, OrderEntity.class);
		
		
		// insert into database
		orderRepository.save(orderEntity);

		OrderDto returnOrderDto = mapper.map(orderEntity, OrderDto.class);

		return returnOrderDto;
		
	}


	@Override
	public OrderDto getOrderByOrderid(String orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public Iterable<OrderEntity> getOrderByUserId(String userId) {
		return orderRepository.findAll();
	}
}
