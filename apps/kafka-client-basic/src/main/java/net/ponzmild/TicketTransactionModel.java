package net.ponzmild;

/**
 * TicketTransaction
 */
public class TicketTransactionModel {
  // チケット取引ID
  private String txId;
  // チケット購入金額
  private Long amount;
  // チケット購入区分 (Webサイト, 実店舗, etc.)
  private String purchaseType;

  TicketTransactionModel() {
  }

  public TicketTransactionModel(String txId, Long amount, String purchaseType) {
    this.txId = txId;
    this.amount = amount;
    this.purchaseType = purchaseType;
  }

  public String getTxId() {
    return txId;
  }

  public void setTxId(String txId) {
    this.txId = txId;
  }

  public Long getAmount() {
    return amount;
  }

  public void setAmount(Long amount) {
    this.amount = amount;
  }

  public String getPurchaseType() {
    return purchaseType;
  }

  public void setPurchaseType(String purchaseType) {
    this.purchaseType = purchaseType;
  }
}