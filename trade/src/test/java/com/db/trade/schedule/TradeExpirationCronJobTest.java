package com.db.trade.schedule;

import com.db.trade.schedule.config.ScheduleTradeExpirationCronJobConfig;
import org.awaitility.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;


@SpringJUnitConfig(ScheduleTradeExpirationCronJobConfig.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
class TradeExpirationCronJobTest {

    @SpyBean
    private TradeExpirationCronJob tradeExpirationCronJob;

    @Test
    public void whenWaitOneSecondThenScheduledIsCalledAtLeastTwoTimes() {
        await()
                .atMost(Duration.ONE_SECOND)
                .untilAsserted(() -> verify(tradeExpirationCronJob, atLeast(50)).updateExpiryFlagOfTrade());
    }

}