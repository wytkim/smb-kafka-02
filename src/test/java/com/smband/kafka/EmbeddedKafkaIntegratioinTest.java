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
package com.smband.kafka;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
//import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import com.smband.kafka.consumer.KafkaConsumer;
import com.smband.kafka.producer.KafkaProducer;

/**
 * <pre>
 * 개요:
 * </pre>
 * @author ytkim
 * @create 2023. 4. 5.
 * @version 
 * @since 
 */
@SpringBootTest
//@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {
		"listeners=PLAINTEXT://localhost:9093", "port=9093"
})
@TestPropertySource(properties = {
		"spring.config.location=classpath:application-test.yml"
})
public class EmbeddedKafkaIntegratioinTest {

	@Autowired
	private KafkaConsumer consumer;
	@Autowired
	private KafkaProducer producer;
	
	@Value("${smband.topics.users-registrations}")
	private String topic;
	
	@Test
	public void givenEmbeddedKafkaBroker_whenSendingtoSimpleProducer_thenMessageReceived() 
      throws Exception {
        String body = "Sending with own simple KafkaProducer";
		producer.send(topic, body);
        Thread.sleep(1000);
        assertEquals(body, consumer.getPayload(), "producer 값과 consumer 값은 동일하다.");
        //consumer.getLatch().await(10000, TimeUnit.MILLISECONDS);
        
        //assertThat(consumer.getLatch().getCount(), equalTo(0L));
        //assertThat(consumer.getPayload(), containsString("embedded-test-topic"));
        
    }

}
