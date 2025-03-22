package vn.com.test.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BinanceTicker {
    @Getter
    private String symbol;
    @Getter
    private double bidPrice;
    @Getter
    private double askPrice;
    
}
