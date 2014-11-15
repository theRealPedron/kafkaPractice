package com.peter.kafka.demo.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;

public class MessageConsumer {

	private static SimpleConsumer consumer;
	public static SimpleConsumer getConsumerSingleton(){
		if(consumer==null){
			/*
			 * This is a default setup for now. It is rudimentary and fraught with peril, but it 
			 * will do for a first pass. A more robust approach will be forthcoming!
			 */
			consumer = new SimpleConsumer("127.0.0.1",9092, 100000, 64 * 1024, "peter");
		}
		return consumer;
	}
	
	public List<String> getTopicNames(){
		List<TopicMetadata> list = getTopicMetaData();
		List<String> topics = new ArrayList<String>();
		for(TopicMetadata current : list){
			topics.add(current.topic());
		}
		Collections.sort(topics);
		return topics;
	}
	
	public List<TopicMetadata> getTopicMetaData(){
        List<String> topics = new ArrayList<String>();
        TopicMetadataRequest req = new TopicMetadataRequest(topics);
        kafka.javaapi.TopicMetadataResponse resp = getConsumerSingleton().send(req);
        return resp.topicsMetadata();
	}
}
