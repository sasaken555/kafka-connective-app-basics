package net.ponzmild;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * トランザクションを使ったProducerクラス
 *
 * @author ponzmild
 */
public class TransactionalProducer {
    public static void main(String[] args) {
        // create producer
        final Logger logger = LoggerFactory.getLogger(net.ponzmild.Producer.class);
        final String bootstrapServers = "127.0.0.1:29092";
        final String topic = "sample-topic";
        KafkaProducer<String, String> producer = createProducer(bootstrapServers);

        // initialize transaction
        producer.initTransactions();

        // send message in transaction
        try {
            producer.beginTransaction();
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, "keyZ", "ValueVVV");
            producer.send(record);
            producer.commitTransaction();
        } catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e) {
            logger.warn("got error, that cannot be recovered automatically!");
        } catch (KafkaException ke) {
            ke.printStackTrace();
            producer.abortTransaction();
        } finally {
            producer.close();
        }
    }

    private static KafkaProducer<String, String> createProducer(String bootstrapServers) {
        Properties producerConfigs = createProducerConfig(bootstrapServers);
        return new KafkaProducer<>(producerConfigs);
    }

    private static Properties createProducerConfig(String bootstrapServers) {
        Properties configs = new Properties();

        // 必須項目
        configs.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        configs.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // トランザクション設定項目
        configs.setProperty(ProducerConfig.ACKS_CONFIG, "all");
        configs.setProperty(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
        configs.setProperty(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "sample-tx-id");

        return configs;
    }
}
