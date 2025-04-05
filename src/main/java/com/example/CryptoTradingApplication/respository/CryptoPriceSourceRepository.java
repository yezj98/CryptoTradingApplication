package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoPriceSourceRepository extends JpaRepository<CryptoPriceModel, Long> {

    Optional<CryptoPriceModel> findTopBySymbolOrderByTimestampDesc (String symbol);
}
