package com.example.CryptoTradingApplication.model;

import jakarta.persistence.*;

@Entity
@Table(name = "userWallet")
public class UserWalletModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userId", nullable = false)
    private String userId;

    @Column(name = "currency", nullable = false)
    private String currency;

    @Column(name = "balance",nullable = false)
    private Double balance;

    public UserWalletModel() {
    }

    public UserWalletModel(String userId, String currency, Double balance) {
        this.userId = userId;
        this.currency = currency;
        this.balance = balance;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }
}
