package net.ponzmild;

import java.util.Properties;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer
 */
public class Producer {

  public static void main(final String[] args) {
    final Logger logger = LoggerFactory.getLogger(Producer.class);
    final String bootstrapServers = "127.0.0.1:29092";
    final String topic = "ticket-transaction";

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

    configs.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
    configs.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    configs.setProperty(ProducerConfig.ACKS_CONFIG, "1"); // acks after persisted in leader-replica

    return new KafkaProducer<String, String>(configs);
  }
}