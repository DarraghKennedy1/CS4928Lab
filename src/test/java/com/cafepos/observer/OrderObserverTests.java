package com.cafepos.observer;

import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.SimpleProduct;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderObserverTests {

    @Test
    void observers_notified_on_item_add() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(1);
        o.addItem(new LineItem(p, 1)); // baseline
        List<String> events = new ArrayList<>();
        o.register((order, evt) -> events.add(evt));
        o.addItem(new LineItem(p, 1));
        assertTrue(events.contains("itemAdded"));
    }

    @Test
    void multiple_observers_receive_ready_event() {
        var p = new SimpleProduct("A", "A", Money.of(2));
        var o = new Order(2);
        List<String> a = new ArrayList<>();
        List<String> b = new ArrayList<>();
        o.register((order, evt) -> a.add(evt));
        o.register((order, evt) -> b.add(evt));
        o.markReady();
        assertTrue(a.contains("ready"));
        assertTrue(b.contains("ready"));
    }
}


