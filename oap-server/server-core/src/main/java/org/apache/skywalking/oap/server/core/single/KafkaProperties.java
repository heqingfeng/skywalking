package org.apache.skywalking.oap.server.core.single;

import java.util.Properties;

public class KafkaProperties extends Properties{

	private static final long serialVersionUID = 6552768121633576673L;
	
	public static final KafkaProperties Kafkaproperties = new KafkaProperties();
	
	private KafkaProperties(){}
	
	public static KafkaProperties getInstance() {
		return Kafkaproperties;
	}
	
}
