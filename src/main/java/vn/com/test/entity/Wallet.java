package vn.com.test.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "wallets")
@Data
public class Wallet {
    @Id
    private Long userId;

   private BigDecimal usdtBalance;
   private BigDecimal btcBalance;
   private BigDecimal ethBalance;
}
