package net.ponzmild;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Producer
 */
public class Producer {

    public static void main(final String[] args) {
        final Logger logger = LoggerFactory.getLogger(Producer.class);
        final String bootstrapServers = "127.0.0.1:29092";
        final String topic = "sample-topic";

        // Create producer
        final KafkaProducer<String, String> producer = createProducer(bootstrapServers);

        // create and send data
        final ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "product-001", "100");

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(final RecordMetadata metadata, final Exception e) {
                if (e == null) {
                    logger.info("sent record: topic={}, partition={}, offset={}", metadata.topic(), metadata.partition(),
                            metadata.offset());
                } else {
                    e.printStackTrace();
                    logger.error("got error w/ send()", e);
                }
            }
        });

        // flush and close producer
        producer.flush();
        producer.close();
    }

    private static KafkaProducer<String, String> createProducer(final String bootstrapServers) {
        final Properties configs = new Properties();

        // 必須の設定項目
        configs.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 任意の設定項目
        configs.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // acks after persisted in all replica
        configs.setProperty(ProducerConfig.RETRIES_CONFIG, "5");

        return new KafkaProducer<String, String>(configs);
    }
}