package net.ponzmild;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

/**
 * Consumer
 */
public class Consumer {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(Consumer.class);
        final String bootstrapServers = "127.0.0.1:29092";
        final String groupId = "G1";
        final String topic = "sample-topic";

        // Create consumer
        KafkaConsumer<String, String> consumer = createConsumer(bootstrapServers, groupId);
        logger.info("Consumer created");

        // Register shutdown process
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Start to exit...");
            consumer.wakeup();
        }));

        // Subscribe topics
        consumer.subscribe(Arrays.asList(topic));

        try {
            while (true) {
                logger.info("-------- loop --------");
                // 1秒お気にレコードを読み出し
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1L));

                for (ConsumerRecord<String, String> record : records) {
                    String recordTopic = record.topic();
                    int recordPartition = record.partition();
                    String recordKey = record.key();
                    String recordVal = record.value();
                    logger.info("Received record: Topic={}, Partition={}, Key={}, Value={}", recordTopic, recordPartition,
                            recordKey, recordVal);
                }
            }
        } catch (WakeupException wue) {
            // Nothing to do!
        } finally {
            logger.info("Closing consumer...");
            consumer.close();
        }
    }

    private static KafkaConsumer<String, String> createConsumer(String bootstrapServers, String groupId) {
        final String deserializerName = StringDeserializer.class.getName();
        Properties configs = new Properties();

        // 必須の設定項目
        configs.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, deserializerName);
        configs.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializerName);
        configs.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // 任意の設定項目
        configs.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // 最初から読み出す

        return new KafkaConsumer<String, String>(configs);
    }
}