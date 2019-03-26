package com.mk.workshop.testing.confo.infrastructure;

import com.mk.workshop.testing.confo.domain.OrderId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class PaypalPayments {
    private final String url;

    public String getApprovalLink(OrderId orderId, BigDecimal totalCost) {
        return String.format("%s?totalCost=%s&paymentId=%s", url, totalCost.toPlainString(), orderId.getRaw());
    }
}
