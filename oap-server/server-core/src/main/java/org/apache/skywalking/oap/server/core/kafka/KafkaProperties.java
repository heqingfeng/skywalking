package org.apache.skywalking.oap.server.core.kafka;

import java.util.Properties;

public class KafkaProperties{
	
	private static final Properties Kafkaproperties = new Properties();
	
	private KafkaProperties(){}
	
	public static Properties getKafkaProperties() {
		return Kafkaproperties;
	}
	
}
