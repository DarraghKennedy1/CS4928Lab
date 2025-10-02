package com.cafepos.domain;

import com.cafepos.common.Money;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.observer.OrderObserver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();
    // Observer subscriptions
    private final List<OrderObserver> observers = new ArrayList<>();

    public Order(long id) {
        this.id = id;
    }

    public void addItem(LineItem li) {
        if (li == null) throw new IllegalArgumentException("line item required");
        if (li.quantity() <= 0) throw new IllegalArgumentException("quantity must be > 0");
        items.add(li);
        notifyObservers("itemAdded");
    }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public void pay(PaymentStrategy strategy) {
        if (strategy == null) throw new
                IllegalArgumentException("strategy required");
        strategy.pay(this);
        notifyObservers("paid");
    }

    public Money taxAtPercent(int percent) {
        return subtotal().calculateTax(percent);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }

    public long id() {
        return id;
    }

    public List<LineItem> items() {
        return Collections.unmodifiableList(items);
    }

    // Observer management
    public void register(OrderObserver o) {
        if (o == null) return;
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    public void unregister(OrderObserver o) {
        if (o == null) return;
        observers.remove(o);
    }

    private void notifyObservers(String eventType) {
        for (OrderObserver observer : observers) {
            observer.updated(this, eventType);
        }
    }

    public void markReady() {
        notifyObservers("ready");
    }
}
