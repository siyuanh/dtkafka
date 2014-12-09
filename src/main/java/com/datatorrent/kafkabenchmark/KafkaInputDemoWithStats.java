/*
 * Copyright (c) 2013 DataTorrent, Inc. ALL Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.datatorrent.kafkabenchmark;

import java.util.HashSet;
import java.util.Properties;

import org.apache.hadoop.conf.Configuration;

import com.datatorrent.api.DAG;
import com.datatorrent.api.StreamingApplication;
import com.datatorrent.api.annotation.ApplicationAnnotation;
import com.datatorrent.contrib.kafka.HighlevelKafkaConsumer;
import com.datatorrent.contrib.kafka.KafkaConsumer;
import com.datatorrent.contrib.kafka.SimpleKafkaConsumer;
import com.datatorrent.lib.stream.DevNullCounter;

@ApplicationAnnotation(name="KafkaIngestionDemoWithStats")
public class KafkaInputDemoWithStats implements StreamingApplication
{
  

  @Override
  public void populateDAG(DAG dag, Configuration conf)
  {
    
    dag.setAttribute(DAG.APPLICATION_NAME, "KafkaIngestionDemoWithStats");
    PartitionableKafkaInputOperator bpkio = new PartitionableKafkaInputOperator();
    
    
    String type = conf.get("dt.application.kafka.consumertype");
    
    KafkaConsumer consumer = null;
    
    
    if (type!=null && type.equals("highlevel")) {
      // Create template high-level consumer

      Properties props = new Properties();
      props.put("zookeeper.connect", conf.get("dt.application.kafka.zookeeper"));
      props.put("group.id", "dtkafka_group");
      props.put("auto.offset.reset", "smallest");
      consumer = new HighlevelKafkaConsumer(props);
    } else {
      // topic is set via property file
      consumer = new SimpleKafkaConsumer(null, 10000, 100000, "test_kafka_autop_client", new HashSet<Integer>());
    }
    
    bpkio.setTuplesBlast(1024 * 1024);
    bpkio.setConsumer(consumer);
    bpkio = dag.addOperator("KafkaIngestionConsumerOperator", bpkio);

    
    DevNullCounter<String> nullCounter = dag.addOperator("NullCounter", new DevNullCounter<String>());
    dag.addStream("InputToCounter", bpkio.oport, nullCounter.data);
 

  }

}
