package com.example.CryptoTradingApplication.service;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import com.example.CryptoTradingApplication.respository.CryptoPriceSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
public class PriceAggregationService {

    @Autowired
    private final CryptoPriceSourceRepository cryptoPriceSourceRepository;

    @Autowired
    private final RestTemplate restTemplate;

    public PriceAggregationService(CryptoPriceSourceRepository cryptoPriceSourceRepository, RestTemplate restTemplate) {
        this.cryptoPriceSourceRepository = cryptoPriceSourceRepository;
        this.restTemplate = restTemplate;
    }

//    @Scheduled(fixedRate = 10000)
    public void storeCryptoPriceFromAPI() {
        String huobiUrl = "https://api.huobi.pro/market/tickers";
        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";

        try {
            Map<String, Object> huobiResponse = restTemplate.getForObject(huobiUrl, Map.class);
//            Map<String, Object> binanceResponse = restTemplate.getForObject(binanceUrl, Map.class);

            if (huobiResponse == null || !huobiResponse.containsKey("data") ) {
                System.err.println("Invalid response from API");
                return;
            }

            List<Map<String, Object>> huobiTicker = (List<Map<String, Object>>) huobiResponse.get("data");
//            List<Map<String, Object>> binanceTicker = (List<Map<String, Object>>) binanceResponse.get("data");

            //Crypto Price From HUOBI
            for (Map<String, Object> ticker : huobiTicker) {
                String symbol = (String) ticker.get("symbol");
                if (symbol.equalsIgnoreCase("ethusdt") || symbol.equalsIgnoreCase("btcusdt")) {
                    Double bidPrice = Double.valueOf(ticker.get("bid").toString());
                    Double askPrice = Double.valueOf(ticker.get("ask").toString());
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                    CryptoPriceModel price = new CryptoPriceModel(
                            symbol.toUpperCase(),
                            bidPrice,
                            askPrice,
                            "HUOBI",
                            timestamp
                    );
                    cryptoPriceSourceRepository.save(price);
                    System.out.println("Save Huobi price successfully for " + symbol + ": Bid=" + bidPrice + ", Ask=" + askPrice);
                }
            }

            //Crypto Price From BINANCE
//            for (Map<String, Object> ticker : binanceTicker) {
//                String symbol = (String) ticker.get("symbol");
//                if (symbol.equalsIgnoreCase("ethusdt") || symbol.equalsIgnoreCase("btcusdt")) {
//                    Double bidPrice = Double.valueOf(ticker.get("bid").toString());
//                    Double askPrice = Double.valueOf(ticker.get("ask").toString());
//                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//
//                    CryptoPriceModel price = new CryptoPriceModel(
//                            symbol.toUpperCase(),
//                            bidPrice,
//                            askPrice,
//                            "BINANCE",
//                            timestamp
//                    );
//                    cryptoPriceSourceRepository.save(price);
//                    System.out.println("Save Binance price successfully for " + symbol + ": Bid=" + bidPrice + ", Ask=" + askPrice);
//                }
//            }
        }catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }


}
