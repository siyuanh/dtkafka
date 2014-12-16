package com.datatorrent.kafka.demos.wordcount;

import java.util.HashSet;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.Context.PortContext;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.contrib.kafka.*;
import com.datatorrent.kafkabenchmark.PartitionableKafkaInputOperator;
import com.datatorrent.lib.algo.UniqueCounter;

@ApplicationAnnotation(name="KafkaWordcount")
public class Application implements StreamingApplication
{
  
  @Override
 public void populateDAG(DAG dag, Configuration conf)
 {
   PartitionableKafkaInputOperator kafkainput = dag.addOperator("kafkawordinput", new PartitionableKafkaInputOperator());
   KafkaConsumer consumer = new SimpleKafkaConsumer(null, 10000, 100000, "test_kafka_autop_client", new HashSet<Integer>());
   
   kafkainput.setConsumer(consumer);
   UniqueCounter<String> wordCount = dag.addOperator("count", new UniqueCounter<String>());
   dag.addStream("kafkawordinput-count", kafkainput.oport, wordCount.data);
   
   KafkaWCDemoOutputOperator  demooutput = dag.addOperator("kafkawordcountoutput", new KafkaWCDemoOutputOperator());
   dag.addStream("kafkawordcount-output", wordCount.count, demooutput.inputPort);

 }

}
