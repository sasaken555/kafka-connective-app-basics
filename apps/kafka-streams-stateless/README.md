# Kafka Streams Application (Stateless)

## About

Kafka Streamsアプリケーションのサンプルアプリです。
以下の操作を学びます。

* `mapValue`, `filter`, `peek`といった到着したイベントを加工操作する。

* イベントの値を合計といった集約操作は別のアプリを参照してください。

## How to use

### Dockerコンテナで動かす場合

```bash
mvn compile jib:dockerBuild
...
-------------------------
BUILD SUCCESS
-------------------------

docker image ls
REPOSITORY                TAG      IMAGE ID       CREATED        SIZE
kafka-streams-stateless   latest   08e87b501d78   50 years ago   222MB

docker run --rm -e APP_ID="this-is-sample" BOOTSTRAP_SERVERS="broker:9092" kafka-streams-stateless
```

### Visual Studio Codeで動かす場合

`StreamsTicketTransactions.java` のmainメソッドの上に表示される `Run|Debug` ボタンを押す。
