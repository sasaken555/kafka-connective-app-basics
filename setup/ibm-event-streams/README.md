# Kafka クラスタを IBM Cloud Event Streams で構築する

## About

IBM Event Streams は IBM Cloud で使用できる Apache Kafka のマネージドサービスです。

## ウマミ

### 選択プラン

- プロビジョニングできるリソースの制限・料金ごとにプランを選択できます。
  - ライト・プラン
  - 標準・プラン
  - エンタープライズ・プラン
- 中でもライト・プランはクレジットカードなしで 30 日間無料で利用可能なクラスタを構築可能です。
- ライトプランは安価な分、最大 Parition 数=1、Connect API と Streams API の利用不可といった制約があります。

### サービス統合

- IKS 上に Kafka Connect クラスタを立てることで、以下のサービスの Connector を利用可能になります。

  - Cloud Object Storage Sink Connector
  - IBM MQ Source Connector

- Activity Tracker 統合
  - クラスタの操作イベントを追跡可能になります。
  - トピック作成、削除、Key Protect の鍵の変更イベントを追跡可能です。
  - 監査目的で利用すると良いでしょう。

## Usage

### コンソールから操作

- クラスタの作成・削除・Topic 管理を GUI から操作できます。

### CLI (es cli)から操作

- IBM Cloud CLI のプラグインとして、 `ibmcloud es` コマンドから Event Streams を操作できます。
- 詳細は[Event Streams CLI の概要](https://cloud.ibm.com/docs/services/EventStreams?topic=eventstreams-cli)を参照してください。

### REST API から操作

- Event Streams では Kafka の Admin API をラップした管理用の REST API が提供されています。
- 詳細は[IBM Event Streams 管理 REST API の使用](https://cloud.ibm.com/docs/services/EventStreams?topic=eventstreams-admin_api)を参照してください。
