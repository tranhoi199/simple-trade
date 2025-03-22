package vn.com.test.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TradeRequest {
    private Long userId;
    private String symbol;
    private String type;
    private BigDecimal quantity;
}
