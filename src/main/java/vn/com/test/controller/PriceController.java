package vn.com.test.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.com.test.entity.PriceData;
import vn.com.test.service.PriceAggregationService;

@RestController
public class PriceController {
    @Autowired
    private PriceAggregationService priceAggregationService;

    @GetMapping("/api/price")
    public ResponseEntity<PriceData> getPrice(@RequestParam(value = "symbol", defaultValue = "") String symbol) {
        var priceData = priceAggregationService.getLatestPrice(symbol);
        
        if (priceData.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(priceData.get());
    }
}
