package vn.com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.test.entity.Transaction;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByUserId(Long userId);
}
