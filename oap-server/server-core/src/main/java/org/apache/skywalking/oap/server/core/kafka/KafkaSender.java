package org.apache.skywalking.oap.server.core.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaSender {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaSender.class);

	private static volatile KafkaProducer<String, String> kafkaProducer=null;
	
	private KafkaSender(){}
	
	public static  KafkaProducer getKafkaProducer() {
		if(kafkaProducer==null) {
		      synchronized(KafkaSender.class) {
		           if(kafkaProducer==null) {
		        	   Properties properties = KafkaProperties.getKafkaProperties();
		        	   kafkaProducer=new KafkaProducer<String, String>(properties);
		           }		        	  
		           return kafkaProducer;
		      }
		 }
		 return kafkaProducer;
	}
	
	public static void send(String message) {
		Properties properties = KafkaProperties.getKafkaProperties();
		try {
			kafkaProducer.send(new ProducerRecord<String, String>(properties.getProperty("topic"),message));
		}catch(Exception e) {
			logger.error("kafka send message error,e="+e.getMessage());
		}
    	
	}
}
