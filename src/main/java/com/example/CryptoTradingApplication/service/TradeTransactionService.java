package com.example.CryptoTradingApplication.service;

import com.example.CryptoTradingApplication.model.TradeTransactionModel;
import com.example.CryptoTradingApplication.respository.TradeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeTransactionService {
    @Autowired
    private final TradeTransactionRepository tradeTransactionRepository;

    public TradeTransactionService(TradeTransactionRepository tradeTransactionRepository) {
        this.tradeTransactionRepository = tradeTransactionRepository;
    }

    public List<TradeTransactionModel> getTradingHistory(String userId) {
        List<TradeTransactionModel> transactions = tradeTransactionRepository.findByUserIdOrderByTimestampDesc(userId);
        if (transactions.isEmpty()) {
            throw new RuntimeException(userId + " has no recorded trading history");
        }
        return transactions;
    }
}
