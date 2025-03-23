package vn.com.test.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.com.test.entity.BinanceTicker;
import vn.com.test.entity.PriceData;
import vn.com.test.repository.PriceDataRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class PriceAggregationService {

    @Autowired
    private PriceDataRepository priceDataRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    private static List<String> SYMBOLS_SUPPORT = Arrays.asList("ETHUSDT", "BTCUSDT");

    private static String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static String HUIBI_URL = "https://api.huobi.pro/market/tickers";

    @Scheduled(fixedRate = 10000L)
    public void fetchAndStorePrice() {
        fetchBinancePrices(BINANCE_URL);
        fetchHuobiPrices();
    }

    private void fetchBinancePrices(String url) {
        try {
            var listTickerPrice = getListOfTicker(url);

            if (listTickerPrice == null) {
                return;
            }

            processTickerPrice(listTickerPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fetchHuobiPrices() {
        try {
            var listTickerPrice = getHuobiTickers(HUIBI_URL);

            if (listTickerPrice == null) {
                return;
            }

            processTickerPrice(listTickerPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processTickerPrice(List<BinanceTicker> listTickerPrice) {
        for (BinanceTicker ticker : listTickerPrice) {
            var symbol = ticker.getSymbol();
            if (!SYMBOLS_SUPPORT.contains(symbol)) {
                continue;
            }
            var bidPrice = ticker.getBidPrice();
            var askPrice = ticker.getAskPrice();
            compareAndUpdateLatestPrice(symbol, bidPrice, askPrice);
        }
    }

    public Optional<PriceData> getLatestPrice(String symbol) {
        return priceDataRepository.findBySymbol(symbol);
    }

    private List<BinanceTicker> getListOfTicker(String url) throws JsonProcessingException {
        var responseString = restTemplate.getForObject(url, String.class);
        var binanceTickerList = objectMapper.readValue(responseString, BinanceTicker[].class);
        return Arrays.asList(binanceTickerList);
    }

    private List<BinanceTicker> getHuobiTickers(String url) throws JsonProcessingException {
        var responseString = restTemplate.getForObject(url, JsonNode.class);
        var data = responseString.get("data");
        var tickers = data;
        var result = new ArrayList<BinanceTicker>();
        for (JsonNode jsonNode : tickers) {
            var symbol = jsonNode.get("symbol").asText().toUpperCase();
            var bidPrice = jsonNode.get("bid").asDouble();
            var askPrice = jsonNode.get("ask").asDouble();
            result.add(new BinanceTicker(symbol, bidPrice, askPrice));
        }
        return result;
    }

    private boolean compareAndUpdateLatestPrice(String symbol, double bidPrice, double askPrice) {
        var databaseBestPriceOpt = priceDataRepository.findBySymbol(symbol);

        if (databaseBestPriceOpt.isEmpty()) {
//            System.out.println(String.format("cannot find symbol, %s", symbol));
            var priceData = new PriceData(symbol, bidPrice, askPrice, LocalDateTime.now());
            priceDataRepository.save(priceData);
            return true;
        }

        var databaseBestPrice = databaseBestPriceOpt.get();

        if (bidPrice == databaseBestPrice.getBidPrice() && askPrice == databaseBestPrice.getAskPrice()) {
            return false;
        }

        if (bidPrice >= databaseBestPrice.getBidPrice() && askPrice <= databaseBestPrice.getAskPrice()) {
            return false;
        }

        if (bidPrice < databaseBestPrice.getBidPrice()) {
            databaseBestPrice.setBidPrice(bidPrice);
        }

        if (askPrice > databaseBestPrice.getAskPrice()) {
            databaseBestPrice.setAskPrice(askPrice);
        }

        priceDataRepository.save(databaseBestPrice);
        return true;
    }
}