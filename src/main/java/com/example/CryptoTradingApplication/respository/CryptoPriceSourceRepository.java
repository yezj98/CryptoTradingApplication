package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoPriceSourceRepository extends JpaRepository<CryptoPrice, Long> {
}
