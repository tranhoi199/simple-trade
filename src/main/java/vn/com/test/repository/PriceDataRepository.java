package vn.com.test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.com.test.entity.PriceData;

import java.util.Optional;

public interface PriceDataRepository extends JpaRepository<PriceData, Long> {
    Optional<PriceData> findBySymbol(String symbol);
}