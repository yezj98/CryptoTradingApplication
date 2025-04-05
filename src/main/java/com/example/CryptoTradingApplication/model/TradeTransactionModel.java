package com.example.CryptoTradingApplication.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "TRADE_TRANSACTION")
public class TradeTransactionModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    @Column(name = "USER_ID", nullable = false, length = 40)
    private String userId;

    @Column(name = "SYMBOL", nullable = false, length = 20)
    private String symbol;

    @Column(name = "ACTION", nullable = false, length = 4)
    private String action;

    @Column(name = "QUANTITY", nullable = false)
    private Double quantity;

    @Column(name = "PRICE", nullable = false)
    private Double price;

    @Column(name = "TIMESTAMP", nullable = true)
    private java.sql.Timestamp timestamp;

    public TradeTransactionModel() {
    }

    public TradeTransactionModel(String userId, String symbol, String action, Double quantity, Double price, java.sql.Timestamp timestamp) {
        this.userId = userId;
        this.symbol = symbol;
        this.action = action;
        this.quantity = quantity;
        this.price = price;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
