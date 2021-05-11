# Kafka Connect とリレーショナルデータベース

## About

Kafka Connect とリレーショナルデータベースを接続するサンプルです。

- リレーショナルデータベース

  - PostgreSQL 12

- Connector

  - Source Connector ... Kafka Connect JDBC
  - Sink Connector ... Kafka Connect JDBC

- Kafka Connect
  - コンテナイメージ ... `confluentinc/cp-kafka-connect:6.1.1`

## Usage

### Kafka Connect クラスタの構築

本リポジトリでは、Docker Compose を使用してクラスタを構築します。

`sedup/docker`ディレクトリに格納している YAML ファイル `docker-compose-connect.yaml` から Zookeeper、Broker、Kafka Connect、PostgreSQL をまとめて立ち上げ可能です。

```bash
> cd sedup/docker

> docker-compose -f docker-compose-connect.yaml up -d
zookeeper  ... done
broker     ... done
connect    ... done
postgresql ... done
```

### Source Connector

(1) Source Connector を作成する

```bash
# 事前にConnectorの定義ファイルを作成しておくこと
> curl -X POST \
    --url http://localhost:8083/connectors \
    -H 'content-type: application/json' \
    -d @create_source_connector.json \
    | jq .
```

(2) Connector の状態を確認する

```bash
> curl http://localhost:8083/connectors | jq .
[
  postgresql-sales-source-connector-01
]

> curl http://localhost:8083/connectors/postgresql-sales-source-connector-01/status | jq .
{
  "name": "postgresql-sales-source-connector-01",
  "connector": {
    "state": "RUNNING",
    "worker_id": "localhost:8083"
  },
  "tasks": [
    {
      "id": 0,
      "state": "RUNNING",
      "worker_id": "localhost:8083"
    }
  ],
  "type": "source"
}
```

(3) Connector を削除する

```bash
> curl -X DELETE http://localhost:8083/connectors/postgresql-sales-source-connector-01

> curl http://localhost:8083/connectors | jq .
[]  # Connectorが削除されて表示されない
```

### Sink Connector

(1) Sink Connector を作成する

```bash
# 事前にConnectorの定義ファイルを作成しておくこと
> curl -X POST \
    --url http://localhost:8083/connectors \
    -H 'content-type: application/json' \
    -d @create_sink_connector.json \
    | jq .
```

(2) Connector の状態を確認する

```bash
> curl http://localhost:8083/connectors | jq .
[
  postgresql-sales-sink-connector-02
]

> curl http://localhost:8083/connectors/postgresql-sales-source-connector-01/status | jq .
{
  "name": "postgresql-sales-sink-connector-02",
  "connector": {
    "state": "RUNNING",
    "worker_id": "localhost:8083"
  },
  "tasks": [
    {
      "id": 0,
      "state": "RUNNING",
      "worker_id": "localhost:8083"
    }
  ],
  "type": "sink"
}
```

(3) Connector を削除する

```bash
> curl -X DELETE http://localhost:8083/connectors/postgresql-sales-sink-connector-02

> curl http://localhost:8083/connectors | jq .
[]  # Connectorが削除されて表示されない
```
