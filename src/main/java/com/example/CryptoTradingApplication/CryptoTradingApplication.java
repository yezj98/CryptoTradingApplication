package com.example.CryptoTradingApplication;

import com.example.CryptoTradingApplication.model.CryptoPriceModel;
import com.example.CryptoTradingApplication.respository.CryptoPriceSourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class CryptoTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTradingApplication.class, args);
	}
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}


}
