# Docker ComposeでKafkaクラスタを立てる

## About

KafkaのクラスタをDocker Composeを使ってコンテナで立ち上げます。
KafkaのDockerコンテナは数多くありますが、本書ではConfluent Platformのコンテナを使用します。

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

* Confluent社のKafka関連Dockerコンテナのパラメータ設定は以下のドキュメントを参照ください。
  * [Docker Configuration Parameters - Confluent Platform](https://docs.confluent.io/current/installation/docker/config-reference.html)

* また、Confluent社のDockerコンテナにはそれぞれライセンスが定められているので、商用利用には注意が必要です。
  * [Docker Image Reference - Confluent Platform](https://docs.confluent.io/current/installation/docker/image-reference.html)
