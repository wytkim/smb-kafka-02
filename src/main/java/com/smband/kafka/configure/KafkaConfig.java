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
package com.smband.kafka.configure;

import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.smband.kafka.common.SimpleUtil;

import lombok.Setter;
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
@Setter
@Configuration
@ConfigurationProperties(prefix = "smband.kafka")
@EnableKafka
public class KafkaConfig {
	private String bootstrapServers = "127.0.0.1:9092";
	private String groupId = "smb-group";
	private String autoOffsetReset = "earliest";

	// consumer 구성하기
	@Bean
	public ConsumerFactory<String, String> consumerFactory(){
		StringDeserializer deserializer = new StringDeserializer();
		return new DefaultKafkaConsumerFactory<>(
				smbConsumerFactoryConfig(deserializer),
				new StringDeserializer(),
				deserializer
				);
	}
	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(
			ConsumerFactory<String, String> consumerFactory
			){
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}
	
	// Producer 구성하기.
	@Bean
	public ProducerFactory<String, String> producerFactory(){
		return new DefaultKafkaProducerFactory<>(producerFactoryConfig());
	}
	
	@Bean
	public KafkaTemplate<String, String> kafkaTemplate(ProducerFactory<String, String>  producerFactory){
		log.info("kafkaTemplate Bean 구성!");
		return new KafkaTemplate<>(producerFactory);
	}
	
	private Map<String, Object> producerFactoryConfig(){
		return SimpleUtil.newMap(
				ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
				ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
				);
	}
	
	private Map<String, Object> smbConsumerFactoryConfig(Deserializer<? extends Object> deserializer){
		Map<String, Object> props = SimpleUtil.newMap(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
				ConsumerConfig.GROUP_ID_CONFIG, groupId,
				ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset,
				ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
				ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer
				);
		return props;
	}
	
}
