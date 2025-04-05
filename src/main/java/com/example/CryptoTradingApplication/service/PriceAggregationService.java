package com.example.CryptoTradingApplication.service;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import com.example.CryptoTradingApplication.respository.CryptoPriceRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
public class PriceAggregationService {

    @Autowired
    private final CryptoPriceRespository cryptoPriceSourceRepository;

    @Autowired
    private final RestTemplate restTemplate;

    public PriceAggregationService(CryptoPriceRespository cryptoPriceSourceRepository, RestTemplate restTemplate) {
        this.cryptoPriceSourceRepository = cryptoPriceSourceRepository;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void storeCryptoPriceFromAPI() {
        String huobiUrl = "https://api.huobi.pro/market/tickers";
        String binanceUrl = "https://api.binance.com/api/v3/ticker/bookTicker";

        Timestamp batchTimestamp = new Timestamp(System.currentTimeMillis()); //1 sec difference between 2 api call,align with timestamp

        Map<String, Double> huobiBids = new HashMap<>();
        Map<String, Double> huobiAsks = new HashMap<>();
        Map<String, Double> binanceBids = new HashMap<>();
        Map<String, Double> binanceAsks = new HashMap<>();

        CompletableFuture<Void> huobiFuture = CompletableFuture.runAsync(() -> fetchHuobiPrices(huobiUrl, huobiBids, huobiAsks));
        CompletableFuture<Void> binanceFuture = CompletableFuture.runAsync(() -> fetchBinancePrices(binanceUrl, binanceBids, binanceAsks));

        CompletableFuture.allOf(huobiFuture, binanceFuture).join();

        aggregateAndStoreBestPrices(huobiBids, huobiAsks, binanceBids, binanceAsks, batchTimestamp);
    }

    private void fetchHuobiPrices(String huobiUrl, Map<String, Double> bidPrices, Map<String, Double> askPrices) {
        try {
            Map<String, Object> huobiResponse = restTemplate.getForObject(huobiUrl, Map.class);
            if (huobiResponse == null || !huobiResponse.containsKey("data")) {
                System.err.println("Invalid response from Huobi API");
                return;
            }

            List<Map<String, Object>> huobiTickers = (List<Map<String, Object>>) huobiResponse.get("data");
            for (Map<String, Object> ticker : huobiTickers) {
                String symbol = (String) ticker.get("symbol");
                if (symbol == null || (!symbol.equalsIgnoreCase("ethusdt") && !symbol.equalsIgnoreCase("btcusdt"))) {
                    continue;
                }

                Object bidPriceObj = ticker.get("bid");
                Object askPriceObj = ticker.get("ask");
                if (bidPriceObj == null || askPriceObj == null) {
                    System.err.println("Missing bid or ask price for symbol " + symbol + " from Huobi");
                    continue;
                }

                Double bidPrice = Double.valueOf(bidPriceObj.toString());
                Double askPrice = Double.valueOf(askPriceObj.toString());

                bidPrices.put(symbol.toUpperCase(), bidPrice);
                askPrices.put(symbol.toUpperCase(), askPrice);
                System.out.println("Fetched Huobi price for " + symbol + ": Bid=" + bidPrice + ", Ask=" + askPrice);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch prices from Huobi: " + e.getMessage());
        }
    }

    private void fetchBinancePrices(String binanceUrl, Map<String, Double> bidPrices, Map<String, Double> askPrices) {
        try {
            List<Map<String, Object>> binanceTickers = restTemplate.getForObject(binanceUrl, List.class);
            if (binanceTickers == null) {
                System.err.println("Invalid response from Binance API");
                return;
            }

            for (Map<String, Object> ticker : binanceTickers) {
                String symbol = (String) ticker.get("symbol");
                if (symbol == null || (!symbol.equalsIgnoreCase("ethusdt") && !symbol.equalsIgnoreCase("btcusdt"))) {
                    continue;
                }

                Object bidPriceObj = ticker.get("bidPrice");
                Object askPriceObj = ticker.get("askPrice");
                if (bidPriceObj == null || askPriceObj == null) {
                    System.err.println("Missing bid or ask price for symbol " + symbol + " from Binance");
                    continue;
                }

                Double bidPrice = Double.valueOf(bidPriceObj.toString());
                Double askPrice = Double.valueOf(askPriceObj.toString());

                bidPrices.put(symbol.toUpperCase(), bidPrice);
                askPrices.put(symbol.toUpperCase(), askPrice);
                System.out.println("Fetched Binance price for " + symbol + ": Bid=" + bidPrice + ", Ask=" + askPrice);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch prices from Binance: " + e.getMessage());
        }
    }

    private void aggregateAndStoreBestPrices(Map<String, Double> huobiBids, Map<String, Double> huobiAsks,
                                             Map<String, Double> binanceBids, Map<String, Double> binanceAsks,
                                             Timestamp batchTimestamp) {
        // List of symbols to aggregate
        String[] symbols = {"ETHUSDT", "BTCUSDT"};

        for (String symbol : symbols) {
            Double huobiBid = huobiBids.get(symbol);
            Double huobiAsk = huobiAsks.get(symbol);
            Double binanceBid = binanceBids.get(symbol);
            Double binanceAsk = binanceAsks.get(symbol);

            if (huobiBid == null && binanceBid == null) {
                System.err.println("No price data available for symbol " + symbol);
                continue;
            }

            Double bestBid = Math.max(huobiBid != null ? huobiBid : Double.MIN_VALUE,
                    binanceBid != null ? binanceBid : Double.MIN_VALUE);
            Double bestAsk = Math.min(huobiAsk != null ? huobiAsk : Double.MAX_VALUE,
                    binanceAsk != null ? binanceAsk : Double.MAX_VALUE);

            CryptoPriceModel price = new CryptoPriceModel(
                    symbol,
                    bestBid,
                    bestAsk,
                    batchTimestamp
            );
            cryptoPriceSourceRepository.save(price);
            System.out.println("Saved best price for " + symbol + ": Bid=" + bestBid + ", Ask=" + bestAsk);
        }
    }

}
