package org.example;

public class OrderInfo {
    private String productName;
    private Integer amount;
    private Long timestamp;

    public OrderInfo(String productName, Integer amount, Long timestamp) {
        this.productName = productName;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public String getProductName() {
        return productName;
    }

    public int getAmount() {
        return amount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "productName='" + productName + '\'' +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}
