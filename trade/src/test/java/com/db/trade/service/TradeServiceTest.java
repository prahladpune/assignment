package com.db.trade.service;

import com.db.trade.exception.InvalidTradeException;
import com.db.trade.models.CreateTradeRequestDTO;
import com.db.trade.models.Trade;
import com.db.trade.models.TradeIdentifier;
import com.db.trade.repository.TradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
class TradeServiceTest {

    @Mock
    TradeRepository tradeRepository;

    TradeService tradeService;

    @BeforeEach
    void setUp() {
        tradeService = new TradeService(tradeRepository);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    public void testAddTradeInStore() {

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(getTradeObject(1));

        String returnedValue = tradeService.createTrade(getCreateTradeRequestForVersion(1));

        Assertions.assertTrue(returnedValue.contains("T1"));

    }

    @Test
    public void testStoreMultipleVersionsTrade() {

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(getTradeObject(1));

        String returnedValue = tradeService.createTrade(getCreateTradeRequestForVersion(1));

        Assertions.assertTrue(returnedValue.contains("T1"));
        Assertions.assertTrue(returnedValue.contains("1"));

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(getTradeObject(2));
        String returnedValue1 = tradeService.createTrade(getCreateTradeRequestForVersion(2));

        Assertions.assertTrue(returnedValue1.contains("T1"));
        Assertions.assertTrue(returnedValue1.contains("2"));
    }


    @Test
    public void testValidateMaturityDate() {

        CreateTradeRequestDTO createTradeRequestDTO = new CreateTradeRequestDTO("T1", 1, "CP-1", "B1", LocalDate.parse("2022-02-01"), LocalDate.now(), 'N');
        InvalidTradeException invalidTradeException = Assertions.assertThrows(InvalidTradeException.class, () -> tradeService.createTrade(createTradeRequestDTO), "");

        Assertions.assertEquals("Maturity date should not be less than today's date.", invalidTradeException.getMessage());
    }

    @Test
    public void testValidateLowerVersionTradeNotAllowed() {

        Mockito.when(tradeRepository.save(any(Trade.class))).thenReturn(getTradeObject(3));
        String returnedValue = tradeService.createTrade(getCreateTradeRequestForVersion(3));
        Assertions.assertTrue(returnedValue.contains("3"));

        InvalidTradeException invalidTradeException = Assertions.assertThrows(InvalidTradeException.class, () -> tradeService.createTrade(getCreateTradeRequestForVersion(1)), "Lower version of trade is not allowed");

        Assertions.assertEquals("Lower version of trade is not allowed.", invalidTradeException.getMessage());
    }

    private Trade getTradeObject(int version) {

        return Trade.builder()
                .tradeIdentifier(
                        TradeIdentifier.builder()
                                .tradeId("T1")
                                .version(version)
                                .build()
                )
                .expired('N')
                .maturityDate(LocalDate.parse("2022-03-31"))
                .createdDate(LocalDate.now())
                .bookId("B1")
                .counterPartyId("CP-1")
                .build();
    }

    private CreateTradeRequestDTO getCreateTradeRequestForVersion(int version) {

        return new CreateTradeRequestDTO("T1", version, "CP-1", "B1", LocalDate.parse("2022-03-31"), LocalDate.now(), 'N');
    }

}