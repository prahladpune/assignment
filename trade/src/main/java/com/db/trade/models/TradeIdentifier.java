package com.db.trade.models;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Builder
@ToString
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class TradeIdentifier implements Serializable {

    private String tradeId;
    private int version;
}
