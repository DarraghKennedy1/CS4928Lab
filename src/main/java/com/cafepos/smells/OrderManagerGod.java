package com.cafepos.smells;

import com.cafepos.catalog.Priced;
import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory; // fix reference, our Priced is in catalog

public class OrderManagerGod {
    public static int TAX_PERCENT = 10; // Global/Static State smell
    public static String LAST_DISCOUNT_CODE = null; // Global/Static State smell

    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        ProductFactory factory = new ProductFactory(); // God Class & Long Method (creation inside)
        Product product = factory.create(recipe);

        Money unitPrice;
        try {
            var priced = product instanceof Priced p ? p.price() : product.basePrice(); // Primitive Obsession
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice(); // Duplicated Logic fallback
        }

        if (qty <= 0) qty = 1; // Primitive Obsession (raw int with business rule)

        Money subtotal = unitPrice.multiply(qty);

        Money discount = Money.zero();
        if (discountCode != null) { // Primitive Obsession (string codes)
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                discount = Money.of(subtotal.asBigDecimal()
                        .multiply(java.math.BigDecimal.valueOf(5))
                        .divide(java.math.BigDecimal.valueOf(100))); // Duplicated Logic
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                discount = Money.of(1.00); // Magic number
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode; // Global/Static State
        }

        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal())); // Duplicated Logic
        if (discounted.asBigDecimal().signum() < 0) discounted = Money.zero();

        var tax = Money.of(discounted.asBigDecimal()
                .multiply(java.math.BigDecimal.valueOf(TAX_PERCENT))
                .divide(java.math.BigDecimal.valueOf(100))); // Shotgun Surgery risk

        var total = discounted.add(tax);

        if (paymentType != null) { // Primitive Obsession, Replace Conditional with Polymorphism 
            if (paymentType.equalsIgnoreCase("CASH")) {
                System.out.println("[Cash] Customer paid " + total + " EUR"); // I/O in domain logic
            } else if (paymentType.equalsIgnoreCase("CARD")) {
                System.out.println("[Card] Customer paid " + total + " EUR with card ****1234");
            } else if (paymentType.equalsIgnoreCase("WALLET")) {
                System.out.println("[Wallet] Customer paid " + total + " EUR via wallet user-wallet-789");
            } else {
                System.out.println("[UnknownPayment] " + total);
            }
        }

        StringBuilder receipt = new StringBuilder(); // Receipt formatting mixed 
        receipt.append("Order (").append(recipe).append(") x").append(qty).append("\n");
        receipt.append("Subtotal: ").append(subtotal).append("\n");
        if (discount.asBigDecimal().signum() > 0) {
            receipt.append("Discount: -").append(discount).append("\n");
        }
        receipt.append("Tax (").append(TAX_PERCENT).append("%): ").append(tax).append("\n");
        receipt.append("Total: ").append(total);

        String out = receipt.toString();
        if (printReceipt) {
            System.out.println(out); // I/O mixed in
        }
        return out;
    }
}


