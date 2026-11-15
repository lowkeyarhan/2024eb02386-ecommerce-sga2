package com.lowkeyarhan.ecommerce.common.validation;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.lowkeyarhan.ecommerce.common.error.BusinessRuleException;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BusinessRulesTest {
    private BusinessRules rules;

    @BeforeEach
    void setUp() {
        rules = new BusinessRules();
    }

    @Test
    void rejectsNonPositiveProductPrice() {
        assertThatThrownBy(() -> rules.requirePositivePrice(new BigDecimal("0.00")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("greater than zero");
    }

    @Test
    void rejectsQuantityAboveAvailableStock() {
        assertThatThrownBy(() -> rules.requireAvailableStock(4, 3))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void allowsRefundOnlyWithinOriginalCharge() {
        assertThatCode(() -> rules.requireRefundWithinCharge(
                new BigDecimal("25.00"), new BigDecimal("75.00"), new BigDecimal("100.00")))
                .doesNotThrowAnyException();
        assertThatThrownBy(() -> rules.requireRefundWithinCharge(
                new BigDecimal("25.01"), new BigDecimal("75.00"), new BigDecimal("100.00")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("exceed the original");
    }

    @Test
    void rejectsZeroOrNegativeQuantity() {
        assertThatThrownBy(() -> rules.requirePositiveQuantity(0))
                .isInstanceOf(BusinessRuleException.class);
    }
}
