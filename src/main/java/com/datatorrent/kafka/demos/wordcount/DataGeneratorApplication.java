package com.datatorrent.kafka.demos.wordcount;



import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.DAG.Locality;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.Context.PortContext;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.contrib.kafka.KafkaSinglePortOutputOperator;

@ApplicationAnnotation(name="WordOutputToKafka")
public class DataGeneratorApplication implements StreamingApplication
{
   @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    WordCountInputOperator input = dag.addOperator("wordinput", new WordCountInputOperator());
    
    KafkaSinglePortOutputOperator<Object, String> kspoo = dag.addOperator("kafkawordoutput", new KafkaSinglePortOutputOperator<Object, String>());
    
    dag.addStream("wordinput-kafka", input.outputPort, kspoo.inputPort).setLocality(Locality.CONTAINER_LOCAL);

    dag.setInputPortAttribute(kspoo.inputPort, PortContext.PARTITION_PARALLEL, true);

  }


}