package vn.com.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;
import vn.com.test.entity.Wallet;
import vn.com.test.repository.WalletRepository;

import java.math.BigDecimal;

@SpringBootApplication
@EnableScheduling
public class Application {

	private static Object lock = new Object();

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	@Transactional
	CommandLineRunner initWallet(WalletRepository walletRepository) {
		return args -> {
			synchronized (lock) {
				try {
					var userId = 10l;
					if (!walletRepository.findByUserId(userId).isEmpty()) {
						System.out.println("already init wallet for this user");
						return;
					}
					var wallet = new Wallet();
					wallet.setUserId(userId);
					wallet.setUsdtBalance(new BigDecimal(50000));
					wallet.setEthBalance(BigDecimal.ZERO);
					wallet.setBtcBalance(BigDecimal.ZERO);
					walletRepository.saveAndFlush(wallet);
					System.out.println("completed initialize");
				} catch (Exception ex) {
					System.out.println(String.format("error when initialize wallet", ex));
				}
			}
		};
	}

}
