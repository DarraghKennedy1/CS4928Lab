package com.cafepos.command;

import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.PaymentStrategy;

public final class OrderService {
    private final ProductFactory factory = new ProductFactory();
    private final Order order;

    public OrderService(Order order) { this.order = order; }

    public void addItem(String recipe, int quantity) {
        Product product = factory.create(recipe);
        order.addItem(new LineItem(product, quantity));
        System.out.println("[Service] Added " + product.name() + " x" + quantity);
    }

    public void removeLastItem() {
        var itemsCopy = new java.util.ArrayList<>(order.items());
        if (!itemsCopy.isEmpty()) {
            itemsCopy.remove(itemsCopy.size() - 1);
            try {
                var field = Order.class.getDeclaredField("items");
                field.setAccessible(true);
                field.set(order, itemsCopy);
            } catch (Exception e) {
                throw new IllegalStateException("Could not remove item reflectively; adapt your Order API.");
            }
            System.out.println("[Service] Removed last item");
        }
    }

    public Money totalWithTax(int percent) {
        return order.totalWithTax(percent);
    }

    public void pay(PaymentStrategy strategy, int taxPercent) {
        var total = order.totalWithTax(taxPercent);
        strategy.pay(order);
        System.out.println("[Service] Payment processed for total " + total);
    }

    public Order order() { return order; }
}


