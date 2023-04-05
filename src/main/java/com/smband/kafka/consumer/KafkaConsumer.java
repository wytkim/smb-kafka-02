/*
 * Copyright (c) 2015 SM band, Inc.
 * All right reserved.
 *
 * This software is the confidential and proprietary information of SM band
 * , Inc. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license agreement
 * you entered into with SM band.
 *
 */
package com.smband.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * 개요:
 * </pre>
 * @author ytkim
 * @create 2023. 4. 5.
 * @version 
 * @since 
 */
@Slf4j
@Service
@Data
public class KafkaConsumer {
	private String payload = null;
	
	@KafkaListener(topics = {"${smband.topics.users-registrations}"}, groupId = "${smband.kafka.group-id}", containerFactory = "kafkaListenerContainerFactory")
	public void consume(ConsumerRecord<?, ?> record) throws InterruptedException {
		log.info("consumer1 receive message: {}", record.toString());
		this.setPayload(record.value().toString());
	}
	
}
