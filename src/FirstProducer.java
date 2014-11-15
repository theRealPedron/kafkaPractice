import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;


public class FirstProducer {

	public Producer<String,String> createProperties(){
		Properties props = new Properties();

		props.put("metadata.broker.list", "127.0.0.1:9092");
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");

		ProducerConfig config = new ProducerConfig(props);

		return new Producer<String, String>(config);
	}
	
	public void sendMessages(){
		
		for(int i=0;i<1000;i++){
			String message = "word up Kafka! message# " + i + "!";
			 KeyedMessage<String, String> data = new KeyedMessage<String, String>("page_visits", "1", message);
			 Producer<String, String> producer = createProperties();
			 producer.send(data);
		}
		 
	}
	
	public FirstProducer(){
		
	}
	
	public static void main(String args[]){
		FirstProducer producer = new FirstProducer();
		producer.sendMessages();
	}
}
