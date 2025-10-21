package com.cafepos;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cafepos.checkout.OrderManager;
import com.cafepos.checkout.OrderManagerFactory;

public class Week6CharacterizationTests {

    @Test
    void no_discount_cash_payment() {
        OrderManager orderManager = OrderManagerFactory.createForCashPayment("NONE");
        String receipt = orderManager.process("ESP+SHOT+OAT", 1, false);
        assertTrue(receipt.startsWith("Order (ESP+SHOT+OAT) x1"));
        assertTrue(receipt.contains("Subtotal: 3.80"));
        assertTrue(receipt.contains("Tax (10%): 0.38"));
        assertTrue(receipt.contains("Total: 4.18"));
    }

    @Test
    void loyalty_discount_card_payment() {
        OrderManager orderManager = OrderManagerFactory.createForCardPayment("LOYAL5");
        String receipt = orderManager.process("LAT+L", 2, false);
        // Latte (Large) base = 3.20 + 0.70 = 3.90, qty 2 => 7.80
        // 5% discount => 0.39, discounted=7.41; tax 10% => 0.74; total=8.15
        assertTrue(receipt.contains("Subtotal: 7.80"));
        assertTrue(receipt.contains("Discount: -0.39"));
        assertTrue(receipt.contains("Tax (10%): 0.74"));
        assertTrue(receipt.contains("Total: 8.15"));
    }

    @Test
    void coupon_fixed_amount_and_qty_clamp() {
        OrderManager orderManager = OrderManagerFactory.createForWalletPayment("COUPON1");
        String receipt = orderManager.process("ESP+SHOT", 0, false);
        // qty=0 clamped to 1; Espresso+SHOT = 2.50 + 0.80 = 3.30; coupon1 => -1 => 2.30; tax=0.23; total=2.53
        assertTrue(receipt.contains("Order (ESP+SHOT) x1"));
        assertTrue(receipt.contains("Subtotal: 3.30"));
        assertTrue(receipt.contains("Discount: -1.00"));
        assertTrue(receipt.contains("Tax (10%): 0.23"));
        assertTrue(receipt.contains("Total: 2.53"));
    }
}


