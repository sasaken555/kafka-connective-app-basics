# Kafka クラスタを Amazon MSK で構築する

## About

Amazon Managed Streaming for Apache Kafka (= Amazon MSK)は Apache Kafka のマネージドサービスです。
Apache Zookeeper と Apache Kafka から Kafka クラスタを構築する手間を AWS に移譲することで運用負荷を劇的に減らせます。

## ウマミ

AWS のサービスと統合されており、以下の恩恵を受けられます。

### Amazon VPC との統合

- クラスタはユーザー作成の VPC 内に構築されます。
- Broker を複数の Availability Zone に配置して耐障害性を向上させます。
- また、セキュリティグループを設定してきめ細やかなアクセス制御を実施可能です。

### AWS IAM との統合

- Amazon MSK の操作は IAM のポリシーで設定可能です。
- クラスタに接続するクライアントに必要最低限のアクセス許可を持ったポリシーをアタッチすることで、悪意を持ったクライアントからのアクセスからクラスタを守ることが可能です。

### AWS KMS との統合

- 暗号化キーとして KMS の CMS(カスタマーマスターキー)を利用可能です。
- クラスタとクライアント、およびクラスタ内の通信を暗号化して安全な通信を確率します。

### Amazon CloudWatch との統合

- MSK は CloudWatch に自動的に Broker レベルとクラスタレベルのメトリクスを送信します。
- CloudWatch アラームを設定することで、Broker や Partition の増減を判断できます。

## Usage

最も簡単なセットアップ方法は公式ドキュメントの [Amazon MSK の開始方法](https://docs.aws.amazon.com/ja_jp/msk/latest/developerguide/getting-started.html)を参照してください。
参考までに、以下のサンプルの設定ファイルをこのディレクトリに格納しています。

- brokernodegroupinfo.json

  - Broker の設定ファイルです。
  - Broker は EC2 インスタンス上に構築されます。
  - EC2 インスタンスのインスタンスタイプ、サブネット、セキュリティグループを設定します。

- encryptioninfo.json
  - 暗号化の設定ファイルです。
  - クラスタ内の Broker 間の通信の暗号化設定を定義しています。
