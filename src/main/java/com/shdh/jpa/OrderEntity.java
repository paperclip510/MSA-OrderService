package com.shdh.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.ColumnDefault;

import lombok.Data;

@Data
@Entity
@Table(name="orders")
public class OrderEntity implements Serializable{

	//직렬화 : 전송, 데이터베이스에 넣기위해서 마샬링, 언마샬링을 하기 위해 사용한다.
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 120, unique = true)
	private String productId;

	@Column(nullable = false)
	private Integer qty;

	@Column(nullable = false)
	private Integer unitPrice;

	@Column(nullable = false)
	private Integer totalPrice;

	
	@Column(nullable = false)
	private String userId;
	
	@Column(nullable = false, unique = true)
	private String orderId;


	
	
	@Column(nullable = false, updatable = false, insertable = false)
	@ColumnDefault(value = "CURRENT_TIMESTAMP")
	private Date createdAt;
}
 