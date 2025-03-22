package vn.com.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.client.RestTemplate;
import vn.com.test.repository.PriceDataRepository;
import vn.com.test.service.PriceAggregationService;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@TestPropertySource(properties = "spring.scheduler.enabled=false")
public class PriceAggregationServiceTest {
    @Mock
    private PriceDataRepository priceDataRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private PriceAggregationService priceAggregationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static String URL = "https://api.binance.com/api/v3/ticker/bookTicker";

//    @Test
//    public void testFetchAndStoreSuccessWithNoRowInDatabase() throws JsonProcessingException {
//        var listTickerPrice = new ArrayList<BinanceTicker>();
//        var symbol = "BTCUSDT";
//        var binanceTicker = new BinanceTicker(symbol, 10000.0, 10001.0);
//        listTickerPrice.add(binanceTicker);
//
////        when(priceDataRepository.findBySymbol(symbol)).thenReturn(Optional.empty());
//        var arrayString = objectMapper.writeValueAsString(listTickerPrice);
//        when(restTemplate.getForObject(URL, String.class)).thenReturn(arrayString);
//        when(priceDataRepository.save(any(PriceData.class))).thenReturn(PriceData.fromBinanceTicker(binanceTicker));
//
//        priceAggregationService.fetchAndStorePrice();
//        var priceData = priceDataRepository.findBySymbol(symbol);
//        Assertions.assertEquals(binanceTicker.getBidPrice(), priceData.get().getBidPrice());
//    }
    
}
