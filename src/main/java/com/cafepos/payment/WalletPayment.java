package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class WalletPayment implements PaymentStrategy {
    private final String walletId;

    public WalletPayment(String walletId) {
        this.walletId = walletId;
    }

    @Override
    public void pay(Order order) {
        String amount = order.totalWithTax(10).toString(); // Example 10% tax
        System.out.println("[Wallet] Customer paid " + amount + " EUR using wallet " + walletId);
    }
}

