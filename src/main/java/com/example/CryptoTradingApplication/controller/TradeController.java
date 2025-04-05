package com.example.CryptoTradingApplication.controller;

import com.example.CryptoTradingApplication.model.TradeTransactionModel;
import com.example.CryptoTradingApplication.service.TradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/transaction")
    public ResponseEntity<?> executeTrade(
            @RequestParam String userId,
            @RequestParam String symbol,
            @RequestParam String orderType,
            @RequestParam Double quantity) {
        try {
            TradeTransactionModel transaction = tradeService.executeTrade(userId, symbol, orderType, quantity);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
