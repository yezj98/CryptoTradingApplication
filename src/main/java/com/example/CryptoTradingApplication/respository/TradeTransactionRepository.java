package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.TradeTransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeTransactionRepository  extends JpaRepository<TradeTransactionModel, Long> {
    List<TradeTransactionModel> findByUserIdOrderByTimestampDesc(String userId);
}
