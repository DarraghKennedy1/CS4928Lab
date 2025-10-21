package com.cafepos.pricing;

import com.cafepos.common.Money;

public final class FixedAmountDiscount implements DiscountPolicy {
    private final Money amount;
    
    public FixedAmountDiscount(Money amount) {
        if (amount == null) throw new IllegalArgumentException("amount required");
        this.amount = amount;
    }
    
    @Override 
    public Money discountOf(Money subtotal) {
        return amount;
    }
}
