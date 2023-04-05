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
package com.smband.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.smband.commons.util.StringUtil;

import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Component
public class KafkaProducer {
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	public void send(String topic, String payload) {
		log.info("sending payload='{}' to topic='{}'", payload, topic);
		ListenableFuture<SendResult<String, String>> future =kafkaTemplate.send(topic, payload);
		future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

			@Override
			public void onSuccess(SendResult<String, String> result) {
				log.info("success: {}", result);
			}

			@Override
			public void onFailure(Throwable ex) {
				log.warn("send fail exception:{}", StringUtil.exceptionMessage(ex));
			}
		});
	}
}
