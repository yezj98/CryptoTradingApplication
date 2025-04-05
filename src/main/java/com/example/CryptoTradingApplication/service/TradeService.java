package com.example.CryptoTradingApplication.service;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import com.example.CryptoTradingApplication.model.TradeTransactionModel;
import com.example.CryptoTradingApplication.model.UserWalletModel;
import com.example.CryptoTradingApplication.respository.CryptoPriceRespository;
import com.example.CryptoTradingApplication.respository.TradeTransactionRepository;
import com.example.CryptoTradingApplication.respository.UserWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Optional;

@Service
public class TradeService {

    @Autowired
    private final CryptoPriceRespository cryptoPriceRespository;

    @Autowired
    private final TradeTransactionRepository tradeTransactionRepository;

    @Autowired
    private final UserWalletRepository userWalletRepository;

    public TradeService(CryptoPriceRespository cryptoPriceRespository, TradeTransactionRepository tradeTransactionRepository, UserWalletRepository userWalletRepository) {
        this.cryptoPriceRespository = cryptoPriceRespository;
        this.tradeTransactionRepository = tradeTransactionRepository;
        this.userWalletRepository = userWalletRepository;
    }

    @Transactional
    public TradeTransactionModel executeTrade(String userId, String symbol, String action, Double quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity cannot be 0 or negative");
        }
        if (!action.equalsIgnoreCase("BUY") && !action.equalsIgnoreCase("SELL")) {
            throw new IllegalArgumentException("Action must be BUY or SELL");
        }

        Optional<CryptoPriceModel> priceOpt;
        if (action.equalsIgnoreCase("BUY")) {
            priceOpt = cryptoPriceRespository.findLatestBestAskPriceBySymbol(symbol);
        } else {
            priceOpt = cryptoPriceRespository.findLatestBestBidPriceBySymbol(symbol);
        }
        if (priceOpt.isEmpty()) {
            throw new RuntimeException("Could not retrieve price data for: " + symbol);
        }
        CryptoPriceModel priceData = priceOpt.get();
        Double price = action.equalsIgnoreCase("BUY") ? priceData.getAskPrice() : priceData.getBidPrice();

        Double totalCost = price * quantity;

        String cryptoCurrency = symbol.replace("USDT", "");

        UserWalletModel usdtWallet = userWalletRepository.findByUserIdAndCurrency(userId, "USDT").orElseThrow(() -> new RuntimeException("USDT wallet not found for user: " + userId));

        UserWalletModel cryptoWallet = userWalletRepository.findByUserIdAndCurrency(userId, cryptoCurrency).orElseThrow(() -> new RuntimeException(cryptoCurrency + " wallet not found for user: " + userId));

        if (action.equalsIgnoreCase("BUY")) {
            // Check if user has enough USDT to buy
            if (usdtWallet.getBalance() < totalCost) {
                throw new RuntimeException("Insufficient USDT balance to buy " + quantity + " " + cryptoCurrency);
            }
            // Deduct USDT, add crypto
            usdtWallet.setBalance(usdtWallet.getBalance() - totalCost);
            cryptoWallet.setBalance(cryptoWallet.getBalance() + quantity);
        } else {
            // Check if user has enough crypto to sell
            if (cryptoWallet.getBalance() < quantity) {
                throw new RuntimeException("Insufficient "+ cryptoCurrency +" balance to sell " + quantity);
            }
            // Deduct crypto, add USDT
            cryptoWallet.setBalance(cryptoWallet.getBalance() - quantity);
            usdtWallet.setBalance(usdtWallet.getBalance() + totalCost);
        }
        // Save updated wallet balances
        userWalletRepository.save(usdtWallet);
        userWalletRepository.save(cryptoWallet);

        // Log the transaction
        TradeTransactionModel transaction = new TradeTransactionModel(
                userId, symbol, action, quantity, price, new Timestamp(System.currentTimeMillis()));
        tradeTransactionRepository.save(transaction);

        return transaction;
    }

}
