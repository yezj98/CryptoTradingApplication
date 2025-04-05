package com.example.CryptoTradingApplication.respository;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoPriceRespository extends JpaRepository<CryptoPriceModel, Long> {
    @Query("SELECT crypto FROM CryptoPriceModel crypto " +
            "WHERE crypto.symbol = :symbol " +
            "ORDER BY crypto.timestamp DESC, crypto.askPrice ASC LIMIT 1")
    Optional<CryptoPriceModel> findLatestBestAskPriceBySymbol(@Param("symbol") String symbol);

    @Query("SELECT crypto FROM CryptoPriceModel crypto " +
            "WHERE crypto.symbol = :symbol " +
            "ORDER BY crypto.timestamp DESC, crypto.bidPrice ASC LIMIT 1")
    Optional<CryptoPriceModel> findLatestBestBidPriceBySymbol(@Param("symbol") String symbol);

}
