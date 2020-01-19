package net.ponzmild;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Consumer
 */
public class Consumer {
  public static void main(String[] args) {
    final Logger logger = LoggerFactory.getLogger(Consumer.class);
    final String bootstrapServers = "127.0.0.1:29092";
    final String groupId = "G1";
    final String topic = "ticket-transaction";

    KafkaConsumer<String, String> consumer = createConsumer(bootstrapServers, groupId);
    logger.info("Consumer created");

    consumer.subscribe(Arrays.asList(topic));
    logger.info("Subscribed topics : %s", topic);

    // 10回だけTopicからレコードを読み出し
    for (int i = 0; i < 10; i++) {
      // 1秒お気にレコードを読み出し
      ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));

      for (ConsumerRecord<String, String> record : records) {
        String recordTopic = record.topic();
        int recordPartition = record.partition();
        String recordKey = record.key();
        String recordVal = record.value();
        logger.info("Received record: Topic=%s, Partition=%d, Key=%s, Value=%s", recordTopic, recordPartition,
            recordKey, recordVal);
      }
    }

    System.exit(0);
  }

  private static KafkaConsumer<String, String> createConsumer(String bootstrapServers, String groupId) {
    final String deserializerName = StringDeserializer.class.getName();
    Properties configs = new Properties();

    configs.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializerName);
    configs.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerName);
    configs.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

    return new KafkaConsumer<String, String>(configs);
  }
}