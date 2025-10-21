package com.cafepos.checkout;

import com.cafepos.catalog.Priced;
import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.TaxPolicy;

public final class OrderManager {
    private final ProductFactory factory;
    private final DiscountPolicy discountPolicy;
    private final TaxPolicy taxPolicy;
    private final PaymentStrategy paymentStrategy;
    private final ReceiptPrinter receiptPrinter;
    
    public OrderManager(ProductFactory factory, 
                       DiscountPolicy discountPolicy, 
                       TaxPolicy taxPolicy, 
                       PaymentStrategy paymentStrategy, 
                       ReceiptPrinter receiptPrinter) {
        this.factory = factory;
        this.discountPolicy = discountPolicy;
        this.taxPolicy = taxPolicy;
        this.paymentStrategy = paymentStrategy;
        this.receiptPrinter = receiptPrinter;
    }
    
    public String process(String recipe, int qty, boolean printReceipt) {
        Product product = factory.create(recipe);
        
        Money unitPrice = getUnitPrice(product);
        
        // Clamp quantity to minimum 1
        if (qty <= 0) qty = 1;
        
        Money subtotal = unitPrice.multiply(qty);
        Money discount = discountPolicy.discountOf(subtotal);
        Money discounted = subtotal.add(discount.multiply(-1));
        
        // Ensure discounted amount is not negative
        if (discounted.asBigDecimal().signum() < 0) {
            discounted = Money.zero();
        }
        
        Money tax = taxPolicy.taxOf(discounted);
        Money total = discounted.add(tax);
        
        // Process payment - create a simple order for payment strategy
        com.cafepos.domain.Order order = new com.cafepos.domain.Order(1);
        order.addItem(new com.cafepos.domain.LineItem(factory.create(recipe), qty));
        paymentStrategy.pay(order);
        
        // Generate receipt
        return receiptPrinter.printReceipt(recipe, qty, subtotal, discount, tax, total);
    }
    
    private Money getUnitPrice(Product product) {
        try {
            if (product instanceof Priced priced) {
                return priced.price();
            }
            return product.basePrice();
        } catch (Exception e) {
            return product.basePrice();
        }
    }
}
