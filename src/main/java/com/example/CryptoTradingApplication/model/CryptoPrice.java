package com.example.CryptoTradingApplication.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
public class CryptoPrice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "SYMBOL", nullable = false, length = 20)
    private String symbol;

    @Column(name = "BID_PRICE", nullable = false)
    private Double bidPrice;

    @Column(name = "ASK_PRICE", nullable = false)
    private Double askPrice;

    @Column(name = "SOURCE", nullable = false, length = 10)
    private String source;

    @Column(name = "TIMESTAMP", nullable = false)
    private Timestamp timestamp;

    public CryptoPrice() {}

    public CryptoPrice(String symbol, Double bidPrice, Double askPrice, String source, Timestamp timestamp) {
        this.id = id;
        this.symbol = symbol;
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.source = source;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(Double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(Double askPrice) {
        this.askPrice = askPrice;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
