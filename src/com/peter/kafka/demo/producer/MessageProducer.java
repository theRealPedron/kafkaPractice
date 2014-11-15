package com.peter.kafka.demo.producer;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.ProducerConfig;

public class MessageProducer {
	
	public static Producer<String,String> producer;
	public MessageProducer(){
		
	}
	
	/*
	 * Currently, for first release, we have just one singleton producer, which means we are restricted
	 * to a single Kafka server... for now. If the need arises, we will add messaging to a different server
	 * but that will be in a future release.
	 */
	public static Producer<String,String>getProducerSingleton(){
		if(producer==null){
			producer = createProperties();
		}
		return producer;
	}
	private static Producer<String,String> createProperties(){
		Properties props = new Properties();
		//currently we'll just check our local machine. we'll eventually 
		//add a way to alter this on the gui. this will be updated in a future release..
		props.put("metadata.broker.list", "127.0.0.1:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		return new Producer<String, String>(config);
	}
}
