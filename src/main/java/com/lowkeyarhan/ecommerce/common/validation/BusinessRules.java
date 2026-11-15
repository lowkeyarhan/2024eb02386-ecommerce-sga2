package com.lowkeyarhan.ecommerce.common.validation;

import com.lowkeyarhan.ecommerce.common.error.BusinessRuleException;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class BusinessRules {
    public void requirePositivePrice(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) throw new BusinessRuleException("Price must be greater than zero.");
    }
    public void requirePositiveQuantity(Integer quantity) {
        if (quantity == null || quantity <= 0) throw new BusinessRuleException("Quantity must be greater than zero.");
    }
    public void requireAvailableStock(Integer requested, Integer available) {
        requirePositiveQuantity(requested);
        if (available == null || requested > available) throw new BusinessRuleException("Insufficient stock. Available quantity: " + (available == null ? 0 : available) + ".");
    }
    public void requireRefundWithinCharge(BigDecimal refund, BigDecimal priorRefunds, BigDecimal charge) {
        if (refund == null || refund.signum() <= 0) throw new BusinessRuleException("Refund amount must be positive.");
        BigDecimal alreadyRefunded = priorRefunds == null ? BigDecimal.ZERO : priorRefunds;
        if (alreadyRefunded.add(refund).compareTo(charge) > 0) throw new BusinessRuleException("Refund would exceed the original completed charge.");
    }
}
