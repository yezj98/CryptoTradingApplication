package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.UserWalletModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWalletModel, Long> {
    List<UserWalletModel> findByUserId(String userId);
}
