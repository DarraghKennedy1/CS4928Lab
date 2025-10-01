package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.domain.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OrderTotalsTests {

    @Test
    void order_totals() {
        var p1 = new SimpleProduct("A", "A", Money.of(2.50));
        var p2 = new SimpleProduct("B", "B", Money.of(3.50));
        var o = new Order(1);
        o.addItem(new LineItem(p1, 2));
        o.addItem(new LineItem(p2, 1));
        assertEquals(Money.of(8.50), o.subtotal());
        assertEquals(Money.of(0.85), o.taxAtPercent(10));
        assertEquals(Money.of(9.35), o.totalWithTax(10));
    }

    @Test
    void order_empty_subtotal() {
        var order = new Order(1);
        assertEquals(Money.zero(), order.subtotal());
    }

    @Test
    void order_single_item() {
        var product = new SimpleProduct("TEST", "Test Product", Money.of(5.00));
        var order = new Order(1);
        order.addItem(new LineItem(product, 1));
        assertEquals(Money.of(5.00), order.subtotal());
    }

    @Test
    void order_tax_calculation() {
        var product = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
        var order = new Order(1);
        order.addItem(new LineItem(product, 1));
        assertEquals(Money.of(1.00), order.taxAtPercent(10));
        assertEquals(Money.of(11.00), order.totalWithTax(10));
    }

    @Test
    void order_tax_zero_percent() {
        var product = new SimpleProduct("TEST", "Test Product", Money.of(10.00));
        var order = new Order(1);
        order.addItem(new LineItem(product, 1));
        assertEquals(Money.zero(), order.taxAtPercent(0));
        assertEquals(Money.of(10.00), order.totalWithTax(0));
    }

    @Test
    void order_negative_tax_percent() {
        var order = new Order(1);
        assertThrows(IllegalArgumentException.class, () -> order.taxAtPercent(-5));
    }

    @Test
    void order_add_null_line_item() {
        var order = new Order(1);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(null));
    }

    @Test
    void order_id() {
        var order = new Order(12345);
        assertEquals(12345, order.id());
    }

    @Test
    void order_items_immutable() {
        var product = new SimpleProduct("TEST", "Test Product", Money.of(5.00));
        var order = new Order(1);
        order.addItem(new LineItem(product, 1));
        
        var items = order.items();
        assertThrows(UnsupportedOperationException.class, () -> items.add(new LineItem(product, 2)));
    }
}
