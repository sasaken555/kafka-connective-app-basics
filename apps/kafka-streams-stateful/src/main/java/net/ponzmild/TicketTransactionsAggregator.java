package net.ponzmild;

import java.util.Properties;

import com.google.gson.Gson;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.ponzmild.models.TicketTransaction;

/**
 * TicketTransactionsAggregator
 */
public class TicketTransactionsAggregator {

  final Logger logger = LoggerFactory.getLogger(TicketTransactionsAggregator.class);

  public static void main(String[] args) {
    TicketTransactionsAggregator engine = new TicketTransactionsAggregator();
    engine.run();
  }

  public void run() {
    final String appId = System.getenv("APP_ID");
    final String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
    final String inputTopic = "ticket-transaction";
    final String outputTopic = "calculated-ticket-transaction";

    // Create Client
    Properties streamsConfig = getStreamsConfig(appId, bootstrapServers);
    Topology topology = getTopology(inputTopic, outputTopic);
    KafkaStreams streams = new KafkaStreams(topology, streamsConfig);

    // Add hook to gracefully shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      logger.info("Graceful shutdown process...");
      streams.close();
    }));

    // Start application
    logger.info("Application Started!");
    streams.start();

    logger.info("Application stopped!");
  }

  private Properties getStreamsConfig(String appId, String servers) {
    Properties config = new Properties();

    // Required configs
    config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);

    // Serdes configs
    String stringSerde = Serdes.String().getClass().getName();
    config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, stringSerde);
    config.setProperty(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, stringSerde);

    return config;
  }

  private Topology getTopology(String inputTopic, String outputTopic) {
    StreamsBuilder builder = new StreamsBuilder();
    Gson gson = new Gson();

    // Read records from topic
    // key=movieId, value={"txId": "xxx", "amount": NNN, "purchaseType": "Z"}
    KStream<String, String> inputStream = builder.stream(inputTopic);

    // Extract amount from record
    KStream<String, Long> amountStream = inputStream
        .mapValues(v -> gson.fromJson(v, TicketTransaction.class).getAmount());

    // Group records by key
    KGroupedStream<String, Long> groupedStream = amountStream.groupByKey();

    // Aggregate amount with group
    KTable<String, Long> aggreatedTable = groupedStream.reduce((acc, v) -> acc + v);

    // Write records to another topic
    aggreatedTable.toStream().to(outputTopic);

    // Build topology
    return builder.build();
  }
}