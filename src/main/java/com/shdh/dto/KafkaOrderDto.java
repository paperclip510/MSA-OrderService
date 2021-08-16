package com.shdh.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KafkaOrderDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5540395887865531617L;
	
	private Schema schema;
	private Payload payload;
}
