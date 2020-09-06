# kafka-connective-app-basics

技術書典 8 の技術同人誌『Kafka をはじめる』に掲載されているソースコードリポジトリです。

## リポジトリ構成

**アプリ**

MavenプロジェクトのJavaアプリケーションです。  
アプリの種類ごとにディレクトリが構成されています。

* `apps/kafka-client-basic`
  * ProducerとConsumerのプロジェクト
* `apps/kafka-connect-jdbc`
  * Kafka Connectワーカーを実行するプロジェクト
* `apps/kafka-streams-stateless`
  * 状態を持たないストリーム処理を実行したアプリケーションプロジェクト
* `apps/kafka-streams-stateful`
  * 状態を持ったストリーム処理を実行したアプリケーションプロジェクト


**環境構築**

各ディレクトリが構築方法に対応しています。

* `setup/docker`
* `setup/k8s`
* `setup/amazon-msk`
* `setup/ibm-event-streams`
