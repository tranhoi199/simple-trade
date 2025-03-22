package vn.com.test.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "price_data")
@AllArgsConstructor
@NoArgsConstructor
public class PriceData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private Double bidPrice;

    @Getter
    @Setter
    private Double askPrice;
    private LocalDateTime timestamp;

    public PriceData(String symbol, double bidPrice, double askPrice, LocalDateTime now) {
        this.symbol = symbol;   
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
        this.timestamp = now;
    }

    public static PriceData fromBinanceTicker(BinanceTicker binanceTicker) {
        return PriceData.builder()
            .symbol(binanceTicker.getSymbol())
            .bidPrice(binanceTicker.getBidPrice())
            .askPrice(binanceTicker.getAskPrice())
            .build();
    }
}