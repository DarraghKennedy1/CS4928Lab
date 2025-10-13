package com.cafepos;

import com.cafepos.catalog.Priced;
import com.cafepos.catalog.Product;
import com.cafepos.common.Money;
import com.cafepos.decorator.ExtraShot;
import com.cafepos.decorator.OatMilk;
import com.cafepos.decorator.SizeLarge;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.factory.ProductFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FactoryVsManualTests {

    @Test
    void factory_and_manual_build_same_drink() {
        Product viaFactory = new ProductFactory().create("ESP+SHOT+OAT+L");
        Product viaManual = new SizeLarge(new OatMilk(new ExtraShot(
                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50))
        )));

        assertEquals(viaFactory.name(), viaManual.name());
        assertEquals(((Priced) viaFactory).price(), ((Priced) viaManual).price());

        Order o1 = new Order(101);
        o1.addItem(new LineItem(viaFactory, 1));
        Order o2 = new Order(102);
        o2.addItem(new LineItem(viaManual, 1));

        assertEquals(o1.subtotal(), o2.subtotal());
        assertEquals(o1.totalWithTax(10), o2.totalWithTax(10));
    }
}
