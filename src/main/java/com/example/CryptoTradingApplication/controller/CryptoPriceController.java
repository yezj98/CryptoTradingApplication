package com.example.CryptoTradingApplication.controller;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import com.example.CryptoTradingApplication.respository.CryptoPriceSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/crypto/prices")
public class CryptoPriceController {
    @Autowired
    private CryptoPriceSourceRepository cryptoPriceSourceRepository;

    @GetMapping("/latest")
    public ResponseEntity<Map<String, CryptoPriceModel>> getLatestPrices(){
        Map<String, CryptoPriceModel> latestPrice = new HashMap<>();

        String[] symbols = {"ETHUSDT", "BTCUSDT"};

        for (String symbol : symbols) {
            CryptoPriceModel price = cryptoPriceSourceRepository.findTopBySymbolOrderByTimestampDesc(symbol).orElse(null);
            if (price != null) {
                latestPrice.put(symbol, price);
            }
        }

        if (latestPrice.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(latestPrice);
    }

}
