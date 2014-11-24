dtkafka
=======

Cluster Details
-----------

### Zookeeper 
  * node0:2181

### Brokers
  * node0:9092,node1:9092,node2:9092,node3:9092


Kafka Setup
----------


## Creating the Kafka topics
```sh
    bin/kafka-topics.sh --create --zookeeper node0:2181 --replication-factor 2 --partition 1 --topic DTKafka
```

## Inspect data in topics
```sh
    bin/kafka-console-consumer.sh --zookeeper node0:2181 --topic DTKafka
```

## Increase the partition
```sh
    bin/kafka-topics.sh --alter --zookeeper node0:2181 --partition 2 --topic DTKafka
```

Managing DT App
------------

### Build the app
```sh
    mvn clean install -DskipTests
```

### Configuration Sample
    You can find the sample configuration in the package  META-INF/properties.xml
    
Example:

```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<configuration>
  <!-- kafka Ingestion application -->
  <property>
    <name>dt.operator.KafkaProducerOperator.prop.topic</name>
    <value>DTKafka</value>
  </property>
  <property>
    <name>dt.operator.KafkaProducerOperator.prop.brokerList</name>
    <value>node10:9092,node11:9092,node12:9092,node13:9092</value>
  </property>
  <property>
    <name>dt.operator.KafkaProducerOperator.prop.partitionNum</name>
    <value>1</value>
  </property>
  <property>
    <name>dt.operator.KafkaIngestionConsumerOperator.prop.topic</name>
    <value>DTKafka</value>
  </property>
  <property>
    <name>dt.operator.KafkaIngestionConsumerOperator.prop.brokerSet</name>
    <value>node10:9092,node11:9092,node12:9092,node13:9092</value>
  </property>
  <property>
    <name>dt.operator.KafkaIngestionConsumerOperator.prop.strategy</name>
    <value>one_to_one</value>
  </property>
  <property>
    <name>dt.operator.KafkaIngestionConsumerOperator.prop.initialOffset</name>
    <value>latest</value>
  </property>
  <property>
    <name>dt.application.kafka.consumertype</name>
    <value>simple</value>
  </property>
  <property>
    <name>dt.application.kafka.zookeeper</name>
    <value>node2:2181,node3:2181,node4:2181</value>
  </property>
</configuration>
```
    You need to change the brokerSet, brokerList, topics and zookeeper to run the package

### Run the package
    In dtcli, run
```sh
   launch-app-package path/to/the/package.jar

```


### Dynamically adjust the partition by adding kafka partition (dynamic deletion is not officially support in kafka)
```sh
    bin/kafka-topics.sh --alter --zookeeper node0:2181 --partition 3 --topic DTKafka
```

Design
------------

### Producer Operator
   * PartitionableKafkaOutputOperator is the producer output. 
   * You can setup partitionNum to scale up/down the partition number.
   * For each partition you can change the threadNum(how many threads for each partition) and interval(between 2 batch of messages) and tupleBlust(Number of tuples in each batch)  to control the throughput.

## Consumer Operator
   * PartitionableKafkaInputOperator directly extends AbstractPartitionableKafkaInputOperator(from Malhar library)
   * If you set the operator property "strategy" to "one_to_one"(case insensitive) The stram will dynamically allocate one operator partition for each kafka pertition. (You can dynamically change the kafka partition to see the partition change)
   * If you set the operator property  "strategy" to "one_to_many"(case insensitive) and set "msgRateUpperBound"/"byteRateUpperBound"(limit of msg/s, bytes/s per partition). It will dynamically allocate as few partitions as possible without breaking the limit (You can change the throughput of the Output Application to see the partition change)

