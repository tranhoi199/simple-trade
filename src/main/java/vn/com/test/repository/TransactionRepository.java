package vn.com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.test.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
