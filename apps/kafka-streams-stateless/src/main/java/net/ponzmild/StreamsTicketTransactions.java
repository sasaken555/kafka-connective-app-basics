package net.ponzmild;

import java.util.Properties;

import com.google.gson.Gson;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import net.ponzmild.models.TicketTransaction;

/**
 * StreamsTicketTransactions
 */
public class StreamsTicketTransactions {

  /**
   * エントリーポイント
   */
  public static void main(String[] args) {
    StreamsTicketTransactions engine = new StreamsTicketTransactions();
    engine.run();
  }

  public void run() {
    // Setup configs
    final String appId = System.getenv("APP_ID");
    final String bootstrapServers = System.getenv("BOOTSTRAP_SERVERS");
    final String inputTopic = "ticket-transaction";
    final String outputTopic = "valid-ticket-transaction";
    Properties streamsConfig = getStreamsConfig(appId, bootstrapServers);

    // Create Kakfa Streams application
    Topology topology = getTopology(inputTopic, outputTopic);
    KafkaStreams streams = new KafkaStreams(topology, streamsConfig);

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
  private Properties getStreamsConfig(String appId, String servers) {
    Properties config = new Properties();

    // Required general configs
    config.setProperty(StreamsConfig.APPLICATION_ID_CONFIG, appId);
    config.setProperty(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, servers);

    // Default serialize/deserialize configs
    config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    config.setProperty(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

    return config;
  }

  /**
   * Kafka Streamsアプリの処理フローを定義する
   * 
   * @param inputTopic  読み出し元のTopic
   * @param outputTopic 書き込み先のTopic
   * @return
   */
  private Topology getTopology(String inputTopic, String outputTopic) {
    StreamsBuilder builder = new StreamsBuilder();

    // Read records from topic
    KStream<String, String> inputStream = builder.stream(inputTopic);

    // Print records (key, value)
    KStream<String, String> sysoutStream = inputStream.peek((k, v) -> System.out.println("key=" + k + ", value=" + v));

    // Include records only type=Website
    Gson gson = new Gson();
    KStream<String, String> filteredStream = sysoutStream.filter((k, v) -> {
      TicketTransaction transaction = gson.fromJson(v, TicketTransaction.class);
      return transaction.getPurchaseType() == "1"; // Website
    });

    // Extract only amount
    KStream<String, Long> amountStream = filteredStream.mapValues(v -> {
      TicketTransaction transaction = gson.fromJson(v, TicketTransaction.class);
      return transaction.getAmount();
    });

    // Write records to another topic
    // override Serdes to <String, Long> instead of default <String, String>
    amountStream.to(outputTopic, Produced.with(Serdes.String(), Serdes.Long()));

    return builder.build();
  }
}
