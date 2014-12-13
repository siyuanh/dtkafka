package com.datatorrent.kafka.demos.wordcount;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kafka.producer.KeyedMessage;

import com.datatorrent.api.DefaultInputPort;
import com.datatorrent.contrib.kafka.AbstractKafkaOutputOperator;

public class KafkaWCDemoOutputOperator extends AbstractKafkaOutputOperator<Object, Object>
{

  private static final transient Logger logger = LoggerFactory.getLogger(KafkaWCDemoOutputOperator.class);
  
  public static final transient ObjectMapper mapper = new ObjectMapper();
  
  /**
   * This input port receives tuples that will be written out to Kafka.
   */
  public final transient DefaultInputPort<Object> inputPort = new DefaultInputPort<Object>()
  {
    
    
    @Override
    public void process(Object tuple)
    {
      // Send out single data
      try {
        getProducer().send(new KeyedMessage<Object, Object>(getTopic(), mapper.writeValueAsString(tuple)));
      } catch (Exception e) {
        logger.error("Format error", e);
      } 

    }
  };

  
 

}
