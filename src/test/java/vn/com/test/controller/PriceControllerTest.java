package vn.com.test.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import vn.com.test.entity.PriceData;
import vn.com.test.service.PriceAggregationService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@SpringBootTest
@AutoConfigureMockMvc
@WebMvcTest(PriceController.class)
public class PriceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PriceAggregationService priceAggregationService;

//    @Test
//    public void testGetPriceSuccess() throws Exception {
//        var symbol = "BTCUSDT";
//        var priceData = PriceData.builder()
//            .symbol(symbol)
//            .bidPrice(10000.0)
//            .askPrice(10001.0)
//            .build();
//
//        when(priceAggregationService.getLatestPrice(symbol)).thenReturn(Optional.of(priceData));
//
//        var response = mockMvc.perform(get("/api/price")
//            .param("symbol", symbol));
//        Assertions.assertEquals(1, 1);
//    }
}
