package com.cafepos;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.payment.CardPayment;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.payment.WalletPayment;

public class PaymentStrategyTest {

    @Test
    void payment_strategy_called() {
        var p = new SimpleProduct("A", "Product A", Money.of(5.0));
        var order = new Order(42);
        order.addItem(new LineItem(p, 1));

        final boolean[] called = {false};
        PaymentStrategy fake = o -> called[0] = true;

        order.pay(fake);

        assertTrue(called[0], "Payment strategy should be called");
    }

    @Test
    void card_payment_prints_correct_message() {
        var p = new SimpleProduct("A", "Product A", Money.of(9.35));
        var order = new Order(1);
        order.addItem(new LineItem(p, 1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        PaymentStrategy card = new CardPayment("1234567812341234");
        order.pay(card);

        System.setOut(originalOut); // restore System.out
        String output = out.toString().trim();

        assertTrue(output.contains("****1234"), "Card number should be masked");
        assertTrue(output.matches(".*\\d+\\.\\d{2}.*"), "Output should contain amount with two decimals");
    }

    @Test
    void wallet_payment_prints_correct_message() {
        var p = new SimpleProduct("A", "Product A", Money.of(9.35));
        var order = new Order(2);
        order.addItem(new LineItem(p, 1));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(out));

        PaymentStrategy wallet = new WalletPayment("alice-wallet-01");
        order.pay(wallet);

        System.setOut(originalOut);
        String output = out.toString().trim();

        assertTrue(output.contains("alice-wallet-01"), "Wallet ID should be printed");
        assertTrue(output.matches(".*\\d+\\.\\d{2}.*"), "Output should contain amount with two decimals");
    }

    @Test
    void payment_strategy_called_with_multiple_items() {
        var p1 = new SimpleProduct("A", "Product A", Money.of(5.0));
        var p2 = new SimpleProduct("B", "Product B", Money.of(3.0));
        var order = new Order(3);
        order.addItem(new LineItem(p1, 1));
        order.addItem(new LineItem(p2, 2));

        final boolean[] called = {false};
        PaymentStrategy fake = o -> called[0] = true;

        order.pay(fake);

        assertTrue(called[0], "Payment strategy should be called even with multiple items");
    }
}
