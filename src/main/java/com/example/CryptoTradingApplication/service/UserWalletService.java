package com.example.CryptoTradingApplication.service;

import com.example.CryptoTradingApplication.model.UserWalletModel;
import com.example.CryptoTradingApplication.respository.UserWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserWalletService {

    @Autowired
    private final UserWalletRepository userWalletRepository;

    public UserWalletService(UserWalletRepository userWalletRepository) {
        this.userWalletRepository = userWalletRepository;
    }

    public List<UserWalletModel> getWalletBalances(String userId) {
        List<UserWalletModel> wallets = userWalletRepository.findByUserId(userId);
        if (wallets.isEmpty()) {
            System.err.println("No wallet found");
        }
        return wallets;
    }
}
