package org.apache.skywalking.oap.server.core.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerFactory {
	
	private static volatile KafkaProducer<String, String> kafkaProducer=null;
	
	private KafkaProducerFactory(){}
	
	public static  KafkaProducer getKafkaProducer(Properties properties) {
		if(kafkaProducer==null) {
		      synchronized(KafkaProducerFactory.class) {
		           if(kafkaProducer==null)
		        	   kafkaProducer=new KafkaProducer<String, String>(properties);
		           return kafkaProducer;
		      }
		 }
		 return kafkaProducer;
	}
}
