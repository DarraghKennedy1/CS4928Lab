package com.cafepos.infra;

import com.cafepos.app.CheckoutService;
import com.cafepos.app.PricingService;
import com.cafepos.domain.OrderRepository;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.PercentTaxPolicy;

/**
 * Composition root - wires all dependencies together.
 * This is where we choose concrete implementations.
 */
public final class Wiring {
    
    public record Components(
        OrderRepository repo,
        PricingService pricing,
        CheckoutService checkout
    ) {}

    public static Components createDefault() {
        OrderRepository repo = new InMemoryOrderRepository();
        PricingService pricing = new PricingService(
            new LoyaltyPercentDiscount(5),
            new PercentTaxPolicy(10)
        );
        CheckoutService checkout = new CheckoutService(repo, pricing);
        return new Components(repo, pricing, checkout);
    }
}
