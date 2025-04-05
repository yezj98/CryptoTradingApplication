package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.UserWalletModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWalletModel, Long> {
    List<UserWalletModel> findByUserId(String userId);

    Optional<UserWalletModel> findByUserIdAndCurrency(String userId, String currency);
}
