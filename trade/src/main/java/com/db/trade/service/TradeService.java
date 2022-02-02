package com.db.trade.service;

import com.db.trade.exception.InvalidTradeException;
import com.db.trade.models.CreateTradeRequestDTO;
import com.db.trade.models.Trade;
import com.db.trade.models.TradeIdentifier;
import com.db.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TradeService {

    private final TradeRepository tradeRepository;

    private final Map<String, Integer> tradeMaxVersionMap = new HashMap<>();

    public String createTrade(final CreateTradeRequestDTO createTradeRequestDTO) {

        //validate version
        validateTrade(createTradeRequestDTO);
        Trade trade = getTrade(createTradeRequestDTO);
        Trade savedTrade = tradeRepository.save(trade);
        tradeMaxVersionMap.put(savedTrade.getTradeIdentifier().getTradeId(), savedTrade.getTradeIdentifier().getVersion());
        return savedTrade.getTradeIdentifier().toString();
    }

    private Trade getTrade(CreateTradeRequestDTO createTradeRequestDTO) {
        return Trade.builder()
                .tradeIdentifier(TradeIdentifier.builder()
                        .tradeId(createTradeRequestDTO.getTradeId())
                        .version(createTradeRequestDTO.getVersion())
                        .build())
                .bookId(createTradeRequestDTO.getBookId())
                .counterPartyId(createTradeRequestDTO.getCounterPartyId())
                .createdDate(createTradeRequestDTO.getCreatedDate())
                .maturityDate(createTradeRequestDTO.getMaturityDate())
                .expired(createTradeRequestDTO.getExpired())
                .build();
    }

    private void validateTrade(final CreateTradeRequestDTO createTradeRequestDTO) {

        Integer maxVersion = tradeMaxVersionMap.get(createTradeRequestDTO.getTradeId());
        if (maxVersion == null) {
            maxVersion = getMaxVersion(createTradeRequestDTO.getTradeId());

        }

        if (maxVersion!=null && maxVersion > createTradeRequestDTO.getVersion()) {
            throw new InvalidTradeException("Lower version of trade is not allowed.");
        }

        if (LocalDate.now().isAfter(createTradeRequestDTO.getMaturityDate())) {
            throw new InvalidTradeException("Maturity date should not be less than today's date.");
        }
    }

    private Integer getMaxVersion(String tradeId) {
        return tradeRepository.findMaxVersionByTradeId(tradeId);
    }

    public List<Trade> getTradeByTradeId(String tradeId) {

        return tradeRepository.findByTradeIdentifierTradeId(tradeId);
    }

    public void updateExpiryFlagOfTrade() {
        tradeRepository.findAll().forEach(t -> {
            if (isTradeExpired(t)) {
                t.setExpired('Y');
                log.info("Trade {} is expired", t);
                tradeRepository.save(t);
            }
        });
    }

    private boolean isTradeExpired(Trade trade) {
        return trade.getMaturityDate().isBefore(LocalDate.now());
    }
}
