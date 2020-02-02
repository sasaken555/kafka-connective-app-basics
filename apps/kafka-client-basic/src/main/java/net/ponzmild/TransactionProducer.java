package net.ponzmild;

import java.util.Properties;

import com.google.gson.Gson;

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
public class TransactionProducer {

  public static void main(final String[] args) {
    final Logger logger = LoggerFactory.getLogger(Producer.class);
    final String bootstrapServers = "127.0.0.1:29092";
    final String topic = "ticket-transaction";
    final Gson gson = new Gson();

    // Create producer
    final KafkaProducer<String, String> producer = createProducer(bootstrapServers);

    // create and send data
    String txId = "54255724-b2ae-4e81-adc4-7179282032e6";
    TicketTransactionModel tx = new TicketTransactionModel(txId, 6000L, "1");
    final ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, txId, gson.toJson(tx));

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
    configs.setProperty(ProducerConfig.ACKS_CONFIG, "all"); // acks after persisted in all replica
    configs.setProperty(ProducerConfig.RETRIES_CONFIG, "5");

    return new KafkaProducer<String, String>(configs);
  }
}