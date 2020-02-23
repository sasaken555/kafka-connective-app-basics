# Kafka クラスタを Docker Compose で構築する

## About

Kafka のクラスタを Docker Compose を使ってコンテナで立ち上げます。
Kafka の Docker コンテナは数多くありますが、本書では Confluent Platform のコンテナを使用します。

## How to use

```bash
# Docker Compose定義ファイルのディレクトリに移動
cd setup/docker

# クラスタを立ち上げる
docker-compose up

# クラスタの実行状態を確認。ここでState=Upになれば準備完了です。
docker-compose ps
```

## Docker Reference

Confluent 社の Kafka 関連 Docker コンテナのパラメータ設定は以下のドキュメントを参照ください。

- [Docker Configuration Parameters - Confluent Platform](https://docs.confluent.io/current/installation/docker/config-reference.html)

また、Confluent 社の Docker コンテナイメージはイメージごとにライセンスが定められています。
商用利用には注意が必要です。

- [Docker Image Reference - Confluent Platform](https://docs.confluent.io/current/installation/docker/image-reference.html)
