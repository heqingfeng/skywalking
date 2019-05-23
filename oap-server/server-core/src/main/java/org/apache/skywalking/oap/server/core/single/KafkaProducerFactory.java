package org.apache.skywalking.oap.server.core.single;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KafkaProducerFactory {
	
	private static final Logger logger = LoggerFactory.getLogger(KafkaProducerFactory.class);

  
	private static KafkaProducer<String, String> kafkaProducer=null;
	private static volatile KafkaProducerFactory kafkaProducerFactory = null;
	
	private KafkaProducerFactory(){}
	
	static {
		Properties properties = KafkaProperties.getInstance();
        kafkaProducer = new KafkaProducer(properties);
	}
	public static  KafkaProducerFactory getInstance() {
	   if(kafkaProducerFactory==null) {
	        synchronized(KafkaProducerFactory.class) {
	            if(kafkaProducerFactory==null)
	            	kafkaProducerFactory=new KafkaProducerFactory();
	                return kafkaProducerFactory;
	        }
	   }
	   return kafkaProducerFactory;
	}	
	
	public void send(String topic, String value) {	   
	    try {
	        kafkaProducer.send(new ProducerRecord<String, String>(topic,value));
	    } catch (Exception e) {
	        logger.error("send kafka meswsage error,e="+e.getMessage());
	    }
	}
}
