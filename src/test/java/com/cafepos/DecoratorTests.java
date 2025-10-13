package com.cafepos;

import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.domain.Order;
import com.cafepos.domain.LineItem;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DecoratorTests {
    
    @Test
    void decorator_single_addon() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);
        
        assertEquals("Espresso + Extra Shot", withShot.name());
        assertEquals(Money.of(3.30), ((Priced) withShot).price());
    }
    
    @Test
    void decorator_stacks() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        
        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        assertEquals(Money.of(4.50), ((Priced) decorated).price());
    }
    
    @Test
    void decorator_stacking_order_independence() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        
        // Different stacking orders should have same total price
        Product order1 = new SizeLarge(new OatMilk(new ExtraShot(espresso)));
        Product order2 = new ExtraShot(new OatMilk(new SizeLarge(espresso)));
        
        assertEquals(((Priced) order1).price(), ((Priced) order2).price());
        assertEquals(Money.of(4.50), ((Priced) order1).price());
    }
    
    @Test
    void order_uses_decorated_price() {
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso); // 3.30
        
        Order o = new Order(1);
        o.addItem(new LineItem(withShot, 2));
        
        assertEquals(Money.of(6.60), o.subtotal());
    }

    @Test
    void factory_parses_recipe() {
        com.cafepos.factory.ProductFactory f = new com.cafepos.factory.ProductFactory();
        Product p = f.create("ESP+SHOT+OAT");
        assertTrue(p.name().contains("Espresso"));
        assertTrue(p.name().contains("Extra Shot"));
        assertTrue(p.name().contains("Oat Milk"));
        assertEquals(Money.of(3.80), ((Priced) p).price());
    }
}
