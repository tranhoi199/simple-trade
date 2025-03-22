package vn.com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.test.entity.Wallet;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUserId(Long userId);
}
