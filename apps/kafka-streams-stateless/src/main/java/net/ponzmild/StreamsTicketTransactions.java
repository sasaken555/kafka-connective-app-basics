package net.ponzmild;

import java.util.Properties;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

/**
 * StreamsTicketTransactions
 */
public class StreamsTicketTransactions {

  /**
   * エントリーポイント
   */
  public static void main(String[] args) {
    // Setup configs
    final String appId = System.getenv("APP_ID");
    final String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
    final String inputTopic = "ticket-transaction";
    final String outputTopic = "valid-ticket-transaction";

    // Create Kakfa Streams topology
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, Long> inputStream = builder.stream(inputTopic);
    KStream<String, Long> filteredStream = inputStream.filter((k, v) -> v > 0L);
    KStream<String, Long> mappedStream = filteredStream.mapValues(v -> (v / 1000L));
    mappedStream.to(outputTopic);

    // Create Kakfa Streams application
    Properties streamsConfig = getStreamsConfig(appId, bootstrapServers);
    KafkaStreams streams = new KafkaStreams(builder.build(), streamsConfig);

    // Add hook for graceful shutdown
    Runtime.getRuntime().addShutdownHook(new Thread(streams::close));

    // Start application
    streams.start();
  }

  /**
   * Kafka Streamsアプリケーションの設定値オブジェクトを取得する。
   * 
   * @param appId   アプリケーションID
   * @param servers Bootstrapサーバ (カンマで区切る)
   * @return 設定値オブジェクト
   */
  private static Properties getStreamsConfig(String appId, String servers) {
    Properties config = new Properties();

    // Required general configs
    config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);

    // Default serialize/deserialize configs
    config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Long().getClass().getName());

    return config;
  }
}
