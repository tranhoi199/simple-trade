package vn.com.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.test.dto.TradeRequest;
import vn.com.test.entity.Transaction;
import vn.com.test.entity.Wallet;
import vn.com.test.service.TradeService;

import java.util.List;

@RestController
@AllArgsConstructor
public class TradeController {
    private final TradeService tradeService;

    @PostMapping("/api/trade/execute")
    public ResponseEntity<Transaction> executeTrade(@RequestBody TradeRequest tradeRequest) {
        try {
            var transaction = tradeService.executeTrade(tradeRequest);
            return ResponseEntity.ok(transaction);
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/api/trade/wallet/{userId}")
    public ResponseEntity<Wallet> getWalletBalance(@PathVariable String userId) {
        try {
            var userIdLong = Long.valueOf(userId);
            var wallet = tradeService.getWalletBalance(userIdLong);
            if (wallet == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(wallet);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/transaction/{userId}")
    public ResponseEntity<List<Transaction>> getTransactionList(@PathVariable Long userId) {
        var transactions = tradeService.getTransactionList(userId);
        return ResponseEntity.ok(transactions);
    }
}
