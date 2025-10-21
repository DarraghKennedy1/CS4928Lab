package com.cafepos.pricing;

import com.cafepos.common.Money;

public final class PercentTaxPolicy implements TaxPolicy {
    private final int percent;
    
    public PercentTaxPolicy(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent must not be negative");
        this.percent = percent;
    }
    
    @Override
    public Money taxOf(Money amount) {
        return amount.calculateTax(percent);
    }
}
