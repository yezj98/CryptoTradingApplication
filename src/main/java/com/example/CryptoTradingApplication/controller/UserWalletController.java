package com.example.CryptoTradingApplication.controller;

import com.example.CryptoTradingApplication.model.UserWalletModel;
import com.example.CryptoTradingApplication.service.UserWalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/wallet")
public class UserWalletController {

    @Autowired
    private UserWalletService userWalletService;

    @GetMapping("/balance")
    public ResponseEntity<List<UserWalletModel>> getWalletBalances(@RequestParam String userId) {
        try {
            List<UserWalletModel> balance = userWalletService.getWalletBalances(userId);
            return ResponseEntity.ok(balance);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
