package com.db.trade.schedule;

import com.db.trade.service.TradeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class TradeExpirationCronJob {

    private final TradeService tradeService;

    @Scheduled(fixedDelay = 10)
    public void updateExpiryFlagOfTrade() {
        log.info("Cron Job: ");
        tradeService.updateExpiryFlagOfTrade();
    }

}
