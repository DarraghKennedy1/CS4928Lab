package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Money implements Comparable<Money> {
    private final BigDecimal amount;

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    // Added for Week 6 smelly code compatibility
    public static Money of(BigDecimal value) {
        if (value == null) throw new IllegalArgumentException("amount required");
        return new Money(value);
    }

    private Money(BigDecimal a) {
        if (a == null) throw new IllegalArgumentException("amount required");
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    public Money add(Money other) {
        if (other == null) throw new IllegalArgumentException("other money required");
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(int qty) {
        if (qty < 0) throw new IllegalArgumentException("quantity must not be negative");
        return new Money(this.amount.multiply(BigDecimal.valueOf(qty)));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return amount.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%.2f", amount);
    }

    // Added for Week 6 smelly code compatibility
    public BigDecimal asBigDecimal() {
        return amount;
    }

    @Override
    public int compareTo(Money other) {
        if (other == null) throw new IllegalArgumentException("other money required");
        return this.amount.compareTo(other.amount);
    }

    // Public method for tax calculations
    public Money calculateTax(int percent) {
        if (percent < 0) throw new IllegalArgumentException("tax percent must not be negative");
        java.math.BigDecimal taxAmount = this.amount.multiply(java.math.BigDecimal.valueOf(percent))
                .divide(java.math.BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Money(taxAmount);
    }
}
