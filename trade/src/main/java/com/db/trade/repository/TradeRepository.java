package com.db.trade.repository;

import com.db.trade.models.Trade;
import com.db.trade.models.TradeIdentifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeRepository extends JpaRepository<Trade, TradeIdentifier> {

    @Query("SELECT MAX(t.tradeIdentifier.version) from Trade t where t.tradeIdentifier.tradeId= :tradeId")
    Integer findMaxVersionByTradeId(@Param("tradeId") String tradeId);

    List<Trade> findByTradeIdentifierTradeId(String tradeId);
}
