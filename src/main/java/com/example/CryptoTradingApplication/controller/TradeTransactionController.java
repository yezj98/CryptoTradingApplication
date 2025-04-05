package com.example.CryptoTradingApplication.controller;

import com.example.CryptoTradingApplication.model.TradeTransactionModel;
import com.example.CryptoTradingApplication.service.TradeTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TradeTransactionController {

    @Autowired
    private TradeTransactionService tradeTransactionService;

    @GetMapping("/history")
    public ResponseEntity <List<TradeTransactionModel>> getHistory (@RequestParam String userId) {
        try {
            List<TradeTransactionModel> transactions = tradeTransactionService.getTradingHistory(userId);
            return ResponseEntity.ok(transactions);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
