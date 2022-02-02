package com.db.trade.controller;

import com.db.trade.models.CreateTradeRequestDTO;
import com.db.trade.models.Trade;
import com.db.trade.service.TradeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;

    @PostMapping("/trades")
    public ResponseEntity<String> createTrade(@RequestBody CreateTradeRequestDTO createTradeRequest) {

        String tradeIdentifier = tradeService.createTrade(createTradeRequest);

        return ResponseEntity.ok().body(tradeIdentifier + " saved successfully");

    }

    @GetMapping("/trades/{tradeId}")
    public ResponseEntity<List<Trade>> getTradeByTradeId(@PathVariable String tradeId){

        List<Trade> trades =  tradeService.getTradeByTradeId(tradeId);

        return ResponseEntity.ok(trades);
    }

}
