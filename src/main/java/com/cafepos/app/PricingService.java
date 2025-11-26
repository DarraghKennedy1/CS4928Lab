package com.cafepos.app;

import com.cafepos.common.Money;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.TaxPolicy;

/**
 * Application service for pricing calculations.
 * Orchestrates discount and tax policies.
 */
public final class PricingService {
    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;

    public PricingService(DiscountPolicy discountPolicy, TaxPolicy taxPolicy) {
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
    }

    /**
     * Result of pricing calculation containing subtotal, discount, tax, and total.
     */
    public record PricingResult(Money subtotal, Money discount, Money tax, Money total) {}

    /**
     * Calculate pricing with discount and tax applied.
     */
    public PricingResult price(Money subtotal) {
        Money discount = discountPolicy.discountOf(subtotal);
        Money afterDiscount = subtotal.subtract(discount);
        Money tax = taxPolicy.taxOf(afterDiscount);
        Money total = afterDiscount.add(tax);
        return new PricingResult(subtotal, discount, tax, total);
    }
}
