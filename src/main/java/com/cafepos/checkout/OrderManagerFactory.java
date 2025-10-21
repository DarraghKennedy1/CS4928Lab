package com.cafepos.checkout;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.CashPayment;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.payment.WalletPayment;
import com.cafepos.pricing.DiscountPolicy;
import com.cafepos.pricing.FixedAmountDiscount;
import com.cafepos.pricing.LoyaltyPercentDiscount;
import com.cafepos.pricing.NoDiscount;
import com.cafepos.pricing.PercentTaxPolicy;
import com.cafepos.pricing.TaxPolicy;

public final class OrderManagerFactory {
    
    public static OrderManager createForCashPayment(String discountCode) {
        return createOrderManager("CASH", discountCode, "****1234", "user-wallet-789");
    }
    
    public static OrderManager createForCardPayment(String discountCode) {
        return createOrderManager("CARD", discountCode, "****1234", "user-wallet-789");
    }
    
    public static OrderManager createForWalletPayment(String discountCode) {
        return createOrderManager("WALLET", discountCode, "****1234", "user-wallet-789");
    }
    
    private static OrderManager createOrderManager(String paymentType, String discountCode, String cardNumber, String walletId) {
        ProductFactory factory = new ProductFactory();
        DiscountPolicy discountPolicy = createDiscountPolicy(discountCode);
        TaxPolicy taxPolicy = new PercentTaxPolicy(10);
        PaymentStrategy paymentStrategy = createPaymentStrategy(paymentType, cardNumber, walletId);
        ReceiptPrinter receiptPrinter = new ReceiptPrinter(false);
        
        return new OrderManager(factory, discountPolicy, taxPolicy, paymentStrategy, receiptPrinter);
    }
    
    private static DiscountPolicy createDiscountPolicy(String discountCode) {
        if (discountCode == null) {
            return new NoDiscount();
        }
        
        switch (discountCode.toUpperCase()) {
            case "LOYAL5":
                return new LoyaltyPercentDiscount(5);
            case "COUPON1":
                return new FixedAmountDiscount(Money.of(1.00));
            case "NONE":
            default:
                return new NoDiscount();
        }
    }
    
    private static PaymentStrategy createPaymentStrategy(String paymentType, String cardNumber, String walletId) {
        if (paymentType == null) {
            return new CashPayment();
        }
        
        switch (paymentType.toUpperCase()) {
            case "CASH":
                return new CashPayment();
            case "CARD":
                return new CardPayment(cardNumber);
            case "WALLET":
                return new WalletPayment(walletId);
            default:
                return new CashPayment();
        }
    }
}
