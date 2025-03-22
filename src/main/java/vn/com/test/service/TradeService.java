package vn.com.test.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.com.test.dto.TradeRequest;
import vn.com.test.entity.Transaction;
import vn.com.test.entity.Wallet;
import vn.com.test.repository.TransactionRepository;
import vn.com.test.repository.WalletRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class TradeService {

    @Autowired
    private PriceAggregationService priceAggregationService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private static List<String> SYMBOLS_SUPPORT = Arrays.asList("ETHUSDT", "BTCUSDT");
    private static String BUY_TYPE = "BUY";
    private static String SELL_TYPE = "SELL";

    @Transactional
    public Transaction executeTrade(TradeRequest request) throws Exception {
        var symbol = request.getSymbol();

        if (!SYMBOLS_SUPPORT.contains(symbol)) {
            return new  Transaction();
        }

        var lastestPriceOptional = priceAggregationService.getLatestPrice(symbol);

        if (lastestPriceOptional.isEmpty()) {
            throw new Exception("Price not found");
        }

        var latestPrice = lastestPriceOptional.get();
        var walletOptional = walletRepository.findByUserId(request.getUserId());

        if (walletOptional.isEmpty()) {
            throw new Exception("Wallet not found");
        }

        var wallet = walletOptional.get();
        
        var typeTrade = request.getType();
        if (!Objects.equals(typeTrade, BUY_TYPE) || !Objects.equals(typeTrade, SELL_TYPE)) {
            throw new Exception("Type is not support");
        }

        var priceToTrade = typeTrade.equals("BUY") ? latestPrice.getAskPrice() : latestPrice.getBidPrice();
        var quantity = request.getQuantity();
        var totalUsdtTrade = request.getQuantity().multiply(BigDecimal.valueOf(priceToTrade));

        if (typeTrade.equals(BUY_TYPE)) {
            var usdtBalance = wallet.getUsdtBalance();
            if (quantity.compareTo(BigDecimal.valueOf(0)) < 0)  {
                throw new Exception("Invalid operation");
            }

            if ( usdtBalance.compareTo(totalUsdtTrade) < 0) {
                throw new Exception("Insufficient balance");
            }
            wallet.setUsdtBalance(usdtBalance.subtract(totalUsdtTrade));
            increaseBalance(wallet, request.getQuantity(), symbol);

            var transaction = Transaction.builder()
                .userId(request.getUserId())
                .symbol(symbol)
                .type(typeTrade)
                .quantity(request.getQuantity())
                .price(BigDecimal.valueOf(priceToTrade))
                .total(totalUsdtTrade)
                .createdAt(LocalDateTime.now())
                .build();
            walletRepository.save(wallet);
            return transactionRepository.save(transaction);
        }

        BigDecimal cryptoBalance = symbol.equals("BTCUSDT") ? wallet.getBtcBalance() : wallet.getEthBalance();

        if (quantity.compareTo(BigDecimal.valueOf(0)) < 0) {
            throw new Exception("Invalid operation");
        }

        if (cryptoBalance == null || cryptoBalance.compareTo(request.getQuantity()) < 0) {
            throw new Exception("Insufficient balance");
        }

        wallet.setUsdtBalance(wallet.getUsdtBalance().add(totalUsdtTrade));
        subtractBalance(wallet, request.getQuantity(), symbol);

        walletRepository.save(wallet);
        var transaction = Transaction.builder()
            .userId(request.getUserId())
            .symbol(symbol)
            .type(typeTrade)
            .quantity(request.getQuantity())
            .price(BigDecimal.valueOf(priceToTrade))
            .total(totalUsdtTrade)
            .createdAt(LocalDateTime.now())
            .build();
        return transactionRepository.save(transaction);
    }

    private Wallet increaseBalance(Wallet wallet, BigDecimal amount, String symbol) {
        if (symbol.equals("BTCUSDT")) {
            var btcBalance = wallet.getBtcBalance();
            if (btcBalance == null || btcBalance.equals(0)) {
                wallet.setBtcBalance(amount);
                return wallet;
            }
            wallet.setBtcBalance(btcBalance.add(amount));
            return wallet;
        }

        if (symbol.equals("ETHUSDT")) {
            var ethBalance = wallet.getEthBalance();
            if (ethBalance == null || ethBalance.equals(0)) {
                wallet.setEthBalance(amount);
                return wallet;
            }
            wallet.setEthBalance(ethBalance.add(amount));
            return wallet;
        }

        return wallet;
    }

    private Wallet subtractBalance(Wallet wallet, BigDecimal amount, String symbol) {
        return increaseBalance(wallet, amount.negate(), symbol);
    }
}
