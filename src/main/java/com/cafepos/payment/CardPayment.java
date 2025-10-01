package com.cafepos.payment;

import com.cafepos.domain.Order;
import java.text.DecimalFormat;

public final class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    public void pay(Order order) {
        // Format amount with two decimals
        DecimalFormat df = new DecimalFormat("#0.00");
        String amount = order.totalWithTax(23).toString();

        // Mask card number
        String maskedCard = maskCardNumber(cardNumber);

        System.out.println("[Card] Customer paid " + amount + " EUR with card " + maskedCard);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() <= 4) return cardNumber;
        String lastFour = cardNumber.substring(cardNumber.length() - 4);
        return "****" + lastFour;
    }
}
